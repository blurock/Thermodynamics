/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational;

import thermo.properties.ChemicalConstants;

/**
 *
 * @author blurock
 */
public class FrequencyCorrection {
    double Rconstant;
    public FrequencyCorrection() {
        Rconstant = ChemicalConstants.getGasConstantInCalsMolesK();
    }
    public double correctCpInCalories(double frequency, double temperature) {
        double x = 1.44*frequency/temperature;
        
        double expx = Math.exp(x);
        double value = (expx*x*x)/(Math.pow((expx -1),2.0)) * Rconstant;
        
        return value;
    }
    public double correctEntropyInCalories(double frequency, double temperature) {
        double x = 1.44*frequency/temperature;
        
        double expx = Math.exp(x);
        double expmx = Math.exp(-x);
        double x1 = x/(expx-1.0);
        double x2 = 1.0 - Math.log(1.0-expmx);
        
        double value = Rconstant*x1*x2;
        
        return value;
    }
}
