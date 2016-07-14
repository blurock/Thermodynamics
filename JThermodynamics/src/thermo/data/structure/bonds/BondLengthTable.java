/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.bonds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/** Bond Length Table
 *   This is made based on Table A.14 of Benson Thermodynamics.
 *
 * @author edwardblurock
 */
public class BondLengthTable extends ArrayList<BondLength> {

    public BondLengthTable() {
    }
    public BondLength findInTable(String a1, String a2, int order) {
        BondLength comp = new BondLength(a1,a2,order);
        return findInTable(comp);
    }
    public BondLength findInTable(BondLength comp) {
        int pos = this.indexOf(comp);
        BondLength ans = this.get(pos);
        return ans;
    }
    public void readFromString(String table, String source) {
        StringTokenizer tok = new StringTokenizer(table,"\n");
        while(tok.hasMoreTokens()) {
            String line = tok.nextToken();
            BondLength bond = new BondLength();
            bond.readLine(line,source);
            this.add(bond);
        }
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Table of Bond Lengths");
        Iterator<BondLength> iter = this.iterator();
        while(iter.hasNext()){
            BondLength bond = iter.next();
            buf.append(bond.toString());
            buf.append("\n");
        }
        return buf.toString();
    }

}
