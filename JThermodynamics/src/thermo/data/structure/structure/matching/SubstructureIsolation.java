/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.matching;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/** Isolates structures given atom lists
 *
 * The main use is in substituting meta atoms.
 *
 * @author blurock
 */
public class SubstructureIsolation {

    
    /** From a list of indicies, create a list of atoms {@link IAtom}) within the molecules
     * 
     * @param molecule The molecule
     * @param set The list of indicies
     * @return The list of {@link IAtom} within the molecule
     */
    static public List<IAtom> listOfAtomsFromIndicies(IAtomContainer molecule, List<Integer> set) {
        ArrayList<IAtom> atoms = new ArrayList<IAtom>();
        Iterator<Integer> i = set.iterator();
        while(i.hasNext()) {
            Integer iI = i.next();
            atoms.add(molecule.getAtom(iI.intValue()));
        }
        return atoms;
    }
    /** Determine list of bonds within the meta atom definition
     * 
     * @param molecule The molecule
     * @param set The list of atoms in the molecule
     * @return The list of bonds within the atom set
     *
     * Examines the bonds connected to each atom in the set.
     * If the bond involves only atoms in the set, then it is added to the list.
     *
     */
    static public List<IBond> listOfBondsWithAtomSet(IAtomContainer molecule, List<IAtom> set) {
        ArrayList<IBond> bonds = new ArrayList<IBond>();
        Iterator<IAtom> i = set.iterator();
        while(i.hasNext()) {
            IAtom atm = i.next();
            List<IBond> connected = molecule.getConnectedBondsList(atm);
            Iterator<IBond> ibond = connected.iterator();
            while(ibond.hasNext()) {
                IBond bond = ibond.next();
                IAtom a1 = bond.getAtom(0);
                IAtom a2 = bond.getAtom(1);
                if(set.contains(a1) && set.contains(a2))
                    bonds.add(bond);
            }
        }
        return bonds;
    }
    /** Determine the bonds in the molecule connected to the meta atom
     * 
     * @param molecule the molecule
     * @param set The set of atoms
     * @return The list of bonds that connect the atoms in the set to atoms outside the set.
     *
     * Examines the bonds connected to each atom in the set.
     * If the bond is between an atom in the set and an atom outside the set
     * (i.e. the bond does not make up the meta atom itself)
     * then it is added to the bond list
     *
     */
    static public List<IBond> listOfBondsConnectedToAtomSet(IAtomContainer molecule, List<IAtom> set) {
        ArrayList<IBond> bonds = new ArrayList<IBond>();
        Iterator<IAtom> i = set.iterator();
        while(i.hasNext()) {
            IAtom atm = i.next();
            List<IBond> connected = molecule.getConnectedBondsList(atm);
            Iterator<IBond> ibond = connected.iterator();
            while(ibond.hasNext()) {
                IBond bond = ibond.next();
                IAtom a1 = bond.getAtom(0);
                IAtom a2 = bond.getAtom(1);
                if(!(set.contains(a1) && set.contains(a2)))
                    bonds.add(bond);
            }
        }
        return bonds;
    }
    /** Determine bonds in the molecule connecting atoms within the set to atoms outside the set.
     * 
     * @param molecule The molecule
     * @param bonds List of bonds to examine
     * @param set The set of indicies of the atoms in the molecule
     * @return The set of bonds between atoms in the set and atoms outside the set
     *
     * Each bond in the bond list is examined.
     * If the bond is between an atom in the set and an atom outside the set, then
     * it is added to the return bond list
     *
     */
    static public ArrayList<IBond> listOfBondsConnectedToAtomSet(AtomContainer molecule, ArrayList<IBond> bonds, HashSet<Integer> set) {
        ArrayList<IBond> subset = new ArrayList<IBond>();
        for (int i = 0; i < bonds.size(); i++) {
            IBond bond = bonds.get(i);
            IAtom atm1 = bond.getAtom(0);
            IAtom atm2 = bond.getAtom(1);
            Integer n1 = (Integer) molecule.getAtomNumber(atm1);
            Integer n2 = (Integer) molecule.getAtomNumber(atm2);
            if (set.contains(n1) && !set.contains(n2)) {
                subset.add(bond);
            } if (!set.contains(n1) && set.contains(n2)) {
                subset.add(bond);
            }
        }
        return subset;
    }

}
