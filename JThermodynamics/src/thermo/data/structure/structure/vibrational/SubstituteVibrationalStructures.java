/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.isomorphism.mcss.RMap;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.GetSubstructureMatches;
import thermo.data.structure.structure.vibrational.DB.SQLSetOfVibrationalStructureInfo;

/**
 *
 * @author blurock
 */
public class SubstituteVibrationalStructures {
    SQLSetOfVibrationalStructureInfo sqlvibset;
    SQLStructureAsCML sqlcmlstruct;
    GetSubstructureMatches matches;
    SetOfVibrationalStructureInfo vibstructures;
    
    public SubstituteVibrationalStructures(ThermoSQLConnection connect) throws SQLException {
        sqlvibset = new SQLSetOfVibrationalStructureInfo(connect);
        sqlcmlstruct = new SQLStructureAsCML(connect);
        matches = new GetSubstructureMatches();
        vibstructures = sqlvibset.getSetOfVibrationalStructureInfo();
    }
    /** This finds the number of substitutions of the vibrational structures in a molecule
     * 
     * @param molecule The molecule to substitute
     * @return The set of vibrational structures and the number of times it appears in molecule.
     * @throws java.sql.SQLException
     * @throws org.openscience.cdk.exception.CDKException
     */
    public SetOfVibrationalStructureCounts findSubstitutions(AtomContainer molecule) throws SQLException, CDKException {
        SetOfVibrationalStructureCounts counts = new SetOfVibrationalStructureCounts();
        Iterator<VibrationalStructureInfo> vibinfo = vibstructures.iterator();
        //int largest = 0;
        counts = new SetOfVibrationalStructureCounts();
        while(vibinfo.hasNext()) {
            VibrationalStructureInfo info = vibinfo.next();
            String structurename = info.getStructureName();
            HashSet structvec = sqlcmlstruct.retrieveStructuresFromDatabase(structurename);
            if(structvec.size() >= 1) {
                Iterator<StructureAsCML> iter = structvec.iterator();
                StructureAsCML cmlstruct = iter.next();
                AtomContainer vibmolecule = cmlstruct.getMolecule();
                System.out.println(cmlstruct.getNameOfStructure());
                int vibsize = vibmolecule.getAtomCount();
                List<List<RMap>> bondMatches = matches.getBondMatches(molecule, vibmolecule);
                //List<List<RMap>> bondMatches = matches.getAtomMatches(molecule, vibmolecule);
                    int n = bondMatches.size();
                    double nD = (double) n;
                    double cD = nD / info.getSymmetry();
                    int nI = (int) -cD;
                if(bondMatches.size() > 0) {
                    System.out.println(info.toString());
                    System.out.println(info.getElementName() + "\n Count: " + nD + "\t Symmetry: " + cD + "\t Final: " + nI);
                }
                if(n>0) {
                    VibrationalStructureInfoCount infocount = new VibrationalStructureInfoCount(info, nI);
                    counts.add(infocount);
                }
            }
            
        }
        return counts;
    }
    public String toString() {
        return vibstructures.toString();
    }
 }
