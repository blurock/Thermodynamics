/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import thermo.data.benson.HeatCapacityTemperaturePair;
import thermo.exception.ThermodynamicException;

/**
 *
 * @author blurock
 */
public class CorrectionToCpFromFrequency extends Vector<HeatCapacityTemperaturePair> {
    double frequencyValue;
    String elementName;
    String delimitor = ",";
    /** The empty constructor
     * 
     */
    public CorrectionToCpFromFrequency() {
    }
    /** Constructor with string line 
     * Convert a String line to a {@link HeatCapacityTemperaturePair} and store it
     * 
     * The line is a set of deliminated (default: comma) double values
     * The first double value is the frequency.
     * The next set of double values corresponds to the correction
     * for each temperature. The number of corrections has to be the same as the number of temperatures.
     * 
     * @param linedata The data in one line
     * @param temperatures The set of temperatures corresponding to the Cp values
     * @param reference A reference string (from where the data comes)
     * @throws thermo.exception.ThermodynamicException
     */
    public CorrectionToCpFromFrequency(String linedata, double[] temperatures, String reference) throws ThermodynamicException {
        convertLineToData(linedata,temperatures,reference);
    }
    /** Convert a String line to a {@link HeatCapacityTemperaturePair} and store it
     * 
     * The line is a set of deliminated (default: comma) double values
     * The first double value is the frequency.
     * The next set of double values corresponds to the correction
     * for each temperature. The number of corrections has to be the same as the number of temperatures.
     * 
     * @param linedata The data in one line
     * @param temperatures The set of temperatures corresponding to the Cp values
     * @param reference A reference string (from where the data comes)
     * @throws thermo.exception.ThermodynamicException
     */
    private void convertLineToData(String linedata, double[] temperatures, String reference) throws ThermodynamicException {
        StringTokenizer tok = new StringTokenizer(linedata,delimitor);
        if(tok.countTokens() != temperatures.length + 1) {
            String freqS = tok.nextToken();
            Double frequencyD = new Double(freqS);
            frequencyValue = frequencyD.doubleValue();
            formCorrectionName();
            for(int i=0;i<temperatures.length;i++) {
                String correctionS = tok.nextToken();
                Double correctionD = new Double(correctionS);
                double correction = correctionD.doubleValue();
                HeatCapacityTemperaturePair pair = 
                        new HeatCapacityTemperaturePair(elementName, temperatures[i], correction, reference);
                this.add(pair);
            }
        } else {
            throw new ThermodynamicException("Illegal Correction to Cp Due to Frequency Data: " + linedata);
        }
    }
    /** Form the element name for this set of corrections
     * 
     * This is used to bind together the set of corrections of one frequency in the database
     * 
     */
    private void formCorrectionName() {
        StringBuffer buf = new StringBuffer();
        buf.append("FrequencyCorrection:");
        buf.append(frequencyValue);
        elementName = buf.toString();
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(elementName);
        buf.append("( ");
        Iterator<HeatCapacityTemperaturePair> iter = this.iterator();
        while(iter.hasNext()) {
            HeatCapacityTemperaturePair pair = iter.next();
            buf.append(pair.toString());
            buf.append("\t");
        }
        buf.append(" )");
        return buf.toString();
    }
}
