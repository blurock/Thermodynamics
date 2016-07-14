/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author blurock
 */
public class SetOfSymmetryMatches extends ArrayList<SymmetryMatch>  {

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator<SymmetryMatch> i = this.iterator();
        while(i.hasNext()) {
            SymmetryMatch match = i.next();
            buf.append(match.toString());
        }
        return buf.toString();
    }
}
