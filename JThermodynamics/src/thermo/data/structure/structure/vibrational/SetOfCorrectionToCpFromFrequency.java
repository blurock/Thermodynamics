/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational;

import jThergas.data.read.ReadFileToString;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import thermo.exception.ThermodynamicException;
import thermo.properties.SProperties;

/**
 *
 * @author blurock
 */
public class SetOfCorrectionToCpFromFrequency  extends Vector<CorrectionToCpFromFrequency> {
    String reference;
    double[] temperatureValues;
    String temperatureDelimitor = ",";
    String title = "FrequencyCorrections: ";
    
    public SetOfCorrectionToCpFromFrequency(boolean  readindefault) throws IOException, ThermodynamicException {
        if(readindefault) {
            String fileasstring = SProperties.getResourceAsString("thermo.data.vibrationcorrections");
            convertToSet(fileasstring);
        }
    }
    public SetOfCorrectionToCpFromFrequency(File file, String ref) throws ThermodynamicException {
        reference = ref;
        ReadFileToString file2string = new ReadFileToString();
        file2string.read(file);
        convertToSet(file2string.outputString);
    }
    public SetOfCorrectionToCpFromFrequency(String setOfLines,String ref) throws ThermodynamicException {
        reference = ref;
        convertToSet(setOfLines);
    }

    private void convertToSet(String setOfLines) throws ThermodynamicException {
        StringTokenizer tok = new StringTokenizer(setOfLines,"\n");
        if(tok.countTokens() > 3) {
            String line1 = tok.nextToken();
            String line2 = tok.nextToken();
            extractTemperatureValues(line2);
            while(tok.hasMoreElements()) {
                String line = tok.nextToken();
                CorrectionToCpFromFrequency freq = new CorrectionToCpFromFrequency(line, temperatureValues, reference);
                this.add(freq);
            }
        }
    }

    private void extractTemperatureValues(String line2) {
        StringTokenizer tok = new StringTokenizer(line2, temperatureDelimitor);
        String freqS = tok.nextToken();
        temperatureValues = new double[tok.countTokens()];
        int count = 0;
        while(tok.hasMoreElements()) {
            String tempS = tok.nextToken();
            Double tempD = new Double(tempS);
            temperatureValues[count] = tempD.doubleValue();
            count++;
        }
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(title);
        buf.append("\n");
        Iterator<CorrectionToCpFromFrequency> iter = this.iterator();
        while(iter.hasNext()) {
            CorrectionToCpFromFrequency correct = iter.next();
            buf.append(correct.toString());
            buf.append("\n");
        }
        return buf.toString();
    }
}
