/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson;

import java.util.HashSet;
import java.util.Iterator;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.interfaces.IAtomContainer;
import thermo.exception.ThermodynamicComputeException;

/**
 * The base class of the benson type thermodynamic data The principle components
 * are: <ol> <li> Standard Enthalpy <li> Standard Entropy <li> List of Heat
 * Capacities at the temperatures specified by the type <ol>
 *
 * @author blurock
 */
public class BensonThermodynamicBase extends ChemObject implements ThermodynamicInformation {

    private String reference;
    String thermodynamicType = "";
    HashSet<HeatCapacityTemperaturePair> setOfHeatCapacities;
    Double standardEnthalpy;
    private Double standardEntropy;
    IAtomContainer structure;
    double dHtoCalories = 1000.0;

    /**
     * The empty constructor
     */
    public BensonThermodynamicBase() {
    }

    /**
     * Initializing constructor
     *
     * @param type The type of thermodynamic values
     * @param setOfHeatCapacities The set of heat capacities
     * @param standardEnthalpy The standard enthalpy at 298
     * @param standardEntropy The standard entropy at 298
     */
    public BensonThermodynamicBase(String type, HashSet setOfHeatCapacities, Double standardEnthalpy, Double standardEntropy) {
        this.thermodynamicType = type;
        this.setOfHeatCapacities = setOfHeatCapacities;
        this.standardEnthalpy = standardEnthalpy;
        this.standardEntropy = standardEntropy;
    }

    public void initialize(String name, String ref, double[] temperatures) {
        reference = ref;
        setOfHeatCapacities = new HashSet<HeatCapacityTemperaturePair>();
        standardEnthalpy = new Double(0.0);
        standardEntropy = new Double(0.0);
        for (int i = 0; i < temperatures.length; i++) {
            HeatCapacityTemperaturePair pair = new HeatCapacityTemperaturePair(name, temperatures[i], 0.0);
            setOfHeatCapacities.add(pair);
        }
    }

    public void addHeatCapacity(String name, double cp, double temperature) {
        HeatCapacityTemperaturePair pair = new HeatCapacityTemperaturePair(name, cp, temperature);
        setOfHeatCapacities.add(pair);
    }

    public void Minus() {
        this.setStandardEnthalpy(-this.getStandardEnthalpy());
        this.setStandardEntropy(-this.getStandardEntropy());
        if (this.setOfHeatCapacities != null) {
            Iterator<HeatCapacityTemperaturePair> iter = this.setOfHeatCapacities.iterator();
            while (iter.hasNext()) {
                HeatCapacityTemperaturePair pair = iter.next();
                pair.setHeatCapacityValue(-pair.getHeatCapacityValue());
            }
        }
    }

    public void addInto(BensonThermodynamicBase toAdd) throws ThermodynamicComputeException {
        double[] temperatures = getTemperatures();
        BensonThermodynamicBase benson = toAdd.reformulateWithTemperatureSet(temperatures);
        this.standardEnthalpy += benson.standardEnthalpy;
        this.standardEntropy += benson.standardEntropy;
        Iterator<HeatCapacityTemperaturePair> pthis = this.setOfHeatCapacities.iterator();
        Iterator<HeatCapacityTemperaturePair> pnew = benson.setOfHeatCapacities.iterator();
        while (pthis.hasNext()) {
            HeatCapacityTemperaturePair pairthis = pthis.next();
            HeatCapacityTemperaturePair pairnew = pnew.next();
            pairthis.addInto(pairnew);
        }
    }

    /**
     *
     * @return The vector of heat capacities
     */
    public HashSet<HeatCapacityTemperaturePair> getSetOfHeatCapacities() {
        return setOfHeatCapacities;
    }

    /**
     *
     * @param setOfHeatCapacities Replace the set of heat capacities
     */
    public void setSetOfHeatCapacities(HashSet<HeatCapacityTemperaturePair> setOfHeatCapacities) {
        this.setOfHeatCapacities = setOfHeatCapacities;
    }

    /**
     * retrieve the standard enthalpy
     *
     * @return The standard Enthalpy
     */
    public Double getStandardEnthalpy() {
        return standardEnthalpy;
    }

