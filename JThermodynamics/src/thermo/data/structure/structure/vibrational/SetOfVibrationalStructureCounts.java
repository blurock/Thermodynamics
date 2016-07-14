/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author blurock
 */
public class SetOfVibrationalStructureCounts extends ArrayList<VibrationalStructureInfoCount> {
    
    public SetOfVibrationalStructureCounts() {
    }
    
    public void add(VibrationalStructureInfo info, int count) {
        VibrationalStructureInfoCount infocount = new VibrationalStructureInfoCount(info,count);
        add(infocount);
    }
    /**
     * 
     * @param counts
     */
    public void subtract(SetOfVibrationalStructureCounts counts) {
        SetOfVibrationalStructureCounts newcounts = new SetOfVibrationalStructureCounts();
        Iterator<VibrationalStructureInfoCount> iter = counts.iterator();
        while(iter.hasNext()) {
            VibrationalStructureInfoCount infocount = iter.next();
            if(!subtract(infocount)) {
                newcounts.add(infocount);
            }
        }
        iter = newcounts.iterator();
        while(iter.hasNext()) {
            VibrationalStructureInfoCount infocount = iter.next();
            infocount.negate();
            this.add(infocount);
        }
    }
    public boolean  subtract(VibrationalStructureInfoCount sub) {
        boolean found = false;
        Iterator<VibrationalStructureInfoCount> iter = this.iterator();
        while(iter.hasNext() & !found) {
            VibrationalStructureInfoCount infocount = iter.next();
            if(infocount.equals(sub)) {
                infocount.subtract(sub);
                found = true;
            }
        }
        return found;
    }
    public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator<VibrationalStructureInfoCount> iter = this.iterator();
        while(iter.hasNext()) {
            VibrationalStructureInfoCount infocount = iter.next();
            buf.append(infocount.toString());
            buf.append("\n");
        }
        return buf.toString();
    }

}
