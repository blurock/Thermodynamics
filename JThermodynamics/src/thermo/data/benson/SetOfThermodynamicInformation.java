/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author edwardblurock
 */
public class SetOfThermodynamicInformation extends ArrayList<ThermodynamicInformation> {
    String nameOfList;
    public SetOfThermodynamicInformation(String name) {
        nameOfList = name;
    }
    @Override
public String toString() {
     StringBuilder buf = new StringBuilder();
     buf.append("Set of ThermodynamicInformation structures: " + this.size());
     buf.append("\n");
        Iterator<ThermodynamicInformation> iterator = this.iterator();
        while(iterator.hasNext()) {
        ThermodynamicInformation info = iterator.next();
        buf.append(info.toString());
        buf.append("\n");
        }
     return buf.toString();
 }

}
