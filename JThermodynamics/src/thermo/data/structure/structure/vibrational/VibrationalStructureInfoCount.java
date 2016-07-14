/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational;

/**
 *
 * @author blurock
 */
public class VibrationalStructureInfoCount extends VibrationalStructureInfo {

    int countMatches = 0;
    /**
     * 
     * @param info
     * @param count 
     */
    public VibrationalStructureInfoCount(VibrationalStructureInfo info, int count) {
           super(info);
           countMatches = count;
    }
    public void subtract(VibrationalStructureInfoCount infocount) {
        /*
        double count = (double) infocount.countMatches;
        double symmcount = count / this.getSymmetry();
        int symmcountI = (int) symmcount;
        countMatches -= symmcountI;
         */
        countMatches -= infocount.countMatches;
    }
    public void negate() {
        //double count = (double) -countMatches;
        //double symmcount = count / this.getSymmetry();
        //int symmcountI = (int) symmcount;

        countMatches = -countMatches;
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(super.toString());
        buf.append(" Count=");
        buf.append(countMatches);
        return buf.toString();
    }
}
