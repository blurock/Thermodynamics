/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author blurock
 */
public class SymmetryMatch {
    Hashtable<String,String> matchedSymmetries;
    private SetOfSymmetryAssignments fromMolecule;
    /**
     * 
     * @param symmatch
     * @param frommol 
     */
    public SymmetryMatch(SetOfSymmetryAssignments frommol) {
        fromMolecule = frommol;
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Symmetry Match;\n");
        buf.append(getFromMolecule().toString());
        /*
        Set<String> names = matchedSymmetries.keySet();
        Iterator<String> i = names.iterator();
        buf.append("Correspondences: ");
        while(i.hasNext()) {
            String key = i.next();
            String match = matchedSymmetries.get(key);
            buf.append("[" + key + "," + match + "]\t");
        }
        buf.append("\n");
        */
        
        return buf.toString();
    }

    public SetOfSymmetryAssignments getFromMolecule() {
        return fromMolecule;
    }
}