    /**
     * set the standard enthalpy
     *
     * @param standardEnthalpy The standard enthalpy
     */
    public void setStandardEnthalpy(Double standardEnthalpy) {
        this.standardEnthalpy = standardEnthalpy;
    }

    /**
     *
     * @return the name of the thermodynamic type
     */
    @Override
    public String getThermodynamicType() {
        return thermodynamicType;
    }

    /**
     *
     * @param type The name of the thermodynamic type
     */
    @Override
    public void setThermodynamicType(String type) {
        thermodynamicType = type;
    }

    /**
     *
     * @return
     */
    @Override
    public double getStandardEnthalpy298() {
        return standardEnthalpy * dHtoCalories;
    }

    /**
     *
     * @return
     */
    @Override
    public double getStandardEntropy298() {
        return getStandardEntropy();
    }

    public double[] getSetOfHeatCapacityTemperatures() {
        double[] set = new double[setOfHeatCapacities.size()];
        Iterator iter = setOfHeatCapacities.iterator();
        int i = 0;
        while (iter.hasNext()) {
            HeatCapacityTemperaturePair pair = (HeatCapacityTemperaturePair) iter.next();
            set[i] = pair.getTemperatureValue();
        }
        return set;
    }

    public BensonThermodynamicBase reformulateWithTemperatureSet(double[] temperatures) throws ThermodynamicComputeException {
        BensonThermodynamicBase base = new BensonThermodynamicBase();

        base.setReference(reference);
        base.setStandardEnthalpy(standardEnthalpy);
        base.setStandardEntropy(standardEntropy);
        base.setThermodynamicType(thermodynamicType);
        String structureName = "Zero";
        HashSet<HeatCapacityTemperaturePair> tempset = new HashSet<HeatCapacityTemperaturePair>();
        if (setOfHeatCapacities == null) {
            for (int i = 0; i < temperatures.length; i++) {
                HeatCapacityTemperaturePair pair = new HeatCapacityTemperaturePair();
                pair.setHeatCapacityValue(0.0);
                pair.setReference(reference);
                pair.setStructureName("Zero");
                pair.setTemperatureValue(temperatures[i]);
                tempset.add(pair);
            }
        } else if (setOfHeatCapacities.size() > 0) {
            Iterator iter = setOfHeatCapacities.iterator();
            HeatCapacityTemperaturePair cpTpair = (HeatCapacityTemperaturePair) iter.next();
            structureName = cpTpair.getStructureName();
            for (int i = 0; i < temperatures.length; i++) {
                HeatCapacityTemperaturePair pair = new HeatCapacityTemperaturePair();
                pair.setHeatCapacityValue(this.getHeatCapacity(temperatures[i]));
                pair.setReference(reference);
                pair.setStructureName(structureName);
                pair.setTemperatureValue(temperatures[i]);
                tempset.add(pair);
            }
        } else {
            for (int i = 0; i < temperatures.length; i++) {
                //System.out.println("Temp: " + tempset.size() + " " + temperatures.length);
                HeatCapacityTemperaturePair pair = new HeatCapacityTemperaturePair();
                pair.setHeatCapacityValue(new Double(0.0));
                pair.setReference(reference);
                pair.setStructureName("Zero");
                pair.setTemperatureValue(temperatures[i]);
                tempset.add(pair);
            }

        }
        base.setSetOfHeatCapacities(tempset);
        return base;
    }

