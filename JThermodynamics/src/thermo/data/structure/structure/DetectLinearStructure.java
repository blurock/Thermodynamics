/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import java.io.IOException;
import java.sql.SQLException;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.matching.SubstituteLinearStructures;

/**
 *
 * @author edwardblurock
 */
public class DetectLinearStructure extends SubstituteLinearStructures {

    public DetectLinearStructure(ThermoSQLConnection c) throws SQLException, CDKException, ClassNotFoundException, IOException {
        super(c);
    }
    public boolean isLinear(AtomContainer mol) throws CDKException, IOException {
    	AtomContainer sub = substitute(mol);
        //StructureAsCML cmlstruct0 = new StructureAsCML(sub);
        //System.out.println(cmlstruct0.getCmlStructureString());
        //Molecule sub1 = substitute(sub);
        //StructureAsCML cmlstruct = new StructureAsCML(sub1);
        //System.out.println(cmlstruct.getCmlStructureString());
        boolean ans = false;
        if(sub.getAtomCount() == 1)
            ans = true;
        return ans;
    }

}
