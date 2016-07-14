/*
 */

package thermo.data.structure.linearform;

import java.util.Iterator;
import java.util.Vector;

/** This is a single 'node' of the linear form representation.
 * The linear representation is translated initially to a tree of nodes.
 * Each node represents an atom and is connected to further nodes representing further atoms.
 * This class is essentually a single link in a linked list.
 * The next link is specified by mainConnection.
 * The bonding information (stored in atomElement) is how the atom
 * represented by this link is connected to the next atom represented
 * as the next link in the linked list.
 *
 *
 * The node information is:
 * <ul>
 * <li> connections: The children of the atom node, meaning
 * the atoms (nodes) it is connected to.
 * <li> atomElement: The basic properties of this atom, including bonding to
 * link
 * <li> mainConnection: the link to the next atom
 * </ul>
 *
 * @author blurock
 */
public class AtomGroupStringNode {
    // The basic properties of this atom
    public LinearFormAtomGroupString atomElement;
    // The children of the atom node, meaning the atoms (nodes) it is connected to.
    Vector<AtomGroupStringNode> connections;
    Vector<Integer> bondorders;
    /** The main connection is the next atom this is connected to.
     * The bonding information of this node refers to how this atom
     * and the mainConnection atom are connected.
     */
    AtomGroupStringNode mainConnection = null;
    
    /** Constructor with name of the atom/meta atom
     *
     * @param atm The name of the atom or meta atom.
     *
     * This constructor sets up a single atom (LinearFormAtomGroupString atomElement) with defaults
     * and the name of the atom.
     * The connections are initialized to an empty list and the mainConnection is null
     */
    public AtomGroupStringNode(String atm) {
        atomElement = new LinearFormAtomGroupString();
        atomElement.atomGroup = atm;
        connections = new Vector<AtomGroupStringNode>();
        bondorders = new Vector<Integer>();
    }
    /** Add a connection to the connection list
     *
     * @param atmnode The node to add as connection
     */
    public void addConnection(AtomGroupStringNode atmnode) {
        addConnection(atmnode,1);
    }
    /** Add a connection to the connection list
     *
     * @param atmnode The node to add as connection
     */
    public void addConnection(AtomGroupStringNode atmnode, int bondorder) {
        connections.add(atmnode);
        Integer bo = new Integer(bondorder);
        bondorders.add(bo);
    }
    /** This connects an atom to be the next in the linked list.
     *
     * @param atmnode The atom as next in the linked list of atoms
     */
    public void addMainConnection(AtomGroupStringNode atmnode) {
        //System.out.println("To connect bond order: " + atmnode.atomElement.bondOrder);
        //System.out.println("This bond order      : " + this.atomElement.bondOrder);
        atmnode.atomElement.bondOrder = this.atomElement.bondOrderForward;
        mainConnection = atmnode;
    }
    public String toString() {
        return toString("");
    }
    /** The string form of the meta atom.
     * The output is multiple lines.
     * The output looks like a tree, with the children indented
     *
     * @param prefix a prefix to be put in front of each line
     * @return The multi-line string representation
     */
    public String toString(String prefix) {
        StringBuffer buf = new StringBuffer();
        buf.append(prefix);
        buf.append(atomElement.toString());
        buf.append("\n");
        String newprefix = prefix + "---->";
        if(mainConnection != null) {
        
        buf.append(mainConnection.toString(newprefix));
        }
        Iterator<AtomGroupStringNode> ivec = connections.iterator();
        Iterator<Integer> ibo = bondorders.iterator();
        while(ivec.hasNext()) {
            AtomGroupStringNode node = ivec.next();
            Integer bo = ibo.next();
            buf.append("bond: " + bo.intValue() + " : " + node.toString(newprefix));
        }
        return buf.toString();
    }
}
