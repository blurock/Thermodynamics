/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import thermo.data.structure.structure.symmetry.utilities.DetermineSetOfSymmetryAssignments;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.symmetry.utilities.DetermineSymmetryAssignmentsFromConnections;

/** DetermineSymmetryFromSingleDefinition
 *
 * @author blurock
 */
public class DetermineSymmetryFromSingleDefinition {
    SymmetryDefinition symmetryDefinition;
    DetermineSetOfSymmetryAssignments determineSymmetryAssignments;
    AtomContainer structure;
    SetOfSymmetryMatches symmetryMatches;
    DetermineSymmetryAssignmentsFromConnections matchAssignments;

    public SetOfSymmetryMatches getSymmetryMatches() {
        return symmetryMatches;
    }
    
    public DetermineSymmetryFromSingleDefinition() {
        matchAssignments = new DetermineSymmetryAssignmentsFromConnections();
    }
    
    public int determineSymmetry(SymmetryDefinition symmetry, AtomContainer struct)  throws CDKException {
        structure = struct;
        symmetryDefinition = symmetry;
        determineSymmetryAssignments = new DetermineSetOfSymmetryAssignments(symmetryDefinition,matchAssignments);
        symmetryDefinition = symmetry;
        determineSetOfSymmetryAssignments(structure);
        return symmetryMatches.size();
    }
    public double computeSymmetryContribution(int symmetry) {
        double symmD = (double) symmetry;
        return symmD * symmetryDefinition.getInternalSymmetryFactor();
    }
    public void determineSetOfSymmetryAssignments(AtomContainer struct) throws CDKException {
        structure = struct;
        symmetryMatches = determineSymmetryAssignments.findIfMatchInStructures(struct);
    }

}
