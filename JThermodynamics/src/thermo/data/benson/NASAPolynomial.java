/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.Molecule;
import thermo.data.structure.structure.AtomCounts;
import thermo.exception.ThermodynamicComputeException;
import thermo.properties.ChemicalConstants;


/**
 *
 * @author blurock
 */
public class NASAPolynomial implements ThermodynamicInformation {

    String reference;

    ArrayList molecules = null;
    /** The name of the molecule
     */
    public String name;
    /** The array of atom symbol names
     */
    public String atoms[] = new String[4];
    /** The corresponding counts of each atom symbol
     */
    public int atomcnt[] = new int[4];
    /** The phase of the molecule
     * Here it is, by default gas (G)
     */
    public String phase;
    /** The lower temperature of the range
     */
    public double lowerT;
    /** The upper temperature of the range
     */
    public double upperT;
    /** The common temperature between the two NASA polynomials
     */
    public double middleT;
    /** The lower temperature range coefficients
     */
    public double[] lower = new double[7];
    /** The upper temperature range coefficients
     */
    public double[] upper = new double[7];

    /** Creates a new instance of ThermoNASAPoly */
    public NASAPolynomial() {
        reference = "NASAPolynomial";
    }

    /** Given a molecule formula, the atoms and atom counts are filled in
     *
     * @param formula The formula (isomer name)
     * @throws IOException For an illegal formula
     */
    public void extractFormulaFromText(String formula) throws IOException {
        int count = 0;
        int index = 0;
        atoms = new String[4];
        atomcnt = new int[4];
        for (int i = 0; i < 4; i++) {
            atoms[i] = "";
        }
        try {
            while (count < formula.length()) {
                //System.out.println(count + ", " + formula);
                int nS = nextSymbol(formula, count);
                int nN = nextNumber(formula, nS + 1);
                if (nS < count || nS == nN) {
                    throw new IOException("Formula not correct: " + formula);
                }
                Integer aI = new Integer(formula.substring(nS + 1, nN + 1));
                atomcnt[index] = aI.intValue();
                atoms[index] = formula.substring(count, nS + 1);
                index++;
                count = nN + 1;
            }
        } catch (NumberFormatException ex) {
            throw new IOException("Formula not correct: " + formula);
        }
    }

    private boolean isNumber(char c) {
        return (c >= '1' && c <= '9') || c == '0';
    }

    private int nextNumber(String text, int start) {
        int count = start;
        while (count < text.length() && isNumber(text.charAt(count))) {
            count++;
        }
        count--;
        return count;
    }

    private int nextSymbol(String text, int start) {
        int count = start;
        while (count < text.length() && !isNumber(text.charAt(count))) {
            count++;
        }
        count--;
        return count;
    }

