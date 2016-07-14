/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

/** Atom Count:
 * This is the number of instances of the atom type
 * (represented by symbolName, meant to be the atom name)
 * is found (atomCount) within the Molelcule.
 *
 * This structure has a direct correspondence with the SQL database
 *
 * @author edwardblurock
 */
public class AtomCount {
    String Molecule;
    String symbolName;
    int atomCount;

    /** Empty Constructor
     *
     */
    public AtomCount() {
    }
    /** The full constructor
     * 
     * @param molname The name of the molecule
     * @param name The atom symbol name
     * @param count The number of times this symbol is found within molecule
     */
    public AtomCount(String molname, String name, int count){
        Molecule = molname;
        symbolName = name.toUpperCase();
        atomCount = count;
    }

    /** Get the number of atoms of type in Molecule
     *
     * @return The number of this type of atom
     */
    public int getAtomCount() {
        return atomCount;
    }

    /** Set the number of atoms of the type in the Molecule
     *
     * @param atomCount
     */
    public void setAtomCount(int atomCount) {
        this.atomCount = atomCount;
    }

    /** The atom type
     *
     * The atom type is usually atomic name (or pseudo atom name).
     *
     * @return The atom type
     */
    public String getSymbolName() {
        return symbolName;
    }

    /** Set the atom symbol name
     *
     * @param symbolName
     */
    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }
    /** The name of the molecule (reference to other data in the database)
     *
     * @return the name of the molecule
     */
    public String getMolecule() {
        return Molecule;
    }

    /** Set the name of the molecule
     *
     * @param Molecule
     */
    public void setMolecule(String Molecule) {
        this.Molecule = Molecule;
    }

    public boolean strictlyLessThanOrEqual(AtomCount o) {
        boolean ans = false;
        if(o.getSymbolName().equalsIgnoreCase(this.getSymbolName())) {
            if(o.atomCount <= this.getAtomCount()) {
                ans = true;
            }
        }
        return ans;
    }

}
