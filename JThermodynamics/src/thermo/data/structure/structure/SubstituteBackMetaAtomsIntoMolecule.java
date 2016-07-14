/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import thermo.data.structure.utilities.MoleculeUtilities;

/** Substitute single meta atom with structure
 *
 * This class is built upon a list of meta atoms (MetaAtomDefinition)
 * which are candidates for substitution into a molecule.
 *
 * The substitute(IAtomContainer molecule) procedure loops through
 * the list of meta atoms and attempts to substutite (call to
 * substituteSingleMetaAtom).
 *
 * It is assumed in this class that the meta atom definition has only
 * one atom which connects within the molecule. This assumption is the
 * only way the substitution can be unique without adding extra information
 * (such as ordering, both in this defintion and in the connections in
 * molecule). The one atom of the meta atom has the 'unspecified'
 * connections and the rest of the 'specified' atoms are connected
 * among themselves and to this connected atom.
 *
 * @author blurock
 */
public class SubstituteBackMetaAtomsIntoMolecule extends ArrayList<MetaAtomDefinition> {

    public SubstituteBackMetaAtomsIntoMolecule() {
    }
    public void addDefinition(String name, StructureAsCML cmlmol) throws CDKException, ClassNotFoundException, IOException {
        MetaAtomDefinition def = new MetaAtomDefinition(name, cmlmol);
        this.add(def);
    }

    public void addDefinition(MetaAtomDefinition def) {
        this.add(def);
    }

