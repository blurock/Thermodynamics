/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compare;

import java.util.Iterator;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.HeatCapacityTemperaturePair;
import thermo.data.benson.ThermodynamicInformation;

/**
 *
 * @author edwardblurock
 */
public class ThermodynamicDifference extends BensonThermodynamicBase implements Comparable<BensonThermodynamicBase> {
    double veryClose;

    public ThermodynamicDifference(double veryClose) {
        this.veryClose = veryClose;
    }

    public int compareTo(BensonThermodynamicBase t) {
        int ans = 0;
        double diff = t.getStandardEnthalpy298() - this.getStandardEnthalpy298();
        //System.out.println("compareTo: " + this.getName() + " - " + t.getName() + " = " + diff);
        if(Math.abs(diff) > veryClose) {
            ans = 1;
            if(Math.abs(t.getStandardEnthalpy298()) < Math.abs(this.getStandardEnthalpy298())) ans = -1;
            //ans = (int) Math.floor(diff);
        } else {
            diff = t.getStandardEntropy298() + this.getStandardEntropy298();
            if(Math.abs(diff) > veryClose) {
                ans = 1;
                if(Math.abs(t.getStandardEntropy298()) < Math.abs(this.getStandardEntropy298())) ans = -1;
                //ans = (int) Math.floor(diff);
            } else {
                Iterator<HeatCapacityTemperaturePair> i1 = this.getSetOfHeatCapacities().iterator();
                Iterator<HeatCapacityTemperaturePair> i2 = t.getSetOfHeatCapacities().iterator();
                double maxdiff = 0.0;
                while(i1.hasNext()) {
                    double cp1 = i1.next().getHeatCapacityValue();
                    double cp2 = i2.next().getHeatCapacityValue();
                    diff = cp2-cp1;
                    if(Math.abs(diff) > Math.abs(maxdiff)) maxdiff = diff;
                }
                ans = 1;
                if(maxdiff < 0) ans = -1;
                //ans = (int) Math.floor(maxdiff);
            }
        }
        //System.out.println("compareTo: " + ans + this.getName() + " - " + t.getName() + " = " + diff);
        return ans;
    }

    public String toStringInTable() {
        StringBuilder buf = new StringBuilder();

        buf.append(getName());
        buf.append("\t");
        buf.append(this.getStandardEnthalpy298());
        buf.append("\t");
        buf.append(this.getStandardEntropy298());

        Iterator<HeatCapacityTemperaturePair> iter = this.getSetOfHeatCapacities().iterator();
        while(iter.hasNext()) {
            buf.append("\t");
            HeatCapacityTemperaturePair pair = iter.next();
            buf.append(pair.getHeatCapacityValue());
        }
        return buf.toString();
    }
}
