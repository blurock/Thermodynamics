/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author blurock
 */
public class SetOfMetaAtoms {
    private HashSet<String> setOfMetaNames;
    
    public SetOfMetaAtoms() {
        setOfMetaNames = new HashSet<String>();
    }
    public boolean addIfNotInSet(String name) {
        boolean added = false;
        if(!setOfMetaNames.contains(name)) {
            setOfMetaNames.add(name);
            added = true;
        }
        return added;
    }

    public String[] getSetOfMetaNames() {
        return (String[]) setOfMetaNames.toArray();
    }
    public String writeAsString() {
        StringBuffer buf = new StringBuffer();
        Iterator<String> it = setOfMetaNames.iterator();
     
        for(int i=0;i<setOfMetaNames.size();i++) {
            if(i != 0) {
                if((i % 5) == 0) {
                    buf.append("\n");
                } else {
                    buf.append(",\t");
                }
            }
                
            buf.append(it.next());
        }
        
        return buf.toString();
    }
}
