/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.bonds;

import java.util.StringTokenizer;

/** Bond length of two atoms (in Angstroms)
 * Bonds distinquished by atoms and bond order.
 *
 * @author edwardblurock
 */
public class BondLength implements Comparable<BondLength> {
    String atom1;
    String atom2;
    int bondOrder;
    double bondLength;
    String source;
    /** empty constructor
     */
    public BondLength() {
        atom1 = null;
        atom2 = null;
        bondOrder = 0;
        bondLength = 0.0;
    }
    /** Bond Length information
     *
     * @param a1 Rignt atom
     * @param a2 Left atom
     * @param bondOrder bond order of the bond
     *
     * bondLength is set to zero.
     *
     */
    public BondLength(String a1, String a2, int bondOrder) {
        this.setAtoms(a1,a2);
        this.bondOrder = bondOrder;
        this.source = "";
    }
    /** Bond Length information
     *
     * @param a1 Rignt atom
     * @param a2 Left atom
     * @param bondOrder bond order of the bond
     * @param bondLength The bond length in angstroms.
     * @param src The source description
     *
     */
    public BondLength(String a1, String a2, int bondOrder, double bondLength, String src) {
        setAtoms(a1,a2);
        this.bondOrder = bondOrder;
        this.bondLength = bondLength;
        this.source = src;
    }
    /** This sets the atoms
     *
     * The atoms are put in the structure (in the proper atoms)
     *
     * @param atom1 One of the bonding atoms
     * @param atom2 One of the bonding atoms
     */
    public void setAtoms(String atom1, String atom2) {
        String a1 = atom1.toLowerCase();
        String a2 = atom2.toLowerCase();
        if(atom1.length() < atom2.length()) {
            a1 = atom2;
            a2 = atom1;
        } else if(atom1.length() == atom2.length()) {
            if(atom1.getBytes()[0] < atom2.getBytes()[0]) {
                    a1 = atom2;
                    a2 = atom1;
            } else if(atom1.getBytes()[0] == atom2.getBytes()[0]) {
                if(atom1.length() > 1 && atom1.getBytes()[1] < atom2.getBytes()[1]) {
                    a1 = atom2;
                    a2 = atom1;
                }
            }
        }
        this.atom1 = a1;
        this.atom2 = a2;
    }
    /** Right atom of bond
     *
     * @return Right atom
     */
    public String getAtom1() {
        return atom1;
    }
    /** Left atom of bond
     *
     * @return
     */
    public String getAtom2() {
        return atom2;
    }
    /** The bond order
     *
     * @return The bond order
     */
    public int getBondOrder() {
        return bondOrder;
    }
    /** Get the bond length
     *
     * @return The bond length in Angstroms
     */
    public double getBondLength() {
        return bondLength;
    }

    /** Get the source decription
     *
     * @return the source description
     */
    public String getSource() {
        return source;
    }

   /** Set the bond length
    *
    * @param bondLength The bond length in Angstroms
    */
   public void setBondLength(double bondLength) {
        this.bondLength = bondLength;
    }

   /** Read the arguments from one line.
    *
    * @param line
    *
    * On the line, the arguments are space deliminated
    *
    * Atom1, Atom2, bondOrder, bondLength, Source
    *
    */
   public void readLine(String line, String srcS) {
       StringTokenizer tok = new StringTokenizer(line);
       if(tok.countTokens() >= 4) {
           String a1 = tok.nextToken();
           String a2 = tok.nextToken();
           String bondOrderS = tok.nextToken();
           String bondLengthS = tok.nextToken();
           Integer bondOrderI = new Integer(bondOrderS);
           Double bondLengthD = new Double(bondLengthS);
           setAtoms(a1,a2);
           this.bondOrder = bondOrderI.intValue();
           this.bondLength = bondLengthD.doubleValue();
           this.source = srcS;
       }
   }
    /** Convert to string
     *
     * @return The printable string verson
     */
    @Override
    public String toString() {
        return "BondLength[" + atom1 + " -" + bondOrder + "- " + atom2 + "] = " + bondLength + " A";
    }

    /** Compare two bond lengths.
     *
     * Only the atoms and the bond order are compared
     *
     * @param t The bond length structure
     * @return  0: equal   -1: t > this  1: t < this
     */
    public int compareTo(BondLength t) {
        int ans = 1;
        int a1 = this.atom1.compareTo(t.atom1);
        int a2 = this.atom2.compareTo(t.atom2);

        if(a1 == 0 && a2==0) {
            ans = this.bondOrder - t.bondOrder;
        } else if(a1 != 0) {
            ans = a1;
        } else {
            ans = a2;
        }
        return ans;
    }
}
