/*
 */

package thermo.data.structure.structure;

/** The keywork information making up a meta atom
 * <ul>
 * <li> elementName The full name of the meta atom
 * <li> metaAtomType The type of meta atom this is
 * <li> metaAtomname The name of the meta atom as found in the molecule
 * </ul>
 *
 * @author blurock
 */
public class MetaAtomInfo {
    // The full name of the meta atom
    private String elementName = null;
    // The type of meta atom this is
    private String metaAtomType = "MetaAtom";
    // The name of the meta atom as found in the molecule
    private String metaAtomName = null;

    /** the empty constructor
     *
     */
    public MetaAtomInfo() {
        
    }
    /** The copy constructor
     *
     * @param info The class to be copied
     */
    public MetaAtomInfo(MetaAtomInfo info) {
        elementName = info.elementName;
        metaAtomType = info.metaAtomType;
        metaAtomName = info.metaAtomName;
    }
    /** Get the element name
     * The name of the meta atom structure
     *
     * @return element name
     */
    public String getElementName() {
        return elementName;
    }

    /** Set the element name
     * The name of the meta atom structure
     * 
     * @param structureName element name
     */
    public void setElementName(String structureName) {
        this.elementName = structureName;
    }

    /** Get meta atom type
     *
     *
     * @return the meta atom type
     */
    public String getMetaAtomType() {
        return metaAtomType;
    }

    /** Set meta atom type
     *
     * @param metaAtomType type
     */
    public void setMetaAtomType(String metaAtomType) {
        this.metaAtomType = metaAtomType;
    }

    /** Meta atom name
     * @return The name of the atom in the molecule
     */
    public String getMetaAtomName() {
        return metaAtomName;
    }

    /** Meta atom name
     * The name of the atom in the molecule
     * @param metaAtomName The name of the atom in the molecule
     */
    public void setMetaAtomName(String metaAtomName) {
        this.metaAtomName = metaAtomName;
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("MetaAtomInfo: ");
        buf.append("Name: " + metaAtomName + "\t");
        buf.append("Type: " + metaAtomType + "\t");
        buf.append("Structure: " + elementName + "\t");
      
        return buf.toString();
    }
}
