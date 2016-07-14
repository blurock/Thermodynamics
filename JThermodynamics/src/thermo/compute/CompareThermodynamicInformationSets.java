/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compute;

import java.util.Iterator;
import thermo.compare.ThermodynamicDifference;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfThermodynamicInformation;
import thermo.data.benson.StandardThergasBensonThermoType;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;

/**
 *
 * @author edwardblurock
 */
public class CompareThermodynamicInformationSets {
    StandardThergasBensonThermoType standardtemps;
    private double verySmall = 1e-10;
    private double veryClose = 0.0001;

    public CompareThermodynamicInformationSets() {
        standardtemps = new StandardThergasBensonThermoType();
    }
    public SetOfThermodynamicDifferences computeDifference(SetOfThermodynamicInformation set1, SetOfThermodynamicInformation set2) throws ThermodynamicComputeException {
        SetOfThermodynamicDifferences difference = new SetOfThermodynamicDifferences("Difference");
        Iterator<ThermodynamicInformation> iter1 = set1.iterator();
        Iterator<ThermodynamicInformation> iter2 = set2.iterator();
        double[] temperatures = standardtemps.getTemperaturesAsDoubleValues();
        while(iter1.hasNext()) {
            ThermodynamicInformation thermo1 = iter1.next();
            ThermodynamicInformation thermo2 = iter2.next();

            double enthalpy1 = thermo1.getStandardEnthalpy298();
            double enthalpy2 = thermo2.getStandardEnthalpy298();
            ThermodynamicDifference benson = new ThermodynamicDifference(veryClose);
            benson.initialize(thermo1.getName(), benson.getReference(),temperatures);
            benson.setName(thermo1.getName());
            System.out.println(benson.getName() + ":  \t" + enthalpy1 + ",\t" + enthalpy2);
            double enthalpypercent = calculatePercent(enthalpy1, enthalpy2);
            double entropypercent = calculatePercent(thermo1.getStandardEntropy298(),thermo2.getStandardEntropy298());
            benson.setStandardEnthalpy(enthalpypercent);
            benson.setStandardEntropy(entropypercent);

            String name = null;
            
            for(int i=0;i<temperatures.length;i++) {
                double t = temperatures[i];
                double cppercent = calculatePercent(thermo1.getHeatCapacity(t), thermo2.getHeatCapacity(t));
                benson.addHeatCapacity(name, cppercent, t);
            }
            difference.add(benson);
        }
        return difference;
    }

    private double calculatePercent(double x1, double x2) {
        double percent = 0.0;
        double sum = Math.abs(x1) + Math.abs(x2);


        if(sum > veryClose) {
            percent = (x2- x1)/sum;
        }
        //System.out.println("calculatePercent: " + x1abs + "\t"+ x2abs + "\t"+ sum + "\t"+ veryClose + "\t"+ percent );

        return percent;
    }


}
