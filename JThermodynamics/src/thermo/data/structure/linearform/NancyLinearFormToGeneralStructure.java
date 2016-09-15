/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.linearform;

import java.sql.SQLException;
import java.util.Iterator;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtom;
import thermo.data.benson.DB.ThermoSQLConnection;

/**
 *
 * @author edwardblurock
 */
public class NancyLinearFormToGeneralStructure extends NancyLinearFormToMolecule {

    String generalAtomStar = "*";
    String generalAtomR = "R";
    String properGeneralAtomS = "Du";

    public NancyLinearFormToGeneralStructure(ThermoSQLConnection c) throws SQLException {
        super(c);
    }

    @Override
    public AtomContainer convert(String linearform) throws SQLException {
    	AtomContainer molecule = super.convert(linearform);

        Iterator<IAtom> iter = molecule.atoms().iterator();
        while (iter.hasNext()) {
            IAtom atm = iter.next();
            String name = atm.getSymbol();
            if (generalAtomToTransform(name)) {
                atm.setSymbol(properGeneralAtomS);
            }
        }
        return molecule;
    }

    private boolean generalAtomToTransform(String name) {
        boolean star = name.equalsIgnoreCase(generalAtomStar);
        boolean R = name.equalsIgnoreCase(generalAtomR);
        boolean ans = star || R;
        return  ans;
    }
}