    /**
     * Compute the enthalpy from the standard enthalpy and the set of heat
     * capacities
     *
     * Using a linear extrapolation of the heat capacities between the values
     * given, the integral delta H = Hstd + integral( Cp(T)dt )
     *
     * Between each pair of heat capacities, cplower and cpupper, integral(
     * Cp(T)dt ) from lower T to upper T is (upperT -
     * lowerT)*(cplower+cpupper)/2
     *
     * The estimate is only valid above the temperature of the lowest heat
     * capacity. The heat capacity above that of the highest is assumed to be
     * constant.
     *
     * @param temperature
     * @return estimated enthalpy
     * @throws ThermodynamicComputeException If temperature is out of range.
     */
    @Override
    public double computeEnthalpy(double temperature) throws ThermodynamicComputeException {
        //String str = "computeEnthalpy: " + thermodynamicType + "\n";
        //System.out.println(str);
        //TO DO: fix the properties resource file so I can read this
        //ThermoInfoType type = ThermodynamicProperties.getTherodyInfoType(thermodynamicType);
        StandardThergasBensonThermoType type = new StandardThergasBensonThermoType();
        Double[] temps = type.getTemperatureArray();

        double enthalpy = standardEnthalpy * dHtoCalories;
        int index = type.getTemperatureLowerIndex(temperature);
        Iterator iter = setOfHeatCapacities.iterator();
        HeatCapacityTemperaturePair pair = (HeatCapacityTemperaturePair) iter.next();
        double cplower = pair.getHeatCapacityValue();
        double tlower = pair.getTemperatureValue();
        int i = 1;
        while (iter.hasNext() && i <= index) {
            HeatCapacityTemperaturePair upperpair = (HeatCapacityTemperaturePair) iter.next();
            double cpupper = upperpair.getHeatCapacityValue();
            double tupper = upperpair.getTemperatureValue();
            double factor = (tupper - tlower) * ((cpupper + cplower) / 2.0);
            enthalpy += factor;
            tlower = tupper;
            cplower = cpupper;
            i++;
        }
        double t = temperature - tlower;
        if (t < 0) {
            throw new ThermodynamicComputeException("Error in computing Enthalpy: Temperature out of range " + temperature + ">" + tlower);
        }
        double factor = 0.0;
        if (index + 1 >= setOfHeatCapacities.size()) {
            factor = cplower * t;
        } else {
        	HeatCapacityTemperaturePair[] arraySet = new HeatCapacityTemperaturePair[setOfHeatCapacities.size()];
        	int cnt = 0;
        	for(HeatCapacityTemperaturePair tpair : setOfHeatCapacities) {
        		arraySet[cnt] = tpair;
        	}
            //HeatCapacityTemperaturePair[] arraySet = (HeatCapacityTemperaturePair[]) setOfHeatCapacities.toArray();
            HeatCapacityTemperaturePair upperpair = arraySet[index];
            double cpupper = upperpair.getHeatCapacityValue();
            double tupper = upperpair.getTemperatureValue();
            factor = t * ((cplower + cpupper) / 2.0);
        }
        enthalpy += factor;
        return enthalpy / dHtoCalories;

    }

    /**
     *
     * @param temperature
     * @return
     */
    @Override
    public double computeEntropy(double temperature) throws ThermodynamicComputeException {
        //String str = "computeEnthalpy: " + thermodynamicType + "\n";
        //System.out.println(str);
        //TO DO: fix the properties resource file so I can read this
        //ThermoInfoType type = ThermodynamicProperties.getTherodyInfoType(thermodynamicType);
        double entropy = this.getStandardEntropy298();
        double cplower = this.getHeatCapacity(300.0);
        double tlower = 300.0;
        //StandardThergasBensonThermoType type = new StandardThergasBensonThermoType();
        for (double t = 310.0; t <= temperature; t += 10.0) {
            double cpupper = this.getHeatCapacity(t);
            double factor = (t - tlower) * ((cpupper + cplower) / 2.0);
            entropy += factor / t;
            tlower = t;
            cplower = cpupper;
        }
        double cpupper = this.getHeatCapacity(temperature);
        double factor = (temperature - tlower) * ((cpupper + cplower) / 2.0);
        entropy += factor / temperature;
        return entropy;
    }

