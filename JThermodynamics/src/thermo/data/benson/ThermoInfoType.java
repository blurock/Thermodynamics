/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson;

import java.util.HashSet;
import java.util.Iterator;

/** This base class (others will be derived from this) holds the meta
 * information about the benson information.
 * 
 * The information at this level is:
 * <ol> 
 * <li> Temperatures of the heat capacities
 * </ol>
 * @author blurock
 */
public class ThermoInfoType {
    private HashSet<Double> Temperatures = null;
    private Double[] TemperatureArray;
    private String typeName;

    /**
     * The Empty Constructor
     */
    public ThermoInfoType() {
        Temperatures = new HashSet<Double>();
        TemperatureArray = new Double[0];
    }

    /**
     * The Standard  Constructor
     * @param name The name of the ThermoInfoType
     */
    public ThermoInfoType(String name) {
        typeName = name;
        Temperatures = new HashSet<Double>();
        TemperatureArray = new Double[0];
    }

    ThermoInfoType(double[] temperatures) {
        Temperatures = new HashSet<Double>();
        TemperatureArray = new Double[temperatures.length];
        typeName = "FromTemperatures";
       for(int i=0;i<temperatures.length;i++) {
           this.add(temperatures[i]);
           TemperatureArray[i] = temperatures[i];
       }
    }

    /**
     * 
     * @return Get the set of temperatures
     */
    public HashSet<Double> getTemperatures() {
        return Temperatures;
    }
    /**
     * 
     * @return Get the set of temperatures
     */
    public Double[] getTemperatureArray() {
        return TemperatureArray;
    }

    /**
     * 
     * @param Temperatures
     */
    public void setTemperatures(HashSet<Double> T) {
        this.Temperatures = T;
        Iterator<Double> iter = Temperatures.iterator();
        TemperatureArray = new Double[Temperatures.size()];
        int i = 0;
        while(iter.hasNext()) {
            TemperatureArray[i++] = iter.next();
        }
    }

    /**
     * 
     * @return The name of the Thermo type
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 
     * @param typeName Set the name of the thermo type
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 
     * @param temperature The temperature to add to the temperature list
     * @return
     */
    public synchronized boolean add(Double temperature) {
        boolean ans = Temperatures.add(temperature);
        if(ans) {
            TemperatureArray = new Double[Temperatures.size()];
            int i=0;
            Iterator<Double> iter = Temperatures.iterator();
            while(iter.hasNext()) {
                TemperatureArray[i++] = iter.next();
            }
        }
        return ans;
    }

    /**
     * 
     * @param temperature Ther temperature to add to the temperature list
     * @return
     */
    public synchronized boolean add(double temperature) {
        Double tD = new Double(temperature);
        return this.add(tD);
    }

    /**
     * 
     * @param i
     * @return Temperature
     */
    public double getTemperature(int i) {
        //Double[] temps = (Double[]) Temperatures.toArray();
        return TemperatureArray[i].doubleValue();
    }

    /** The lower bound of the set of temperatures
     * 
     * @param t The input temperature
     * @return Within the set temperatures, the highest temperature lower than t
     */
    public int getTemperatureLowerIndex(double t) {
        int lower = -1;
        int i = TemperatureArray.length - 1;
        while (lower == -1 && i >= 0) {
        	if(TemperatureArray[i] != null) {
            if (TemperatureArray[i] <= t) {
                lower = i;
                i--;
            } else {
                i--;
            }
        	} else {
        		i--;
        		System.out.println("Temperature null in" + typeName);
        		System.out.println("Temperature null in" + Temperatures.toString());
        		System.out.println("Temperature null in");
        		for(int cnt=0; cnt< TemperatureArray.length;cnt++) {
        			System.out.print(TemperatureArray[cnt] + "  ");
        		}
        		System.out.println("");
        	}
        }
        return lower;
    }
   public double[] getTemperaturesAsDoubleValues() {
    double[] temperatures = new double[Temperatures.size()];
        Iterator<Double> it = Temperatures.iterator();
        int count = 0;
        while(it.hasNext()) {
            Double tD = it.next();
            temperatures[count] = tD.doubleValue();
            count++;
        }
    return temperatures;
    }
};
