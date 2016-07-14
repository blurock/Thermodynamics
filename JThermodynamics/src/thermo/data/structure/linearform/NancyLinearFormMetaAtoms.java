/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.linearform;

import java.io.IOException;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author blurock
 */
public class NancyLinearFormMetaAtoms extends MetaAtomDefinition {

    public NancyLinearFormMetaAtoms(MetaAtomInfo info, StructureAsCML cmlstruct) throws CDKException, ClassNotFoundException, IOException {
        super(info,cmlstruct);
    }
    public NancyLinearFormMetaAtoms(NancyLinearFormMetaAtoms nancy) {
        super(nancy);
    }
}
