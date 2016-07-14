/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author blurock
 */
public class SetOfSymmetryDefinitions extends ArrayList<SymmetryDefinition> {

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator<SymmetryDefinition> iter = this.iterator();
        buf.append("----------------------------------------\n");
        buf.append("SetOfSymmetryDefinitions\n");
        while(iter.hasNext()) {
            SymmetryDefinition def = iter.next();
            buf.append(def.toString());
            buf.append("\n");
        }


        return buf.toString();
    }
}
