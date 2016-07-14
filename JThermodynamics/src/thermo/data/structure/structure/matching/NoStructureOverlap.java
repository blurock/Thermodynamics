/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.matching;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author blurock
 */
public class NoStructureOverlap {
    /**
     * 
     */
    public NoStructureOverlap() {
        
    }
    
    /**
     * 
     * @param connections
     * @return
     */
    public boolean noOverlapInStructures(Hashtable<String, IAtomContainer> connections) {
        boolean ans = true;
        Set<String> keys = connections.keySet();
        Iterator<String> i = keys.iterator();
        int count = 0;
        ArrayList<IAtom> atomlist = new ArrayList<IAtom>();
        while(i.hasNext() && ans) {
            String key = i.next();
            IAtomContainer sub = connections.get(key);
            Iterator<IAtom> j = sub.atoms().iterator();
            while(j.hasNext()) {
                IAtom atm = j.next();
                if(!atomlist.contains(atm)) {
                    atomlist.add(atm);
                } else {
                    ans = false;
                }
            }
        }
        return ans;
    }
    
}
