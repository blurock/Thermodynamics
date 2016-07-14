/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational;


/** 
 *
 * @author blurock
 */
public class VibrationalStructureInfo {
    private String elementName;
    private String structureName;
    private Double frequency;
    private Double symmetry;
    
    /** The vibrational structure information
     * 
     */
    public VibrationalStructureInfo() {
     }
    /** Copy constructor
     * 
     * @param info
     */
    public VibrationalStructureInfo(VibrationalStructureInfo info) {
        elementName = info.getElementName();
        structureName = info.getStructureName();
        frequency = info.getFrequency();
        symmetry = info.getSymmetry();
    }
    /** The vibrational structure information
     * 
     * @param name The name of the element
     * @param struct The corresponding structure name
     * @param freq The frequency
     * @param symm The symmetry
     */
    public VibrationalStructureInfo(String name, String struct, double freq, double symm) {
         elementName = name;
         structureName = struct;
         frequency = freq;
         symmetry = symm;
     }
     double structureCorrection() {
         double correction = 1.0;
         return correction;
     }
     /** The element name of this set of information
      * 
      * @return
      */
     public String getElementName() {
        return elementName;
    }

     /**The element name of this set of information
      * 
      * @param elementName
      */
     public void setElementName(String elementName) {
        this.elementName = elementName;
    }

     /** The corresponding structure name
      * 
      * @return
      */
     public String getStructureName() {
        return structureName;
    }

    /** The corresponding structure name
     * 
     * @param structureName
     */
    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    /** The vibrational frequency of the structure
     * 
     * @return
     */
    public Double getFrequency() {
        return frequency;
    }

    /** The vibrational frequency of the structure
     * 
     * @param frequency
     */
    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    /** The symmetry of the structure
     * 
     * @return
     */
    public Double getSymmetry() {
        return symmetry;
    }

    /** The symmetry of the structure
     * 
     * @param symmetry
     */
    public void setSymmetry(Double symmetry) {
        this.symmetry = symmetry;
    }
    /** The structure as a string
     * 
     * @return
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Vibrational: ");
        buf.append(elementName);
        buf.append("(");
        buf.append(structureName);
        buf.append(", ");
        buf.append(frequency);
        buf.append(", ");
        buf.append(symmetry);
        buf.append(")");
        return buf.toString();
    }
    @Override
    public boolean equals(Object obj) {
        boolean ans = false;
        VibrationalStructureInfo info = (VibrationalStructureInfo) obj;
        ans = info.elementName.equals(this.elementName)
                && info.structureName.equals(this.structureName)
                && info.frequency.equals(this.frequency)
                && info.symmetry.equals(this.symmetry);
        return ans;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.elementName != null ? this.elementName.hashCode() : 0);
        hash = 59 * hash + (this.structureName != null ? this.structureName.hashCode() : 0);
        hash = 59 * hash + (this.frequency != null ? this.frequency.hashCode() : 0);
        hash = 59 * hash + (this.symmetry != null ? this.symmetry.hashCode() : 0);
        return hash;
    }

}
