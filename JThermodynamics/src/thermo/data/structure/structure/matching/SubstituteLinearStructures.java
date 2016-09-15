/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.matching;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author edwardblurock
 */
public class SubstituteLinearStructures  {
    ThermoSQLConnection connect;
    String linearS = "LinearStructure";
    String linearPrefix = "L-";
    SQLMetaAtomDefinitionFromMetaAtomInfo sqlSubstitution;
    SetOfMetaAtomsForSubstitution substitutions;

    public SubstituteLinearStructures(ThermoSQLConnection c) throws SQLException, CDKException, ClassNotFoundException, IOException {
        connect = c;
        sqlSubstitution = new SQLMetaAtomDefinitionFromMetaAtomInfo(connect);
            substitutions = sqlSubstitution.createSubstitutionSets(linearS);
    }

    public AtomContainer substitute(AtomContainer mol) throws CDKException, IOException {
        StructureAsCML cmlstruct = new StructureAsCML(mol);
        return substitute(cmlstruct);
    }
    public AtomContainer substitute(StructureAsCML cmlstruct) throws CDKException, IOException {
        try {
        	AtomContainer molecule = substitutions.substitute(cmlstruct);
            //StructureAsCML cmlstruct1 = new StructureAsCML(molecule);
            //Molecule molecule1 = substitutions.substitute(cmlstruct1);
            condenseConnectedLinearStructures(molecule);

            return molecule;
        } catch (ClassNotFoundException ex) {
            throw new IOException("ERROR in SubstituteLinearStructures.substitute");
        }
    }

    private void condenseConnectedLinearStructures(AtomContainer molecule) {
        boolean notdone = true;
        Iterator<IBond> iter = molecule.bonds().iterator();
        while(iter.hasNext() && notdone) {
            IBond bond = iter.next();
            if(bond.getOrder() == IBond.Order.SINGLE) {
                IAtom atm1 = bond.getAtom(0);
                IAtom atm2 = bond.getAtom(1);
                if(isLinearAtom(atm1) && isLinearAtom(atm2)) {
                    
                    notdone = false;
                    String symbol = createNewLinearAtomName(atm1,atm2);
                    List<IAtom> bonded = molecule.getConnectedAtomsList(atm2);
                    System.out.println("Bond:" + atm1.getSymbol() + ", " + atm2.getSymbol());
                    System.out.println("Bonds with 2: " + bonded.size());
                    if(bonded.size() == 2) {
                        IAtom connect = bonded.get(0);
                        if(connect.equals(atm1)) {
                            connect = bonded.get(1);
                        }
                        System.out.println("Connected: " + connect.getSymbol());
                        Bond bnd = new Bond(connect,atm1);
                        molecule.addBond(bnd);
                        IBond atm2bond = molecule.getBond(atm2,connect);
                        molecule.removeBond(atm2bond);
                        
                    }

                    atm1.setSymbol(symbol);
                    molecule.removeBond(bond);
                    molecule.removeAtom(atm2);
                    condenseConnectedLinearStructures(molecule);
                }
            }
        }
    }
    private boolean isLinearAtom(IAtom atm) {
        boolean ans = false;
        if(atm.getSymbol().startsWith(linearPrefix))
            ans = true;
        return ans;
    }
    private String createNewLinearAtomName(IAtom atm1, IAtom atm2) {
        StringBuffer buf = new StringBuffer();
        buf.append(linearPrefix);
        int sze = linearPrefix.length();
        buf.append(atm1.getSymbol().substring(sze));
        buf.append("-");
        buf.append(atm2.getSymbol().substring(sze));
        return buf.toString();
    }


}
