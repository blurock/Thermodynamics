/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.substructure;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author edwardblurock
 */
public class SetOfSubStructures extends ArrayList<SubStructure> {

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Set of SubStructures");
        Iterator<SubStructure> iter = this.iterator();
        while(iter.hasNext()) {
            SubStructure structure = iter.next();
            buf.append(structure.toString());
            buf.append("\n");
        }
        return buf.toString();
    }

}
