/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import java.io.IOException;
import java.util.ArrayList;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.matching.SubstituteMetaAtom;

/**
 *
 * @author reaction
 */
public class SetOfMetaAtoms extends ArrayList<SubstituteMetaAtom> {

    public SetOfMetaAtoms() {
    }

    public void addDefinition(String name, StructureAsCML cmlmol) throws CDKException, ClassNotFoundException, IOException {
        MetaAtomDefinition def = new MetaAtomDefinition(name, cmlmol);
        SubstituteMetaAtom sub = new SubstituteMetaAtom(def);
        this.add(sub);
    }

    public void addDefinition(MetaAtomDefinition def) {
        SubstituteMetaAtom sub = new SubstituteMetaAtom(def);
        this.add(sub);
    }


}
