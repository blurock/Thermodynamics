/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry.utilities;

import thermo.data.structure.structure.symmetry.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import thermo.data.structure.structure.matching.GetSubstructureMatches;

/**
 *
 * @author blurock
 */
public class DetermineSymmetryAssignmentsFromConnections {

    protected SetOfSymmetryAssignments assignments;
    GetSubstructureMatches matches = new GetSubstructureMatches();

    /**
     * 
     */
    public DetermineSymmetryAssignmentsFromConnections() {
    }

    /** This determines the 'symmetry' of the connected structures
     *
     * All connected structures are grouped by whether they are equal or not.
     * For each set of equal connected structures, there is a
     * {@link SymmetryAssignment} structure which are in the
     * {@link SetOfSymmetryAssignments} structure.
     * The {@link SymmetryAssignment} is referenced by the name of the
     * connecting atom. The 'connectionSymmetry' defaults to 'N'.
     *
     * The {@link SetOfSymmetryAssignments} is put in the 'assignments' field
     *
     * @param connections The set of connected structures, referenced by the connected atom name
     * @throws org.openscience.cdk.exception.CDKException
     */
    public void determineSymmetryAssignments(Hashtable<String, IAtomContainer> connections) throws CDKException {
        assignments = new SetOfSymmetryAssignments();
        Set<String> keys = connections.keySet();
        Iterator<String> ikeys = keys.iterator();
        int count = 0;
        // Loop through all the connected structures
        while (ikeys.hasNext()) {
            String connectionname = ikeys.next();
            IAtomContainer structure = connections.get(connectionname);

            // Loop through all the symmetry structures identified
            boolean unassigned = true;
            Set<String> assignkeys = getAssignments().keySet();
            Iterator<String> iassigned = assignkeys.iterator();
            while (iassigned.hasNext() && unassigned) {
                String key = iassigned.next();
                SymmetryAssignment symmetry = getAssignments().get(key);
                // From the given symmetry, get the first assigned connected molecule
                Vector<String> assignednames = symmetry.getAssignmentsInMolecule();
                Iterator<String> inames = assignednames.iterator();
                if (inames.hasNext()) {
                    String name = inames.next();
                    IAtomContainer mol = connections.get(name);
                    // Check if molecule is the same as the connected structure
                    if (matches.equals(structure, mol)) {
                        // if yes, then assign this structure to
                        symmetry.addAssignment(connectionname);
                        unassigned = false;
                    }
                }
            }
            if (unassigned) {
                String connectSym = "N";
                SymmetryAssignment symmetry = new SymmetryAssignment(connectionname, connectSym, structure);
                symmetry.addAssignment(connectionname);
                assignments.put(connectionname, symmetry);
            }
        }
    }

    /**
     * 
     * @return
     */
    public SetOfSymmetryAssignments getAssignments() {
        return assignments;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Set of Symmetry Assignments:\n");
        Set<String> keys = assignments.keySet();

        Iterator<String> ikeys = keys.iterator();
        while (ikeys.hasNext()) {
            String key = ikeys.next();
            SymmetryAssignment sym = assignments.get(key);
            buf.append(sym.toString());
            buf.append("\n");
        }

        return buf.toString();
    }
}
