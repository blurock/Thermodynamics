/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jThergas.data.structure;

import java.text.DecimalFormat;
import jThergas.exceptions.JThergasReadException;

/** This class parses the structure information of the thermodynamic block
 * The first line contains the n
 * name of the structure (if not a group),
 * the nancy linear form,
 * INDIC,
 * optic symmetry,
 * internal symmetry,
 * external symmetry,
 * and the date.
 * The second line (if not empty) contains the formula
 *
 * @author blurock
 */
public class JThergasStructureData {
    private int tableNumber = 0;
    private int groupNumber = 0;
    private String nameOfStructure;
    private String nancyLinearForm;
    private int INDIC = 0;
    private double opticSymmetry = 0.0;
    private double internalSymmetry = 0.0;
    private double externalSymmetry = 0.0;
    private String dateString = "";
    boolean formulaGiven = false;
    private String formula = "";
    boolean group = false;
    
    String dotS = ".";
    /** The empty constructure
     *
     */
    public JThergasStructureData() {
    }

    /** The constructor with the minimal information set
     *
     * @param name The name of the structure
     * @param nancy The Nancy linear form of the structure
     */
    public JThergasStructureData(String name, String nancy) {
        nameOfStructure = name;
        nancyLinearForm = nancy;
    }

    /** Get the name of the structure
     *
     * @return the name of the structure
     */
    public String getNameOfStructure() {
        return nameOfStructure;
    }

    /** Set the name of the structure
     *
     * @param nameOfStructure
     */
    public void setNameOfStructure(String nameOfStructure) {
        this.nameOfStructure = nameOfStructure;
    }
    /** This parses line 1, supplemented with line 1a of the thermo block.
     * <ul>
     * <li> Line 1:
     * <ul>
     * <li> (0,0) Blank
     * <li> (1,2) The table number
     * <li> (3,6) The group number
     * <li> (7,8)
     * <li> if a group (from boolean):
     * <ul>
     * <li>    (10,57) the Nancy linear form
     * </ul>
     * <li> else
     * <ul>
     * <li>    (10,27)  The Nancy linear form
     * <li>    (28,57)  The structure name
     * </ul>
     * <li> (58,58) INDICS
     * <li> (59,62) optical symmetry
     * <li> (62,66) internal symmetry
     * <li> (73,76) external symmetry
     * <li> (77,79) date
     * </ul>
     * <li> Line 1a, if parsed (not empty):
     * <ul>
     * <li> A1     (0,0)   Blank or O
     * <li> 2x     (1,2)
     * <li> A18    (3,20)  Structure name
     * </ul>
     * </ul>
     * </ul>
      *
     * @param line1 The first line of information
     * @param line1a The second line of information (could be empty)
     * @param g true if a group
     * @throws jthergas.exceptions.JThergasReadException
     */
    public void parse(String line1, String line1a, boolean g) throws JThergasReadException {
        group = g;
        String tableNumberS = line1.substring(1, 3).trim();
        String groupNumberS = line1.substring(3, 7).trim();
        String lineS = line1.substring(7, 9);
        if(group) {
            setNancyLinearForm(line1.substring(10, 58));
            nameOfStructure = "";
        } else {
            setNancyLinearForm(line1.substring(10, 28));
            nameOfStructure = line1.substring(28, 58);
        }
        String INDICS = line1.substring(58, 59).trim();
        String opticSymmetryS = line1.substring(59, 63).trim();
        String internalSymmetryS = line1.substring(63, 67).trim();
        String externalSymmetryS = line1.substring(73, 77).trim();
        dateString = line1.substring(77, 80);

        try {
            tableNumber = Integer.parseInt(tableNumberS);
            groupNumber = Integer.parseInt(groupNumberS);
            INDIC = Integer.parseInt(INDICS);
            opticSymmetry = Double.parseDouble(opticSymmetryS);
            internalSymmetry = Double.parseDouble(internalSymmetryS);
            externalSymmetry = Double.parseDouble(externalSymmetryS);
            if (line1a.length() > 0) {
                if (!line1a.substring(0, 1).equals(" ")) {
                    formula = nancyLinearForm;
                    nancyLinearForm = line1a.substring(3).trim();
                }
            }
            if(group && line1.substring(9,10).equals(dotS)) {
                    nameOfStructure = formula;
            }
        } catch (NumberFormatException ex) {
            throw new JThergasReadException("Error in Parsing Structural Line:\n" + line1);
        }


    }

    /**
     *
     * @return The structure data as a one line string
     */
    public String writeToString() {
        DecimalFormat dec = new DecimalFormat("#0.0");

        StringBuffer buf = new StringBuffer();
        buf.append(" ");
        buf.append(String.format("%2d",getTableNumber()));
        buf.append(String.format("%4d",getGroupNumber()));
        buf.append(" 1 ");
        if(group) {
            buf.append(String.format("%-48s",getNancyLinearForm()));
        } else {
            buf.append(String.format("%-18s",getNancyLinearForm()));
            buf.append(String.format("%-30s", nameOfStructure));
        }
        buf.append(String.format("%1d",getINDIC()));
        buf.append(String.format("%4s", dec.format(getOpticSymmetry())));
        buf.append(String.format("%4s", dec.format(getInternalSymmetry())));
        buf.append("      ");
        buf.append(String.format("%4s", dec.format(getExternalSymmetry())));
        buf.append(getDateString());



        return buf.toString();
    }

    /** Get the Nancy linear form string
     *
     * @return Nancy linear form string
     */
    public String getNancyLinearForm() {
        return nancyLinearForm;
    }

    /** Get the Nancy linear form string
     *
     * @param nancyLinearForm Nancy linear form string
     */
    public void setNancyLinearForm(String nancyLinearForm) {
        this.nancyLinearForm = nancyLinearForm;
    }

    /** the formula
     *
     * @return the molecular formula
     */
    public String getFormula() {
        return formula;
    }

    /** The Benson table number of the data
     * @return the tableNumber
     */
    public int getTableNumber() {
        return tableNumber;
    }

    /** The benson group number of the data
     * @return the groupNumber
     */
    public int getGroupNumber() {
        return groupNumber;
    }

    /** INDIC
     * @return the INDIC
     */
    public int getINDIC() {
        return INDIC;
    }

    /** The optical symmetry of the molecule
     * @return the opticSymmetry
     */
    public double getOpticSymmetry() {
        return opticSymmetry;
    }

    /** The internal symmetry number of the molecule
     * @return the internalSymmetry
     */
    public double getInternalSymmetry() {
        return internalSymmetry;
    }

    /** The external symmetry number of the molecule
     * @return the externalSymmetry
     */
    public double getExternalSymmetry() {
        return externalSymmetry;
    }

    /** The reference date
     * @return the dateString
     */
    public String getDateString() {
        return dateString;
    }
}
