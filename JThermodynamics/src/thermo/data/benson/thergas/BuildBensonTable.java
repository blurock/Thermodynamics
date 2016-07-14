/*
 */
package thermo.data.benson.thergas;

import jThergas.data.JThermgasThermoStructureDataPoint;
import jThergas.data.group.JThergasReadGroupStructureThermo;
import jThergas.data.group.JThergasThermoStructureGroupPoint;
import jThergas.data.read.JThergasReadStructureThermo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import jThergas.exceptions.JThergasReadException;
import java.util.HashSet;
import java.util.Iterator;
import nu.xom.ParsingException;
import thermo.data.benson.BensonGroupStructure;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.CML.CMLBensonThermodynamicBase;
import thermo.data.benson.DB.SQLBensonThermodynamicBase;
import thermo.data.benson.DB.SQLGroupElement;
import thermo.data.benson.DB.ThermoSQLConnection;

/**  Build the benson tables from file
 *
 * The {@link JThergasReadStructureThermo}
 * class is used to parse the raw Thergas file.
 * Each {@link JThermgasThermoStructureDataPoint} is looped over
 * and the {@link BensonGroupStructure} and {@link BensonThermodynamicBase} classes are built with
 *  {@link BuildBensonThermodynamicFromThergas}.
 * Through {@link SQLBensonThermodynamicBase} and {@link SQLGroupElement}
 * these are put into the database.
 *
 *
 * @author blurock
 */
public class BuildBensonTable {

    JThergasReadStructureThermo readThergasTable;
    BuildBensonThermodynamicFromThergas buildBenson;
    ThermoSQLConnection connection;
    SQLGroupElement sqlelement;
    SQLBensonThermodynamicBase sqlthermo;
    boolean success;

    /** The empty constructor
     *
     * Sets up:
     * <ul>
     * <li> {@link BuildBensonThermodynamicFromThergas}
     * <li> {@link ThermoSQLConnection}
     * <li> {@link SQLGroupElement}
     * <li> {@link SQLGroupElement}
     * </ul>
     * and, in addition, starts the SQL connection.
     *
     *
     */
    public BuildBensonTable() {
        initialize();
    }

    protected void initialize() {
        buildBenson = new BuildBensonThermodynamicFromThergas();
        connection = new ThermoSQLConnection();
        success = connection.connect();
        if (success) {
            sqlelement = new SQLGroupElement(connection);
            sqlthermo = new SQLBensonThermodynamicBase(connection);
        }
    }
    /** Initialize the Benson Table
     *
     * Through calls to initialization methods in
     * {@link SQLGroupElement} and {@link SQLBensonThermodynamicBase}
     * the Benson table is initialized.
     * 
     * @throws java.sql.SQLException
     */
    public void initializeTable() throws SQLException {
        sqlelement.initializeStructureInDatabase();
        sqlthermo.initializeStructureInDatabase();

    }
    /** Initialize the Benson Table
     *
     * Through calls to initialization methods in
     * {@link SQLGroupElement} and {@link SQLBensonThermodynamicBase}
     * the Benson table is initialized.
     *
     * @throws java.sql.SQLException
     */
    public void initializeTable(String reference) throws SQLException {
        HashSet<String> names = sqlthermo.findElementsOfReference(reference);
        sqlelement.deleteUsingElementNames(names);
        sqlthermo.initializeStructureInDatabase(reference);
    }

