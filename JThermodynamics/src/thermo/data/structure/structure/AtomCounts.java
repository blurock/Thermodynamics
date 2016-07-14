/*
 * AtomCounts
 *
 * This class isolates the atom names (through getSymbol() of IAtom)
 * and determines the number atoms of each type.
 *
 * The main usage is for the isomer formula, writing the molecule as a string
 * and for the NASA polynomials
 */
package thermo.data.structure.structure;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtom;

/**
 * AtomCounts
 *
 * From an AtomContainer, the number of each atom (determined by the symbol
 * name, IAtom.getSymbol() ) The basic storage is in the derived
 * Hashtable<String, Integer>
 *
 * @author edwardblurock
 */
public class AtomCounts extends Hashtable<String, Integer> implements Comparable {

    //! The set of atom names
    private java.util.HashSet<String> atomNames = null;
    //! The names of the hydrogen and carbon (for use in making the isomer name.
    private String hydrogenS = new String("H");
    private String carbonS = new String("C");
    //! The molecule ID
    public String moleculeID;

    /**
     * Empty Constructor
     */
    public AtomCounts() {
        atomNames = new HashSet<String>();
    }

    /**
     * Constructor
     *
     * @param atoms The molecule to analyze
     *
     * From the molecule, the derived Hashtable is filled in with the atoms
     * (IAtom.getSymbol()) and the corresponding counts
     */
    public AtomCounts(AtomContainer atoms) {
        super();
        moleculeID = atoms.getID();
        getAtoms(atoms);
        setUpHasTable();
        countAtoms(atoms);
    }

    public String getMoleculeID() {
        return moleculeID;
    }

    public void setMoleculeID(String moleculeID) {
        this.moleculeID = moleculeID;
    }

    public HashSet<String> getAtomNames() {
        return atomNames;
    }

    /**
     * Set up the array of unique atom names.
     *
     * The atom names are derived from IAtom.getSymbol()
     *
     * @param atoms The molecule
     */
    private HashSet<String> getAtoms(AtomContainer atoms) {
        atomNames = new HashSet<String>();
        Iterator<IAtom> iter = atoms.atoms().iterator();
        while (iter.hasNext()) {
            IAtom atm = iter.next();
            String name = atm.getSymbol();
            if (!atomNames.contains(name)) {
                atomNames.add(name);
            }
        }
        return atomNames;
    }

    /**
     * From the list of unique atom names, the Hashtable<String,Integer> is set
     * up
     *
     * The table is initialized with zero counts for each atom
     */
    private void setUpHasTable() {
        Iterator<String> names = atomNames.iterator();
        while (names.hasNext()) {
            String name = names.next();
            Integer zero = new Integer(0);
            this.put(name, zero);
        }
    }

    /**
     * Fills in the hash table (after unique set of names is determined).
     *
     * @param atoms The molecule
     */
    private void countAtoms(AtomContainer atoms) {
        Iterator<IAtom> iter = atoms.atoms().iterator();
        while (iter.hasNext()) {
            IAtom atm = iter.next();
            String name = atm.getSymbol();
            Integer count = this.get(name);
            count++;
            this.put(name, count);
        }
    }

    /**
     * Determine the list of atom names (symbol names)
     *
     * This calls getAtomStringArray with zero as the size.
     *
     * @return The array of atom names
     */
    public String[] getAtomStringArray() {
        return getAtomStringArray(0);
    }

    /**
     * Determine the list of atom names (symbol names)
     *
     * @param sze The desired size of the array
     * @return The array of atom names
     *
     * If sze is less than that actual size of the number of atoms types, then
     * the size of the array will be increased correspondingly. If sze is
     * greater than the actual size, the names are filled in with empty strings.
     */
    public String[] getAtomStringArray(int sze) {
        if (sze < atomNames.size()) {
            sze = atomNames.size();
        }
        String[] atoms = new String[sze];
        atomNames.toArray(atoms);
        if (atomNames.size() < sze) {
            for (int i = atomNames.size(); i < sze; i++) {
                atoms[i] = new String("");
            }
        }
        return atoms;
    }

    /**
     * Determine the corresponding (to atom names) counts of each atom
     *
     * This calls correspondingAtomCount with size 0
     *
     * @return Array of corresponding (to atom name array) counts of each atom
     */
    public int[] correspondingAtomCount() {
        return correspondingAtomCount();
    }

    /**
     * Determine the corresponding (to atom names) counts of each atom
     *
     * @param sze
     * @return Array of corresponding (to atom name array) counts of each atom
     * If sze is less than that actual size of the number of atoms types, then
     * the size of the array will be increased correspondingly. If sze is
     * greater than the actual size, the names are filled in with zeroes
     */
    public int[] correspondingAtomCount(int sze) {
        if (sze < atomNames.size()) {
            sze = atomNames.size();
        }
        int[] atomcounts = new int[sze];
        if (atomNames.size() < sze) {
            for (int i = atomNames.size(); i < sze; i++) {
                atomcounts[i] = 0;
            }
        }
        Integer[] counts = new Integer[atomNames.size()];
        Iterator<String> names = atomNames.iterator();
        int i = 0;
        while (names.hasNext()) {
            Integer count = this.get(names.next());
            atomcounts[i++] = count.intValue();
        }
        return atomcounts;
    }

