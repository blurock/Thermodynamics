/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.linearform;

/** The atom information and bonding of a node
 *
 * This is formed directly from the Nancy linear form
 * and is used to form a molecule.
 *
 * The bonding information refers to the primary c
 * connecting atom. If the node is a main node of the
 * linked list, the connecting atom is the next
 * in the linked list. If the node is a connecting atom
 * of a main atom of a linked list, then the connection
 * refers to that the main linked list atom.
 * 
 * @author blurock
 */
public class LinearFormAtomGroupString {
    // The string name of the atom or meta atom
    public String atomGroup;
    // true if the atom is a radical
    public boolean radical = false;
    // The number of times this atom is repeated
    public int multiplicity = 1;
    // The cycle number within the Nancy linear form
    public int cycleNumber = 0;
    // true if the bond is aromatic
    public boolean aromaticBond = false;
    // Bond order
    public int bondOrder = 1;
    //
    public int bondOrderForward = 1;
    //
    public boolean endOfCycle = false;
    //
    public boolean bridgeToCycleAtom = false;
    /** The constructor
     * The elements are all public, so the constructor is empty
     */
    public LinearFormAtomGroupString() {
    }
    
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        buf.append(atomGroup);
        if(multiplicity > 1)
            buf.append(multiplicity);
        if(radical)
            buf.append("(.)");
        buf.append(": ");
        if(aromaticBond)
            buf.append("A");
        else if(bondOrder == 1)
            buf.append("S");
        else if(bondOrder == 2)
            buf.append("D");
        else if(bondOrder == 3)
            buf.append("T");
        if(cycleNumber > 0)
            buf.append(" Cycle: " + cycleNumber);
        if(endOfCycle)
            buf.append(" (connect)");
        if(bridgeToCycleAtom)
            buf.append(" (bridge");
        
        return buf.toString();
    }
}