    /** Substitute the list of meta atoms in molecule
     *
     * The input molecule is modified.
     *
     * This routine loops through the meta atoms in the inhereted list
     * and trys substituting them one by one (substituteSingleMetaAtom).
     * The ids are renumbered to be consectutive.
     *
     * @param molecule The molecule to modify with substitutions
     */
    public void substitute(IAtomContainer molecule) {
        Iterator<MetaAtomDefinition> inancy = this.iterator();
        while (inancy.hasNext()) {
            MetaAtomDefinition nancy = inancy.next();
            System.out.println("substitute: " + nancy.toString());
            substituteSingleMetaAtom(nancy, molecule);
        }
        MoleculeUtilities.assignIDs(molecule);
    }
/** Substitute, multiple times, a meta atom in a molecule
 * 
 * This loops through each atom in the molecule to see whether it
 * matches the meta atom. If it does, then the substitution proceeds
 * through:
 * <ul>
 * <li> replaceConnection
 * <li> addMetaAtomsToMolecule
 * <li> addMetaAtomBondsToMolecule
 * </ul>
 * 
 * @param nancy The meta atom
 * @param molecule The molecule to be modified.
 */
    private void substituteSingleMetaAtom(MetaAtomDefinition nancy, IAtomContainer molecule) {
        Iterator<IAtom> iatoms = molecule.atoms().iterator();
        boolean notfound = true;
        while (iatoms.hasNext() && notfound) {
            IAtom atom = iatoms.next();
            String atomsymbol = atom.getSymbol().toLowerCase();
            if (atomsymbol.equals(nancy.getMetaAtomName())) {
                IAtom replacement = replaceConnection(nancy, atom, molecule);
                Hashtable<IAtom, IAtom> correspondences = addMetaAtomsToMolecule(nancy, replacement, molecule);
                addMetaAtomBondsToMolecule(nancy,correspondences,molecule);
                molecule.removeAtom(atom);
                notfound = false;
                substituteSingleMetaAtom(nancy, molecule);
                
            }
        }
    }
    /** Replace meta atom with the connecting atom in meta atom definition
     *
     * A copy of the connecting atom in the meta atom definition is
     * found, and put in the molecule.
     * The connections of meta atom found in the molecule are replaced with
     * connections to the new connecting atom.
     * The meta atom found in the molecule is removed.
     *
     * @param nancy The meta atom definition
     * @param atom the meta atom in molecule
     * @param molecule the molecule
     * @return The new connected atom in the molecule (substitutes the meta atom position)
     */
    private IAtom replaceConnection(MetaAtomDefinition nancy, IAtom atom, IAtomContainer molecule) {
        int id = nancy.getConnectingAtoms().get(0).intValue();
        Atom replacement = new Atom(nancy.getMolecule().getAtom(id));
        replacement.setID(atom.getID());
        molecule.addAtom(replacement);
        List<IBond> bonds = molecule.getConnectedBondsList(atom);
        Iterator<IBond> ibonds = bonds.iterator();
        while (ibonds.hasNext()) {
            IBond bond = ibonds.next();
            IAtom atm1 = bond.getAtom(0);
            IAtom atm2 = bond.getAtom(1);
            if (atm1.equals(atom)) {
                Bond bnd = new Bond(atm2, replacement);
                molecule.addBond(bnd);
            } else {
                Bond bnd = new Bond(atm1, replacement);
                molecule.addBond(bnd);
            }
            molecule.removeBond(bond);
        }
        return replacement;
    }
/** The 'specified' atoms of the meta atom definition are added to the molecule
 *
 * The specified atoms of the meta atom definition are copied and placed in the
 * molecule. The copy and the original are put in a correspondence hashtable.
 * This hash table is used to form the bonds in addMetaAtomBondsToMolecule.
 *
 * @param nancy
 * @param replacement
 * @param molecule
 * @return The correspondence hashtable
 */
    private Hashtable<IAtom, IAtom> addMetaAtomsToMolecule(MetaAtomDefinition nancy, IAtom replacement, IAtomContainer molecule) {
        IAtomContainer metaatommol = nancy.getMolecule();

        Hashtable<IAtom, IAtom> correspondences = new Hashtable<IAtom, IAtom>();
        int idconnect = nancy.getConnectingAtoms().get(0).intValue();
        IAtom metareplace = metaatommol.getAtom(idconnect);
        correspondences.put(metareplace, replacement);

        List<Integer> specified = nancy.getSpecifiedAtoms();
        int connected = nancy.getConnectingAtoms().get(0).intValue();
        Iterator<Integer> ids = specified.iterator();
        while (ids.hasNext()) {
            int id = ids.next().intValue();
            if (id != connected) {
                IAtom toadd = metaatommol.getAtom(id);
                IAtom added = new Atom(toadd);
                correspondences.put(toadd, added);
                molecule.addAtom(added);
            }
        }
        return correspondences;
    }
/** Add the meta atom bonds
 *
 * The specified atoms are looped over to insert all their bonds. A list of
 * bonds added is kept (used) so as not to add the same bond twice.  The
 * correspondences is used to find the replacement atoms in the molecule
 * to use to make the bonds. The bond order of the meta atom bond is
 * transfered.
 *
 * @param nancy the meta atom definition
 * @param correspondences The correspondence between meta atoms (original and replacements)
 * @param molecule The modifed molecule
 */
    private void addMetaAtomBondsToMolecule(MetaAtomDefinition nancy, Hashtable<IAtom, IAtom> correspondences, IAtomContainer molecule) {
        List<Integer> specified = nancy.getSpecifiedAtoms();
        IAtomContainer metaatommol = nancy.getMolecule();
        int connected = nancy.getConnectingAtoms().get(0).intValue();
        Iterator<Integer> ids = specified.iterator();
        ArrayList<IBond> used = new ArrayList<IBond>();
        while (ids.hasNext()) {
            int id = ids.next().intValue();
            if (id != connected) {
                IAtom toadd = metaatommol.getAtom(id);
                List<IBond> bonds = metaatommol.getConnectedBondsList(toadd);
                Iterator<IBond> bond = bonds.iterator();
                while (bond.hasNext()) {
                    IBond bnd = bond.next();
                    if(!used.contains(bnd)) {
                        used.add(bnd);
                        IAtom atm1 = bnd.getAtom(0);
                        IAtom atm2 = bnd.getAtom(1);
                        IAtom replace1 = correspondences.get(atm1);
                        IAtom replace2 = correspondences.get(atm2);
                        Bond newbond = new Bond(replace1, replace2, bnd.getOrder());
                        molecule.addBond(newbond);
                    }
                }
            }
        }
    }

}