    /**
     * Determine the isomer name from the atom counts
     *
     * @return The isomer name
     *
     * This, by default, lists the carbon first (if any) and then the hydrogens
     * (if any).
     */
    public String isomerName() {
        Integer carbons = this.get(carbonS);

        StringBuffer name = new StringBuffer();
        if (carbons != null) {
            name.append(carbonS);
            if (carbons.intValue() > 1) {
                name.append(carbons.toString());
            }
        }
        Integer hydrogens = this.get(hydrogenS);
        if (hydrogens != null) {
            name.append(hydrogenS);
            if (hydrogens.intValue() > 1) {
                name.append(hydrogens.toString());
            }
        }
        Iterator<String> iter = atomNames.iterator();
        while (iter.hasNext()) {
            String atomname = iter.next();
            if (!atomname.equals(carbonS) && !atomname.equals(hydrogenS)) {
                Integer count = this.get(atomname);
                if (count.intValue() > 0) {
                    name.append(adjustedAtomName(atomname));
                    if (count.intValue() > 1) {
                        name.append(count.toString());
                    }
                }
            }
        }
        return name.toString();
    }

    /**
     *
     * @param atomname The original atom name
     * @return Converts to a proper name.
     *
     * The rule is that the first character is capital and the rest are lower
     * case
     */
    private String adjustedAtomName(String atomname) {
        StringBuffer buf = new StringBuffer();
        buf.append(atomname.substring(0, 1).toUpperCase());
        for (int i = 1; i < atomname.length(); i++) {
            buf.append(atomname.substring(i, i + 1).toLowerCase());
        }
        return buf.toString();
    }

    public int findCountOfAtom(String atomS) {
        int count = 0;
        Set<String> keyset = this.keySet();
        Iterator<String> iter = keyset.iterator();
        boolean notdone = true;
        while (iter.hasNext() && notdone) {
            String name = iter.next();
            if (name.equalsIgnoreCase(atomS)) {
                notdone = false;
                Integer countI = this.get(name);
                count = countI.intValue();
            }
        }
        return count;
    }

    public HashSet<AtomCount> vectorOfAtomCounts() {
        HashSet<AtomCount> vec = new HashSet<AtomCount>();
        Iterator<String> iter = atomNames.iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            Integer countI = this.get(name);
            AtomCount cnt = new AtomCount(moleculeID, name, countI.intValue());
            vec.add(cnt);
        }
        return vec;
    }

    public boolean strictlyLessThanOrEqual(AtomCounts o) {
        boolean ans = false;
        if (o.size() <= this.size()) {
            Iterator<String> iter = o.keySet().iterator();
            ans = true;
            while (iter.hasNext() && ans) {
                String key = iter.next();
                if (!key.equalsIgnoreCase("R")) {
                    Integer count = this.get(key);
                    if (count != null) {
                        Integer ocount = o.get(key);
                        if (ocount > count) {
                            ans = false;
                        }
                    } else {
                        ans = false;
                    }
                }
            }
        }
        //System.out.println("strictlyLessThanOrEqual: \t" + ans + "\t" + o.getMoleculeID() + " :\t " + this.getMoleculeID());
        return ans;
    }

    private int numberOfAtoms() {
        int count = 0;
        Iterator<String> iterator = this.atomNames.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            Integer c = this.get(name);
            count += c.intValue();
        }
        return count;
    }

    public int compareTo(Object o) {
        AtomCounts counts = (AtomCounts) o;
        //int natomsthis = this.numberOfAtoms();
        //int natomscomp = counts.numberOfAtoms();
        int ans = 0;
        if (this.size() < counts.size()) {
            ans = 1;
        } else if (this.size() > counts.size()) {
            ans = -1;
        } else {
            ans = compareAtom(counts, carbonS);
            if (ans == 0) {
                ans = compareAtom(counts, hydrogenS);
                if (ans == 0) {
                    Iterator<String> iter = counts.keySet().iterator();
                    while (iter.hasNext() && ans == 0) {
                        String name = iter.next();
                        if (!name.equalsIgnoreCase(carbonS)
                                || !name.equalsIgnoreCase(hydrogenS)) {
                            ans = compareAtom(counts, name);
                        }
                    }
                }
            }
        }

        return ans;
    }

    /**
     * Used for ordering molecules to make a comparator
     *
     * @param o The atom counts
     * @param atomS
     * @return
     */
    public int compareAtom(AtomCounts o, String atomS) {
        int ans = 0;
        Integer oatm = o.findCountOfAtom(atomS);
        Integer atm = this.findCountOfAtom(atomS);
        if (oatm == null) {
            if (atm == null) {
                ans = 0;
            } else {
                ans = -1;
            }
        } else {
            if (oatm == null) {
                ans = -1;
            } else {
                if (oatm > atm) {
                    ans = 1;
                } else {
                    ans = -1;
                }
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return this.isomerName();
    }
}
