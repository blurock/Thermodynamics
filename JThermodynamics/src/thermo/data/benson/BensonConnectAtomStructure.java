/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

/** This is the information of the connected atom in {@link BensonGroupStructure}
 *
 * This is structure representing one of the connections to the center
 * atom in {@link BensonGroupStructure}.
 *
 * The connection has two pieces of information:
 * <ul>
 * <li> The name of the connected atom group
 * <li> The multiplicity
 * </ul>
 *
 * @author blurock
 */
public class BensonConnectAtomStructure {
    private String connectedAtomS;
    private int multiplicity;

    /** empty constructor
     *
     */
    public BensonConnectAtomStructure() {
    }
    /** Constructor creating a full structure
     *
     * @param atomS The name of the connection atom
     * @param m The multiplicity of the connection
     */
    public BensonConnectAtomStructure(String atomS, int m) {
        connectedAtomS = atomS;
        multiplicity = m;
    }
    /** The copy constructor
     *
     * @param structure the structure
     */
    public BensonConnectAtomStructure(BensonConnectAtomStructure structure) {
        connectedAtomS = structure.connectedAtomS;
        multiplicity = structure.multiplicity;
    }
    /** The connected atom string
     *
     * @return connected atom string
     */
    public String getConnectedAtomS() {
        return connectedAtomS.toLowerCase();
    }

    /**The connected atom string
     *
     * @param connectedAtomS connected atom string
     */
    public void setConnectedAtomS(String connectedAtomS) {
        this.connectedAtomS = connectedAtomS;
    }

    /** The number of times this atom is connected
     *
     * @return The number of times this atom is connected
     */
    public int getMultiplicity() {
        return multiplicity;
    }

    /** The number of times this atom is connected
     *
     * @param multiplicity The number of times this atom is connected
     */
    public void setMultiplicity(int multiplicity) {
        this.multiplicity = multiplicity;
    }
    /** Write as string in compact form
     *
     * @return the string version
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\t[");
        buf.append(connectedAtomS);
        buf.append(",");
        buf.append(multiplicity);
        buf.append("]");
        return buf.toString();
    }
    
}
