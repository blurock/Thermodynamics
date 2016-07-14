/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

/**
 *
 * @author edwardblurock
 */
public class DetermineInternalSymmetry extends DetermineTotalSymmetry {

    public DetermineInternalSymmetry(DetermineSymmetryFromSingleDefinition determine,
                                        SetOfSymmetryDefinitions definitions) {
        super(determine,definitions);
        determineSymmetry = determine;
    }
    public void initializeSymmetry() {
        symmetryValue = 1;
    }
    public void combineInSymmetryNumber(int symmetry) {
        symmetryValue *= symmetry;
    }
    public int getSymmetryValue() {
        return symmetryValue;
    }

}
