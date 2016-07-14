/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author blurock
 */
public class SetOfBensonGroupStructures extends ArrayList<BensonGroupStructure> {
    /**
     * 
     */
    public SetOfBensonGroupStructures() {
    }
    public SetOfBensonGroupStructures(ArrayList<BensonGroupStructure> lst) {
        super(lst);
    }
    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        buf.append("Set of Benson Group Structures\n");
        Iterator<BensonGroupStructure> i = this.iterator();
        while (i.hasNext()) {
            BensonGroupStructure struct = i.next();
            buf.append(struct.toString());
            buf.append("\n");
        }
        
        
        return buf.toString();
    }

}
