/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import thermo.data.structure.structure.matching.SubstituteMetaAtom;
import thermo.data.structure.utilities.MoleculeUtilities;

/** Set of Meta atoms to substitute into a molecule
 *
 * The class is derived from an {@link ArrayList} of {@link SubstituteMetaAtom}
 * which are derived from {@link MetaAtomDefinition}.
 *
 * The main operation is substitute which loops through the definition and
 * substitutes the meta atoms
 *
 * @author blurock
 */
public class SetOfMetaAtomsForSubstitution extends ArrayList<SubstituteMetaAtom> {

    int topPriority = 5;

    /**
     * 
     */
    public SetOfMetaAtomsForSubstitution() {
    }

    /** Create and add a {@link SubstituteMetaAtom} from name and structure
     *
     * @param name The name of the meta atom
     * @param cmlmol The structure to substitute
     * @throws CDKException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void addDefinition(String name, StructureAsCML cmlmol) throws CDKException, ClassNotFoundException, IOException {
        MetaAtomDefinition def = new MetaAtomDefinition(name, cmlmol);
        SubstituteMetaAtom sub = new SubstituteMetaAtom(def);
        this.add(sub);
    }

    /** Add a {@link SubstituteMetaAtom} to the list derived from {@link MetaAtomDefinition}.
     *
     * @param def The meta atom definition
     */
    public void addDefinition(MetaAtomDefinition def) {
        SubstituteMetaAtom sub = new SubstituteMetaAtom(def);
        this.add(sub);
    }
    /**
     * 
     * @param struct The molecule structure to be substituted in CML form
     * @return The molecule with the meta atoms substituted.
     * @throws CDKException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Molecule substitute(StructureAsCML struct) throws CDKException, ClassNotFoundException, IOException {
        NormalizeMoleculeFromCMLStructure norm = new NormalizeMoleculeFromCMLStructure();
        Molecule molecule = norm.getNormalizedMolecule(struct);
        return substitute(molecule);
        }

/**
 *
 * @param molecule The molecule to be substituted
 * @return The substituted molecule
 * @throws CDKException
 * @throws ClassNotFoundException
 * @throws IOException
 *
 * The set of meta atoms (within this class) are iterated over in terms of their priority (top priority is set by {@link topPriority}).
 * The molecule is normalized at the end of the operation.
 *
 * The actual substitution is done by the {@link SubstituteMetaAtom} class.
 *
 */
public Molecule substitute(Molecule molecule) throws CDKException, ClassNotFoundException, IOException {
        for (int pr = topPriority; pr >= 0; pr--) {

            Iterator<SubstituteMetaAtom> sub = this.iterator();
            while (sub.hasNext()) {
                
                SubstituteMetaAtom substitute = sub.next();
                System.out.println(substitute.getID() + "\t Priority: " + substitute.getPriority());
                if (substitute.getPriority() == pr) {
                    System.out.println(pr + "------------ Substitute -----------------\n");
                    System.out.println(substitute.toString());
                    substitute.substitute(molecule);
                StructureAsCML cml = new StructureAsCML(molecule);
                System.out.println("---- Result -----\n");
                System.out.println(cml.getCmlStructureString());
                }
            }
        }
        MoleculeUtilities.normalizeMolecule(molecule);
        return molecule;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append("   --- SetOfMetaAtomsForSubstitution   -----\n");
        buf.append("            MetaAtomDefinitions\n");
        Iterator<SubstituteMetaAtom> subs = this.iterator();
        while (subs.hasNext()) {
            buf.append("--------------------------------------------------\n");
            SubstituteMetaAtom smeta = subs.next();
            buf.append(smeta.toString());
        }

        return buf.toString();
    }
}
