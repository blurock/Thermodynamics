/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry;

/** The pairing of a structure with a particular group
 * 
 * A set of these pairings make up the symmetry assignments
 *
 * @author blurock
 */
public class SymmetryPair {

    private String groupName;
    private String structureName;
    private String connectedSymmetry = "N";

    /**
     * 
     * @param group The name of the group assignments
     * @param structure The name of the structure in the group
     */
    public SymmetryPair(String group, String structure) {
        groupName = group;
        structureName = structure;
    }
    public SymmetryPair(String group, String structure,String connectedSym) {
        groupName = group;
        structureName = structure;
        connectedSymmetry = connectedSym;
    }

    /**
     * 
     * @return
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 
     * @param groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 
     * @return
     */
    public String getStructureName() {
        return structureName;
    }

    /**
     * 
     * @param atomName
     */
    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }
    public String toString() {
        return "[" + groupName + "," + structureName + "]";
    }

    public String getConnectedSymmetry() {
        return connectedSymmetry;
    }

    public void setConnectedSymmetry(String connectedSymmetry) {
        this.connectedSymmetry = connectedSymmetry;
    }

}
