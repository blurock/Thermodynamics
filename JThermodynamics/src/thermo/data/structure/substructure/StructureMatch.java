/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.substructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.isomorphism.mcss.RMap;

/**
 *
 * @author edwardblurock
 */
public class StructureMatch extends ArrayList<RMap> implements Comparable<StructureMatch> {
    String nameOfStructure;

    public StructureMatch(String nameOfStructure, List<RMap> map) {
        super(map);
        this.nameOfStructure = nameOfStructure;
    }

    public String getNameOfStructure() {
        return nameOfStructure;
    }

    public void setNameOfStructure(String nameOfStructure) {
        this.nameOfStructure = nameOfStructure;
    }
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(nameOfStructure);
        buf.append("\t");
        Iterator<RMap> iter = this.iterator();
        while(iter.hasNext()) {
            RMap map = iter.next();
            buf.append("(");
            buf.append(map.getId1());
            buf.append(",");
            buf.append(map.getId2());
            buf.append(")\t");
        }
        return buf.toString();
    }

    public int compareTo(StructureMatch t) {
        int comp = 0;
        if(this.size() > t.size()) {
            comp = -1;
        } else if(this.size() < t.size()) {
            comp = 1;
        }
        return comp;
    }


}
