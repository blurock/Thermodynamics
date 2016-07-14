/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

import java.util.ArrayList;
import java.util.Iterator;
import thermo.exception.ThermodynamicComputeException;

/**
 *
 * @author blurock
 */
public class SetOfBensonThermodynamicBase extends ArrayList<BensonThermodynamicBase> {
    
    public SetOfBensonThermodynamicBase() {
    }
    public SetOfBensonThermodynamicBase(ArrayList<BensonThermodynamicBase> lst) {
        super(lst);
    }
    
    public BensonThermodynamicBase combineToOneBensonRule(double[] temperatures) throws ThermodynamicComputeException {
        BensonThermodynamicBase ans = new BensonThermodynamicBase();
        ans.initialize("Total", "Total", temperatures);
        ans.setThermodynamicType("Standard");
        Iterator<BensonThermodynamicBase> i = this.iterator();
        while(i.hasNext()) {
            BensonThermodynamicBase benson = i.next();
            ans.addInto(benson);
        }
        return ans;
    }

    public void add(SetOfBensonThermodynamicBase set) {
        Iterator<BensonThermodynamicBase> iter = set.iterator();
        while(iter.hasNext()) {
            BensonThermodynamicBase base = iter.next();
            this.add(base);
        }
    }
    public void Minus() {
        Iterator<BensonThermodynamicBase> iter = this.iterator();
        while(iter.hasNext()) {
            BensonThermodynamicBase base = iter.next();
            base.Minus();
        }
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator<BensonThermodynamicBase> t = this.iterator();
        while(t.hasNext()) {
            BensonThermodynamicBase base = t.next();
            buf.append(base.toString());
            buf.append("\n");
        }
        return buf.toString();
    }
    public String printThermodynamicsAsHTMLTable() {
        StringBuffer buf = new StringBuffer();
        buf.append("<table>");
        buf.append("<thead>");
        buf.append("<tr>");
        buf.append("<th>Group</th>");
        buf.append("<th>Enthalpy (298K)</th>");
        buf.append("<th>Entropy (298K)</th>");
        buf.append("<th>Cp Value at Temperature</th>");
        buf.append("<th>Reference</th>");
        buf.append("</tr>");
        buf.append("</thead>");
        Iterator<BensonThermodynamicBase> b = this.iterator();
        while (b.hasNext()) {
                BensonThermodynamicBase thermo = b.next();
                buf.append(thermo.printThermodynamicsAsHTMLTable());
            }
        buf.append("</table>");
        buf.append("<br><br>");
        return buf.toString();
}
}
