/*
 */
package jThergas.data.thermo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import jThergas.exceptions.JThergasReadException;

/** Class to parse through the line with the thermodynamic data:
 * enthalpy, entropy and the 7 heat capacities
 * (300, 400, 500, 600, 800, 1000, 1500)
 *
 * @author blurock
 */
public class JThergasThermoData {
    private

    int tableNumber = 0;
    private int groupNumber = 0;
    private double standardEnthalpy = 0.0;
    private int enthalpyReference = 0;
    private double standardEntropy = 0.0;
    private int entropyReference = 0;
    private double[] cpValues = new double[7];
    private int heatCapacityReference1 = 0;
    private int heatCapacityReference2 = 0;
    private String dateString;
    
    /**
     *
     */
    public JThergasThermoData() {
    }
    /* Parse the thermodyanamic data line.
     * 
     * A!:   (0,0) Blank
     * I2:   (1,2) tableNumber
     * I4:   (3,6) groupNumber
     * I2:   (7,8) 2
     * 1X:   (9,9) 
     * I2:   (10,11) enthalpyReference
     * F8.0: (12,19) standardEnthalpy
     * I2:   (20,21) entropyStandard
     * F8.0; (22,29) standardEntropy
     * I2:   (30,30) heatCapacityReference
     * F6.0: (31,35) cpValues[0] (300)
     * F6.0: (36,41) cpValues[1] (400)
     * F6.0: (42,47) cpValues[2] (500)
     * F6.0: (48,53) cpValues[3] (600)
     * F6.0: (54,59) cpValues[4] (800)
     * F6.0: (60,65) cpValues[5] (1000)
     * F6.0: (66,71) cpValues[6] (1500)
     *
     * @param line
     * @throws jthergas.exceptions.JThergasReadException
     */
    public void parse(String line)
           throws JThergasReadException {
        
        String tableNumberS       = line.substring(1, 3).trim();
        String groupNumberS       = line.substring(3, 7).trim();
        String enthalpyReferenceS = line.substring(10, 12).trim();
        String standardEnthalpyS  = line.substring(12, 20).trim();
        String entropyReferenceS  = line.substring(20, 22).trim();
        String standardEntropyS   = line.substring(22, 30).trim();
        String heatCapacityReference1S = line.substring(30, 32).trim();
        String cpValues300S       = line.substring(33, 39).trim();
        String cpValues400S       = line.substring(39, 45).trim();
        String cpValues500S       = line.substring(45, 51).trim();
        String cpValues600S       = line.substring(51, 57).trim();
        String cpValues800S       = line.substring(57, 63).trim();
        String cpValues1000S      = line.substring(63, 69).trim();
        String cpValues1500S      = line.substring(69, 75).trim();
        String heatCapacityReference2S = line.substring(75, 77).trim();
        setDateString(line.substring(77, 80));
        
        try {
            setTableNumber(Integer.parseInt(tableNumberS));
            setGroupNumber(Integer.parseInt(groupNumberS));
            setEnthalpyReference(Integer.parseInt(enthalpyReferenceS));
            if(standardEnthalpyS.length() != 0)
                setStandardEnthalpy(Double.parseDouble(standardEnthalpyS));
            else 
                setStandardEnthalpy(0.0);
            if(standardEntropyS.length() != 0)
                setStandardEntropy(Double.parseDouble(standardEntropyS));
            else
                setStandardEntropy(0.0);
            if(entropyReferenceS.length() != 0)
                setEntropyReference(Integer.parseInt(entropyReferenceS));
            else 
                setEntropyReference(0);
            if(heatCapacityReference1S.length() != 0) 
                setHeatCapacityReference1(Integer.parseInt(heatCapacityReference1S));
            else
                setHeatCapacityReference1(0);
            
            for(int i=0;i<6;i++) {
                getCpValues()[i] = 0.0;
            }
            if(cpValues300S.length() > 0)
                getCpValues()[0] = Double.parseDouble(cpValues300S);
            if(cpValues400S.length() > 0)
                getCpValues()[1] = Double.parseDouble(cpValues400S);
            if(cpValues500S.length() > 0)
                getCpValues()[2] = Double.parseDouble(cpValues500S);
            if(cpValues600S.length() > 0)
                getCpValues()[3] = Double.parseDouble(cpValues600S);
            if(cpValues800S.length() > 0)
                getCpValues()[4] = Double.parseDouble(cpValues800S);
            if(cpValues1000S.length() > 0)
                getCpValues()[5] = Double.parseDouble(cpValues1000S);
            if(cpValues1500S.length() > 0)
                getCpValues()[6] = Double.parseDouble(cpValues1500S);
            
            if(heatCapacityReference2S.length() != 0)
                setHeatCapacityReference2(Integer.parseInt(heatCapacityReference2S));
            else
                setHeatCapacityReference2(0);

        } catch (NumberFormatException ex) {
            throw new JThergasReadException("Error in Parsing Thermo Line:\n" + line);
        }
    }
    /*
     */
    /**
     *
     * @return the one line string with the thermo data
     */
    public String writeToString() {
        StringBuffer buf = new StringBuffer();
        DecimalFormat dec6 = new DecimalFormat("###.00");
        DecimalFormat dec8 = new DecimalFormat("####.000",new DecimalFormatSymbols(Locale.US));
        
        buf.append(" ");
        buf.append(String.format("%2d", new Integer(getTableNumber())));
        buf.append(String.format("%4d", new Integer(getGroupNumber())));
        buf.append(String.format("%2d", new Integer(2)));
        buf.append(" ");
        buf.append(String.format("%2d", new Integer(getEnthalpyReference())));
        buf.append(String.format("%8s",dec8.format(getStandardEnthalpy())));
        buf.append(String.format("%2d", new Integer(getEntropyReference())));
        buf.append(String.format("%8s",dec8.format(getStandardEntropy())));
        buf.append(String.format("%2d",getHeatCapacityReference1()));
        for(int i=0;i<7;i++) {
            buf.append(String.format("%6s",dec6.format(getCpValues()[i])));
        }
        buf.append(String.format("%2d ",getHeatCapacityReference2()));
        buf.append(getDateString());        
        
        return buf.toString();
    }