    /** parse from a {@link String}
     * 
     * The string should have the 4 lines of a NASA polynomial
     * 
     * @param nasalines The 4 lines of a NASA polynomial
     * @throws IOException If an error occurs in parsing
     */
    public void parse(String nasalines) throws IOException {
        StringTokenizer tok = new StringTokenizer(nasalines,"\n");
        parse(tok);
    }
    /** parse from a {@link StringTokenizer}
     *
     * The tokenizer should have at least 4 tokens, one for each line of the NASA polynomial
     *
     * @param nasatok The tokenizer with 4 elements
     * @throws IOException If an error occurs in parsing
     */
    public void parse(StringTokenizer nasatok) throws IOException {
        if(nasatok.countTokens() > 4) {

        } else {
            String error = "";
            if(nasatok.countTokens() > 0) {
            error = "not enough lines for NASA polynomial: " +
                    nasatok.countTokens() +
                    "  on line: " + nasatok.nextToken();
            } else {
                error = "No lines for NASA polynomials";
            }
            throw new IOException(error);
        }
    }
    /** Parse the four lines making up the NASA polynomial
     *
     * @param l1 Line 1
     * @param l2 Line 2
     * @param l3 Line 3
     * @param l4 Line 4
     *
     * The objects of the class are filled in
     *
     * @throws IOException The lines don't contain the right info
     */
    public void parse(String l1, String l2, String l3, String l4) throws IOException {
        String namefield = l1.substring(0, 23);
        StringTokenizer tok = new StringTokenizer(namefield, " ");
        name = tok.nextToken();
        //System.out.println(" Therm: " + name);
        atoms[0] = l1.substring(24, 26).trim();
        atoms[1] = l1.substring(29, 31).trim();
        atoms[2] = l1.substring(34, 36).trim();
        atoms[3] = l1.substring(39, 41).trim();

        try {
            atomcnt[0] = convertInt(l1.substring(26, 29));
            atomcnt[1] = convertInt(l1.substring(31, 34));
            atomcnt[2] = convertInt(l1.substring(36, 39));
            atomcnt[3] = convertInt(l1.substring(41, 44));

            phase = l1.substring(44, 44);
            //System.out.println(l2.substring(0, 15));
            
            lowerT = parseDouble(l1.substring(45, 55));
            upperT = parseDouble(l1.substring(55, 65));
            middleT = parseDouble(l1.substring(65, 74));
            upper[0] = parseDouble(l2.substring(0, 15));
            upper[1] = parseDouble(l2.substring(15, 30));
            upper[2] = parseDouble(l2.substring(30, 45));
            upper[3] = parseDouble(l2.substring(45, 60));
            upper[4] = parseDouble(l2.substring(60, 75));
            upper[5] = parseDouble(l3.substring(0, 15));
            upper[6] = parseDouble(l3.substring(15, 30));
            lower[0] = parseDouble(l3.substring(30, 45));
            lower[1] = parseDouble(l3.substring(45, 60));
            lower[2] = parseDouble(l3.substring(60, 75));
            lower[3] = parseDouble(l4.substring(0, 15));
            lower[4] = parseDouble(l4.substring(15, 30));
            lower[5] = parseDouble(l4.substring(30, 45));
            lower[6] = parseDouble(l4.substring(45, 60));
        } catch (NumberFormatException ex) {
            throw new IOException("Error in reading NASA polynomial: \n" + ex.toString() + "\n " + l1);
        }
    }

    int convertInt(String str) throws NumberFormatException {
        str = str.trim();
        int cnt = 0;
        if (str.length() != 0) {
            cnt = Integer.parseInt(str);
        }
        return cnt;
    }

    double parseDouble(String str) throws NumberFormatException {
        str = str.trim();
        double cnt = 0;
        if (str.length() != 0) {
            cnt = Double.parseDouble(str);
        }
        return cnt;
    }

    /** A primitive molecular formula from the atom counts
     *
     * @return The primitive formula
     *
     * This is a direct translation of the atom count information
     */
    public String moleculeFormula() {
        StringBuffer formula = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            if (atoms[i].length() > 0) {
                if(atomcnt[i] > 0) {
                    formula.append(atoms[i]);
                if(atomcnt[i] > 1)
                    formula.append(atomcnt[i]);
                }
            }
        }

