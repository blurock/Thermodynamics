package jThergas.data.group;

/** This is the base information of the center (main) atom/group of the
 * Benson rule.
 *
 * @author blurock
 * @version 2008.10
 */
public class JThergasCenterAtom {
    private String centerAtomS;

    JThergasCenterAtom(String atomS) {
        centerAtomS = atomS;
    }
    /** Retrieve the center atom of the Benson rule
     * 
     * @return The center atom as a string
     */
    public String getCenterAtomAsString() {
        return centerAtomS;
    }

    /** set the center atom string
     * 
     * @param centerAtom The string form of the center atom
     */
    public void setCenterAtomString(String centerAtom) {
        this.centerAtomS = centerAtom;
    }
    
}