    /** Build the benson tables from file
     * 
     * The raw Thergas information file (f) is parsed with
     * {@link JThergasReadStructureThermo} and forms a vector of
     * {@link JThermgasThermoStructureDataPoint} classes.
     *  Then the {@link BuildBensonTable#setUpDatabase(boolean, java.lang.String)
     * method is called to set up the database
     * 
     * @param f The file to parse
     * @param cmltest true if a cmltest is to be performed on {@link BensonThermodynamicBase}
     * @return The (error) string giving the result of build
     * @throws JThergasReadException
     * @throws FileNotFoundException
     * @throws IOException 
     * 
     */
    public String build(File f, boolean cmltest) throws JThergasReadException, FileNotFoundException, IOException {
        String errorString = "No Parsing Errors Detected";
        String sqlerror = "No Database Errors Detected";
        String reference = f.toString();
        readThergasTable = new JThergasReadGroupStructureThermo();
        try {
            readThergasTable.readAndParse(f);
        } catch (JThergasReadException ex) {
            errorString = ex.toString();
            Logger.getLogger(BuildBensonTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            setUpDatabase(cmltest, reference);
        } catch (SQLException ex) {
            sqlerror = ex.toString();
            Logger.getLogger(BuildBensonTable.class.getName()).log(Level.SEVERE, null, ex);
        }

        return errorString + "\n=========================================\n" + sqlerror;
    }
   /** Build the benson tables from input stream
     * 
     * The raw Thergas information file (f) is parsed with
     * {@link JThergasReadStructureThermo} and forms a vector of
     * {@link JThermgasThermoStructureDataPoint} classes.
     *  Then the {@link BuildBensonTable#setUpDatabase(boolean, java.lang.String)
     * method is called to set up the database
     * 
    * @param inp  The input stream
    * @param reference  The reference string
    * @param cmltest true if a cmltest is to be performed on {@link BensonThermodynamicBase}
     * @return The (error) string giving the result of build
     * @throws JThergasReadException
     * @throws FileNotFoundException
     * @throws IOException 
     * 
     */
    public String build(InputStream inp, String reference, boolean cmltest) throws JThergasReadException, FileNotFoundException, IOException {
        String errorString = "No Parsing Errors Detected";
        String sqlerror = "No Database Errors Detected";
        readThergasTable = new JThergasReadGroupStructureThermo();
        try {
            readThergasTable.readAndParse(inp);
        } catch (JThergasReadException ex) {
            errorString = ex.toString();
            Logger.getLogger(BuildBensonTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            setUpDatabase(cmltest, reference);
        } catch (SQLException ex) {
            sqlerror = ex.toString();
            Logger.getLogger(BuildBensonTable.class.getName()).log(Level.SEVERE, null, ex);
        }

        return errorString + "\n=========================================\n" + sqlerror;
    }

    /**
     * A data string of the raw Thergas information is parsed with
     * {@link JThergasReadStructureThermo} and forms a vector of
     * {@link JThermgasThermoStructureDataPoint} classes.
     *  Then the {@link BuildBensonTable#setUpDatabase(boolean, java.lang.String)
     * method is called to set up the database
     *
     * @param data
     * @param cmltest
     * @param reference
     * @return 
     */
    public String build(String data, boolean cmltest, String reference) {
        readThergasTable = new JThergasReadGroupStructureThermo();
        String errorString = "No Parsing Errors Detected";
        String sqlerror = "No Database Errors Detected";
        try {
            readThergasTable.readAndParse(data);
        } catch (JThergasReadException ex) {
            Logger.getLogger(BuildBensonTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            setUpDatabase(cmltest, reference);
        } catch (SQLException ex) {
            sqlerror = ex.toString();
            Logger.getLogger(BuildBensonTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return errorString + "\n=========================================\n" + sqlerror;
    }

    /**
     * The calling routine to this method has  parsed the
     *   raw Thergas information with
     * {@link JThergasReadStructureThermo} and formed a vector of
     * {@link JThermgasThermoStructureDataPoint} classes.
     * This vector is looped through two methods are called:
     * <ul>
     * <li> addBensonToDatabase: adds the parsed information into the SQL database
     * <li> testCMLBensonThermodynamicBase: a test of the CML structures (only performed
     * if cmltest is true).
     * </ul>
     *
     * @param cmltest true if a cmltest is to be performed on {@link BensonThermodynamicBase}
     * @param reference The reference string name
     * @throws java.sql.SQLException
     */
    public void setUpDatabase(boolean cmltest, String reference) throws SQLException {
        String errorString = "";

        //sqlelement.initializeStructureInDatabase();
        //sqlthermo.initializeStructureInDatabase(reference);
        Vector<JThermgasThermoStructureDataPoint> data = readThergasTable.getData();
        for (int i = 0; i < data.size(); i++) {
            JThergasThermoStructureGroupPoint point = (JThergasThermoStructureGroupPoint) readThergasTable.getData().elementAt(i);
            try {
                BensonThermodynamicBase thermo = addBensonToDatabase(point, reference);
                if (cmltest) {
                    testCMLBensonThermodynamicBase(thermo);
                }
            } catch (SQLException ex) {

                errorString = errorString + "\n------------SQL Error #" + i + " --------------\n" + point.writeToString() + "\n" + ex.toString();
            }

        }

        System.out.println("Done Parsing");
        System.out.println(buildBenson.getSetOfMetaAtoms().writeAsString());
        if (errorString.length() > 0) {
            throw new SQLException(errorString);
        }
    }

    /** Adds the thermo data to the SQL database
     *
     * Through {@link BuildBensonThermodynamicFromThergas} the
     * {@link BensonGroupStructure} and {@link BensonThermodynamicBase} classes are built.
     * Through {@link SQLBensonThermodynamicBase} and {@link SQLGroupElement}
     * these are put into the database.
     *
     *
     * @param i The ith element parsed in the file
     * @param reference The reference string name
     * @return The interpreted thermodynamic information
     * @throws java.sql.SQLException
     */
    private BensonThermodynamicBase addBensonToDatabase(JThergasThermoStructureGroupPoint point, String reference) throws SQLException {
        BensonGroupStructure grp = buildBenson.buildBensonGroupStructure(point);
        BensonThermodynamicBase thermo = buildBenson.buildBensonThermodynamicBase(point, reference);

        if (grp != null) {
            sqlelement.deleteGroupElementFromDatabase(grp);
            sqlelement.addToDatabase(grp);
        }
        System.out.println(thermo.toString());
        sqlthermo.deleteElement(thermo);
        sqlthermo.addToDatabase(thermo);
        return thermo;
    }

    /** test if the CML structure can be built.
     *
     * using the variety of the parsed thermo data, the CML structure
     * generation of {@link CMLBensonThermodynamicBase} is tested.
     * The routines {@link CMLBensonThermodynamicBase#setStructure(java.lang.Object)},
     * {@link CMLBensonThermodynamicBase#restore() } and {@link CMLBensonThermodynamicBase#parse(java.lang.String) }
     * are tested.
     *
     * @param thermo
     * @throws java.sql.SQLException
     */
    private void testCMLBensonThermodynamicBase(BensonThermodynamicBase thermo) throws SQLException {
        System.out.println("---------------------------------------------------------");
        System.out.print("BensonThermodynamicBase CML Test");
        CMLBensonThermodynamicBase cmlthermo = new CMLBensonThermodynamicBase();
        cmlthermo.setStructure(thermo);
        String cmlthermoS = cmlthermo.restore();
        System.out.println(cmlthermoS);
        CMLBensonThermodynamicBase cmlthermo2 = new CMLBensonThermodynamicBase();
        try {
            cmlthermo2.parse(cmlthermoS);
        } catch (ParsingException ex) {
            Logger.getLogger(BuildBensonTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BuildBensonTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        BensonThermodynamicBase thermo2 = (BensonThermodynamicBase) cmlthermo2.getStructure();
        thermo2.toString();

        HashSet vec = sqlthermo.retrieveStructuresFromDatabase(thermo2.getID());
        Iterator<BensonThermodynamicBase>  iter = vec.iterator();
        BensonThermodynamicBase thermo3 = iter.next();
        System.out.println(thermo3.toString());
        System.out.println("---------------------------------------------------------");

    }
}
