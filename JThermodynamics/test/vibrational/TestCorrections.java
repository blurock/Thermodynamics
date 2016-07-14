/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vibrational;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import thermo.data.structure.structure.vibrational.FrequencyCorrection;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestCorrections {

    public TestCorrections() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testCpCorrections() {
        
        FrequencyCorrection correction = new FrequencyCorrection();
        double[] temperatures = {300.0,400.0, 500.0, 600.0, 800.0, 1000.0, 1500.0};
        double[] frequencies  = {100.0,200.0,250.0,300.0,350.0,400.0,500.0,600.0,800.0,1000,1200.0,1500.0,2000.0,2500.0,3000.0};
        
        StringBuffer buf = new StringBuffer();
        buf.append("Table A15 in Benson\n");
        buf.append("Freq\t");
            for(int j=0;j<temperatures.length;j++) {
                buf.append(temperatures[j]);
                buf.append("\t");
            }
        for(int i=0;i<frequencies.length;i++) {
            buf.append("\n");
            buf.append(frequencies[i]);
            buf.append("\t");
            for(int j=0;j<temperatures.length;j++) {
                double value = correction.correctCpInCalories(frequencies[i], temperatures[j]);
                DecimalFormat format = new DecimalFormat("###.##");
                
                buf.append(format.format(value));
                buf.append("\t");
            }
        }
        System.out.println(buf.toString());
    }
    @Test
    public void testEntropyCorrections() {
        
        FrequencyCorrection correction = new FrequencyCorrection();
        double[] temperatures = {300.0,400.0, 500.0, 600.0, 800.0, 1000.0, 1500.0};
        double[] frequencies  = {50.0,75.0,100.0,125.0,200.0,250.0,300.0,350.0,400.0,500.0,600.0,800.0,1000,1200.0,1500.0,2000.0,2500.0,3000.0,3500.0};
        
        StringBuffer buf = new StringBuffer();
        buf.append("Table A.17 in Benson\n");
        buf.append("Freq\t");
            for(int j=0;j<temperatures.length;j++) {
                buf.append(temperatures[j]);
                buf.append("\t");
            }
        for(int i=0;i<frequencies.length;i++) {
            buf.append("\n");
            buf.append(frequencies[i]);
            buf.append("\t");
            for(int j=0;j<temperatures.length;j++) {
                double value = correction.correctEntropyInCalories(frequencies[i], temperatures[j]) + 0.05;
                DecimalFormat format = new DecimalFormat("###.#");
                
                buf.append(format.format(value));
                buf.append("\t");
            }
        }
        System.out.println(buf.toString());
    }

}