    /**
     *
     * @param temperature
     * @return
     * @throws ThermodynamicComputeException
     */
    @Override
    public double getHeatCapacity(double temperature) throws ThermodynamicComputeException {
        ThermoInfoType type = new ThermoInfoType(this.getTemperatures());
        HeatCapacityTemperaturePair[] arraySet = new HeatCapacityTemperaturePair[setOfHeatCapacities.size()];
        int count=0;
        for(HeatCapacityTemperaturePair pair : setOfHeatCapacities) {
        	arraySet[count++] = pair;
        }
        
        //HeatCapacityTemperaturePair[] arraySet = (HeatCapacityTemperaturePair[]) setOfHeatCapacities.toArray();
        int i = type.getTemperatureLowerIndex(temperature);
        double heatCapacity = 0;
        if (i == -1) {
            HeatCapacityTemperaturePair pair = arraySet[0];
            heatCapacity = pair.getHeatCapacityValue();
        } else if (i + 1 >= arraySet.length) {
            HeatCapacityTemperaturePair pair = arraySet[i];
            heatCapacity = pair.getHeatCapacityValue();
        } else {
            HeatCapacityTemperaturePair pairlower = arraySet[i];
            HeatCapacityTemperaturePair pairupper = arraySet[i+1];
            double lowerCp = pairlower.getHeatCapacityValue();
            double upperCp = pairupper.getHeatCapacityValue();
            double lowerT = pairlower.getTemperatureValue();
            double upperT = pairupper.getTemperatureValue();
            double delta = (upperCp - lowerCp) / (upperT - lowerT);
            heatCapacity = lowerCp + delta * (temperature - lowerT);
        }
        return heatCapacity;
    }

    public void setStandardEntropy(Double standardEntropy) {
        this.standardEntropy = standardEntropy;
    }

    public Double getStandardEntropy() {
        return standardEntropy;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("BensonThermodynamicBase");
        if (this.getID() != null) {
            buf.append("(" + this.getID() + ")");
        }
        buf.append(":\tH298:");
        buf.append(this.getStandardEnthalpy().toString());
        buf.append("\tS298:");
        buf.append(this.standardEntropy.toString());
        buf.append("\tReference:'");
        buf.append(this.getReference());
        buf.append("'");
        if(this.getSetOfHeatCapacities() != null) {
        for(HeatCapacityTemperaturePair pair : this.getSetOfHeatCapacities()) {
        //Iterator<HeatCapacityTemperaturePair> iter = (Iterator<HeatCapacityTemperaturePair>) this.getSetOfHeatCapacities();
        //while(iter.hasNext()){
            //HeatCapacityTemperaturePair pair = iter.next();
                            buf.append("\t[");
                buf.append(pair.toString());
                buf.append("]");
        }
        } else {
        	buf.append("\tno heat capacities ");
        }
        return buf.toString();
    }

    public double[] getTemperatures() {
        Iterator<HeatCapacityTemperaturePair> t = setOfHeatCapacities.iterator();
        double[] temperatures = new double[setOfHeatCapacities.size()];
        for (int i = 0; i < temperatures.length; i++) {
            HeatCapacityTemperaturePair pair = t.next();
            temperatures[i] = pair.getTemperatureValue();
        }
        return temperatures;
    }

    public String printThermodynamicsAsHTMLTable() {
        StringBuilder buf = new StringBuilder();
        buf.append("<tr>");
        buf.append("<td>");
        if (this.getID() == null) {
            buf.append("Correction: ");
        } else {
            buf.append(this.getID());
        }
        buf.append("</td>");
        buf.append("<td>");
        buf.append(this.getStandardEnthalpy298());
        buf.append("</td>");
        buf.append("<td>");
        buf.append(this.getStandardEntropy298());
        buf.append("</td>");
        HashSet<HeatCapacityTemperaturePair> pairs = this.getSetOfHeatCapacities();
        if (pairs != null) {
            Iterator<HeatCapacityTemperaturePair> p = pairs.iterator();
            buf.append("<td>");
            while (p.hasNext()) {
                HeatCapacityTemperaturePair pair = p.next();
                buf.append(pair.getHeatCapacityValue());
                buf.append(" at ");
                buf.append(pair.getTemperatureValue());
                buf.append(" K ");

                buf.append("<br>");
            }
            buf.append("</td>");
        } else {
            buf.append("<td> ---  </td>");
        }
        buf.append("<td>");
        buf.append(this.getReference());
        buf.append("</td>");
        buf.append("</tr>");
        return buf.toString();

    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return this.getID();
    }

    /**
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        this.setID(name);
    }
}