        return formula.toString();
    }

    /** Convert to a NASAPolynomial String
     *
     * @return The 4 line string
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        Formatter f = new Formatter();
        if(name != null)
            buf.append(f.format("%-24s", name));
        else
            buf.append("                        ");

        if (atoms != null) {
            for (int i = 0; i < 4; i++) {
                f = new Formatter();
                if (atoms[i] != null && atoms[i].length() > 0) {
                    buf.append(f.format("%-3s%2d", atoms[i].toLowerCase(), atomcnt[i]));
                } else {
                    buf.append("     ");
                }
            }
        } else {
            buf.append("                    ");
        }
        buf.append("G");
        f = new Formatter();
        buf.append(f.format("%10.2f%10.2f%10.2f    1\n", lowerT, upperT, middleT));
        f = new Formatter();
        buf.append(f.format("%+15.8e%+15.8e%+15.8e%+15.8e%+15.8e    2\n",
                upper[0], upper[1], upper[2], upper[3], upper[4]));
        f = new Formatter();
        buf.append(f.format("%+15.8e%+15.8e%+15.8e%+15.8e%+15.8e    3\n",
                upper[5], upper[6], lower[0], lower[1], lower[2]));
        f = new Formatter();
        buf.append(f.format("%+15.8e%+15.8e%+15.8e%+15.8e                   4\n",
                lower[3], lower[4], lower[5], lower[6]));
        return buf.toString();
    }
    void fillInMoleculeProperties(Molecule mol) {
        AtomCounts counts = new AtomCounts(mol);
        atoms = counts.getAtomStringArray(4);
        atomcnt = counts.correspondingAtomCount(4);
        name = mol.getID();
     }
    /** Get the origin of the polynomial
     *
     * @return The name
     */
    public String getThermodynamicType() {
        return reference;
    }
    /** Set the origin of the polynomial
     *
     * @param type The name
     */
    public void setThermodynamicType(String type) {
        reference = type;
    }
    /** The standard enthalpy in calories per mole
     *
     * @return The standard enthalpy at 298 (1 atm)
     */
    public double getStandardEnthalpy298() {
        double enthalpy = 0.0;
        try {
            enthalpy = computeEnthalpy(298.0);
        } catch (ThermodynamicComputeException ex) {
            Logger.getLogger(NASAPolynomial.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enthalpy;
    }
    /** Standard Entropy
     *
     * @return The standard entropy at 298 (1 atm) in calories per mole
     */
    public double getStandardEntropy298(){
        double entropy = 0.0;
        try {
            entropy =  computeEntropy(298.0);
        } catch (ThermodynamicComputeException ex) {
            Logger.getLogger(NASAPolynomial.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entropy;
    }
    /**
     *
     * @param temperature The temperature
     * @return The enthalpy at that temperature (calories per mole)
     * @throws ThermodynamicComputeException
     */
    public double computeEnthalpy(double temperature) throws ThermodynamicComputeException {
        double enthalpy = 0.0;
        if(temperature >= middleT) {
            enthalpy = computeEnthalpy(upper,temperature);
        } else {
            enthalpy = computeEnthalpy(lower,temperature);
        }
        return enthalpy*ChemicalConstants.getGasConstantInCalsMolesK()*temperature;

    }
    /** Entropy
     *
     * @param temperature The temperature
     * @return The entropy at that tempeature (calories per mole)
     * @throws ThermodynamicComputeException
     */
    public double computeEntropy(double temperature)  throws ThermodynamicComputeException {
        double entropy = 0.0;
        if(temperature >= middleT) {
            entropy = computeEntropy(upper,temperature);
        } else {
            entropy = computeEntropy(lower,temperature);
        }
        return entropy*ChemicalConstants.getGasConstantInCalsMolesK();

    }
    /** Heat capacity
     *
     * @param temperature The temperature
     * @return The heat capacity in calories per mole
     * @throws ThermodynamicComputeException
     */
    public double getHeatCapacity(double temperature)  throws ThermodynamicComputeException {
        double cp = 0.0;
        if(temperature >= middleT) {
            cp = computeHeatCapacity(upper,temperature);
        } else {
            cp = computeHeatCapacity(lower,temperature);
        }
        return cp*ChemicalConstants.getGasConstantInCalsMolesK();
    }
    double computeHeatCapacity(double[] a, double t) {
        double cp = a[0]
                +   t* (a[1]
                +   t* (a[2]
                +   t* (a[3]
                +   t* (a[4]))));
        return cp;
    }
    double computeEnthalpy(double[] a, double t) {
        double enthalpy = a[0]
                +   t* (a[1] / 2.0
                +   t* (a[2] / 3.0
                +   t* (a[3] / 4.0
                +   t* (a[4] / 5.0))))
                +   a[5]/t;
        return enthalpy;
    }
    double computeEntropy(double[] a, double t) {
        double entropy = a[0] * Math.log(t)
                +   t* (a[1]
                +   t* (a[2] / 2.0
                +   t* (a[3] / 3.0
                +   t* (a[4] / 4.0))))
                +   a[6];
        return entropy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
