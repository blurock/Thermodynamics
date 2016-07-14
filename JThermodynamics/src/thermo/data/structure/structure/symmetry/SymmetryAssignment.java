/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry;

import java.util.Iterator;
import java.util.Vector;
import org.openscience.cdk.interfaces.IAtomContainer;

/** Collects the names of a set of symmetry assignments
 * 
 * A symmetry assignment is meant to be the list of structures which are the same. 
 * The determination of which structures are the same is determined elsewhere. Here
 * only the names of the structures are stored.
 * 
 * Equivalence is determined as equivalent name
 *
 * @author blurock
 */
public class SymmetryAssignment {

    private String groupName;
    private String symmetryConnection = "N";
    private Vector<String> assignmentsInMolecule;
    private Vector<String> assignmentsInSymmetryStructure;
    private IAtomContainer structure;

    /** Set up a new assignment set with a given name
     * 
     * @param name The name of the set
     */
    public SymmetryAssignment(String name, String symmconnect, IAtomContainer struct) {
        assignmentsInMolecule = new Vector<String>();
        structure = struct;
        groupName = name;
        symmetryConnection = symmconnect;
    }
    public boolean IsAtomInMolecule(String atm) {
        return assignmentsInMolecule.contains(atm);
    }
    /** Detects whether two assignments are the same
     * 
     * The criteria that they are the same is:
     * <ol>
     * <li> The have the same number of assignments
     * <li> All assignment names are found
     * </ol>
     * 
     * An implicit assumption is that the names of all assignments are all different.
     * 
     * @param assignment
     * @return
     */
    public boolean matches(SymmetryAssignment assignment) {

        boolean m = assignmentsInMolecule.size() == assignment.getAssignmentsInMolecule().size();
        if (m) {
            Iterator<String> i = assignment.getAssignmentsInMolecule().iterator();
            while (i.hasNext() && m) {
                String name = i.next();
                m = assigned(name);
            }
        }
        String ansS = "false   ";
        if(m)
            ansS = "true    ";
        //System.out.println(ansS + assignmentsInMolecule.toString() + "  <==>  " + assignment.getAssignmentsInMolecule().toString());
        return m;
    }

    /** Is an assignment already in the list?
     * 
     * @param n The name of the element to check
     * @return true is already in the list
     */
    public boolean assigned(String n) {
        return this.getAssignmentsInMolecule().contains(n);
    }

    /** Add a new (unique) element to the list of assignments
     * 
     * An element is only added if it is unique.
     * 
     * @param n The name of the assignment
     * @return true if the new element is not already in the list
     */
    public boolean addAssignment(String n) {
        boolean m = this.assigned(n);
        if (!m) {
            getAssignmentsInMolecule().add(n);
        }
        return m;
    }

    /** The name of this group of symmetry assignments
     * 
     * @return The name
     */
    public String getGroupName() {
        return groupName;
    }

    /** Set the name of this group of symmetry assignments
     * 
     * @param groupName The current name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 
     * @return
     */
    public Vector<String> getAssignmentsInMolecule() {
        return assignmentsInMolecule;
    }

    /**
     * 
     * @param assignmentsInMolecule
     */
    public void setAssignmentsInMolecule(Vector<String> assignmentsInMolecule) {
        this.assignmentsInMolecule = assignmentsInMolecule;
    }

    public String getSymmetryConnection() {
        return symmetryConnection;
    }

    public void setSymmetryConnection(String symmetryConnection) {
        this.symmetryConnection = symmetryConnection;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("SymmetryAssignment: ");
        buf.append(groupName);
        buf.append(" (" + symmetryConnection + ")");
        buf.append(" -> ");

        Iterator<String> inames = assignmentsInMolecule.iterator();
        while (inames.hasNext()) {
            String name = inames.next();
            buf.append(name);
            buf.append("\t");
        }
        buf.append("  (");
        if(assignmentsInSymmetryStructure != null) {
            Iterator<String> snames = assignmentsInSymmetryStructure.iterator();
            while (snames.hasNext()) {
                String name = snames.next();
                buf.append(name);
                buf.append("\t");
            }
        } else {
            buf.append("none");
        }
        buf.append(")");
        return buf.toString();
    }

    public IAtomContainer getStructure() {
        return structure;
    }

    public Vector<String> getAssignmentsInSymmetryStructure() {
        return assignmentsInSymmetryStructure;
    }

    public void setAssignmentsInSymmetryStructure(Vector<String> assignmentsInSymmetryStructure) {
        this.assignmentsInSymmetryStructure = assignmentsInSymmetryStructure;
    }
}