    /**
     *
     * @return The benson table number
     */
    public int getTableNumber() {
        return tableNumber;
    }

    /**
     *
     * @param tableNumber
     */
    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    /** the group number in the list
     *
     * @return the group number in the list
     */
    public int getGroupNumber() {
        return groupNumber;
    }

    /** groupNumber
     *
     * @param groupNumber
     */
    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    /** The standard enthalpy
     *
     * @return The standard enthalpy
     */
    public double getStandardEnthalpy() {
        return standardEnthalpy;
    }

    /**
     *
     * @param standardEnthalpy
     */
    public void setStandardEnthalpy(double standardEnthalpy) {
        this.standardEnthalpy = standardEnthalpy;
    }

    /** The enthalpy reference
     *
     * @return The enthalpy reference
     */
    public int getEnthalpyReference() {
        return enthalpyReference;
    }

    /**
     *
     * @param enthalpyReference
     */
    public void setEnthalpyReference(int enthalpyReference) {
        this.enthalpyReference = enthalpyReference;
    }

    /** standard entropy
     *
     * @return standard entropy
     */
    public double getStandardEntropy() {
        return standardEntropy;
    }

    /**
     *
     * @param standardEntropy
     */
    public void setStandardEntropy(double standardEntropy) {
        this.standardEntropy = standardEntropy;
    }

    /** The entropy reference
     *
     * @return The entropy reference
     */
    public int getEntropyReference() {
        return entropyReference;
    }

    /** entropyReference
     *
     * @param entropyReference
     */
    public void setEntropyReference(int entropyReference) {
        this.entropyReference = entropyReference;
    }

    /** The 7 cp values
     * 
     * Cp at 300, 400, 500, 600, 800, 1000, 1500
     * @return 7 cp values
     */
    public double[] getCpValues() {
        return cpValues;
    }

    /** The 7 cp values
     *
     * Cp at 300, 400, 500, 600, 800, 1000, 1500
     *
     * @param cpValues
     */
    public void setCpValues(double[] cpValues) {
        this.cpValues = cpValues;
    }

    /** heat capacity reference
     *
     * @return heat capacity reference
     */
    public int getHeatCapacityReference1() {
        return heatCapacityReference1;
    }

    /**
     *
     * @param heatCapacityReference1
     */
    public void setHeatCapacityReference1(int heatCapacityReference1) {
        this.heatCapacityReference1 = heatCapacityReference1;
    }

    /**
     *
     * @return heatCapacityReference2
     */
    public int getHeatCapacityReference2() {
        return heatCapacityReference2;
    }

    /**
     *
     * @param heatCapacityReference2
     */
    public void setHeatCapacityReference2(int heatCapacityReference2) {
        this.heatCapacityReference2 = heatCapacityReference2;
    }

    /** The date
     *
     * @return The date
     */
    public String getDateString() {
        return dateString;
    }

    /** The date
     *
     * @param dateString The date
     */
    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
