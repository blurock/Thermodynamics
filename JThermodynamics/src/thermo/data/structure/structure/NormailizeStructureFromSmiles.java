/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IAtomType.Hybridization;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.SmilesValencyChecker;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

/**
 *
 * @author blurock
 */
public class NormailizeStructureFromSmiles {

        public AtomContainer moleculeFromSmiles(String smiles) {
            IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
            
            AtomContainer molecule = null;
            builder.newInstance(AtomContainer.class, molecule);
        try {
            String cmlstring = null;            
            SmilesParser parser = new SmilesParser(builder);            
            molecule = (AtomContainer) parser.parseSmiles(smiles);
            addHydrogens(molecule);
        } catch (InvalidSmilesException ex) {
            molecule = null;
        } catch (CDKException ex) {
            molecule = null;
        }
       return molecule;
    }
    public void addHydrogens(AtomContainer molecule) throws CDKException {
        SmilesValencyChecker svalency = new SmilesValencyChecker();
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance((IChemObjectBuilder) molecule.getBuilder());

        for (int i = 0; i < molecule.getAtomCount(); i++) {
            IAtom atom = molecule.getAtom(i);
            atom.setHybridization((Hybridization) CDKConstants.UNSET);
            IAtomType type = matcher.findMatchingAtomType(molecule, molecule.getAtom(i));
            AtomTypeManipulator.configure(molecule.getAtom(i), type);
            CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
            adder.addImplicitHydrogens(molecule, molecule.getAtom(i));
            int num = atom.getImplicitHydrogenCount();
            for (int h = 0; h < num; h++) {
                Atom hydrogen = new Atom("H");
                molecule.addAtom(hydrogen);
                Bond bnd = new Bond(atom, hydrogen);
                molecule.addBond(bnd);
            }
            atom.setImplicitHydrogenCount(new Integer(0));
        }

    }

}
