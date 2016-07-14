/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.io.IOException;
import java.util.List;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author blurock
 */
public class OpticalSymmetry  extends SymmetryDefinition {
    String opticalSymmetryName = "OpticalIsomer";
        /**
     * 
     * @param symname
     * @param structure
     * @param pairlist
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public OpticalSymmetry(String symname, 
            StructureAsCML structure, 
            List<SymmetryPair> pairlist) throws CDKException, ClassNotFoundException, IOException {
        super(symname, structure,pairlist);
        this.setMetaAtomType(opticalSymmetryName);
    }
    public OpticalSymmetry(OpticalSymmetry symmetry) {
        super(symmetry);
    }

}
