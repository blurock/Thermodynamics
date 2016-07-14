/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IElectronContainer;
import org.openscience.cdk.interfaces.ISingleElectron;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import thermo.data.structure.utilities.MoleculeUtilities;
import thermo.exception.NotARadicalException;

/** AddHydrogenToSingleRadical
 * Substitute a hydrogen for a radical.
 * This class is used for the calulations of the thermodynamic corrections
 * for radicals.
 *
 * This class is used for the vibrational corrections
 * {@link CalculateVibrationalCorrectionForRadical}.
 *
 * @author edwardblurock
 */
public class AddHydrogenToSingleRadical {

    /**
     *
     */
    public AddHydrogenToSingleRadical(){

    }

    public boolean isARadical(Molecule mol) {
        boolean ans = mol.getSingleElectronCount() > 0;
        return ans;
    }
    /** Convert radical R. to RH
     *
     * This finds a single radical in the molecule
     * removes the radical from the atom
     * and bonds a generic hydrogen atom to it.
     *
     * @param mol The radical (R.)
     * @return The molecule with the hydrogen (RH)
     * @throws NotARadicalException
     */
    public Molecule convert(Molecule mol) throws NotARadicalException {
        Molecule molecule = null;
        try {
            molecule = (Molecule) mol.clone();
            IAtom atm = null;
            List<ISingleElectron> electrons = null;
            boolean notfound = true;
            int count = 0;
            while (notfound && count < molecule.getAtomCount()) {
                atm = molecule.getAtom(count);
                electrons = molecule.getConnectedSingleElectronsList(atm);
                if (electrons.size() > 0) {
                    notfound = false;
                } else {
                    count++;
                }
            }
            if (!notfound) {
                ISingleElectron electron = electrons.get(0);
                molecule.removeSingleElectron(electron);
                IAtom hydrogen = formHydrogenAtom();
                molecule.addAtom(hydrogen);
                Bond bnd = new Bond(atm, hydrogen, Order.SINGLE);
                molecule.addBond(bnd);

            } else {
                throw new NotARadicalException("Radical Not Found");
            }

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(AddHydrogenToSingleRadical.class.getName()).log(Level.SEVERE, null, ex);
        }
        MoleculeUtilities.assignIDs(molecule);
        return molecule;
    }
    private IAtom formHydrogenAtom() {
        Atom hydrogen = new Atom("H");
        return hydrogen;
    }
}
