/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.substructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.isomorphism.mcss.RMap;

/**
 *
 * @author edwardblurock
 */
public class ListOfStructureMatches extends ArrayList<StructureMatch> {

    public ListOfStructureMatches() {
    }

    public void append(String name, List<List<RMap>> map) {
       Iterator<List<RMap>> mapiter = map.iterator();
            while(mapiter.hasNext()) {
                List<RMap> mapmatch = mapiter.next();
                StructureMatch match = new StructureMatch(name,mapmatch);
                this.add(match);
            }
    }

    public void sort(){
        Collections.sort(this);
    }
    public String[] reduceToListOfNames() {
        ArrayList<String> nonoverlap = new ArrayList<String>();
        Iterator<StructureMatch> iter = this.iterator();
        while(iter.hasNext()) {
            StructureMatch match = iter.next();
            nonoverlap.add(match.getNameOfStructure());
        }

        return (String[]) nonoverlap.toArray();
    }
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        Iterator<StructureMatch> iter = this.iterator();
        while(iter.hasNext()) {
            StructureMatch match = iter.next();
            buf.append(match.toString());
            buf.append("\n");
        }
        return buf.toString();
    }
}
