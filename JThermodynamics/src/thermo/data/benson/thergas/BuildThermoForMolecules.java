/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson.thergas;

import jThergas.data.JThermgasThermoStructureDataPoint;
import jThergas.data.read.JThergasReadStructureThermo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import jThergas.exceptions.JThergasReadException;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.SQLBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLMolecule;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;
import thermo.data.structure.structure.DB.SQLSubstituteBackMetaAtomIntoMolecule;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author reaction
 */
public class BuildThermoForMolecules {

    JThergasReadStructureThermo readThergasTable;
    BuildBensonThermodynamicFromThergas buildBenson;
    ThermoSQLConnection connection;
    SQLBensonThermodynamicBase sqlthermo;
    String sourceS;
    String bensonAtomType = "BensonAtom";
    SQLMetaAtomDefinitionFromMetaAtomInfo sqlMetaAtom;
    SetOfMetaAtomsForSubstitution metaAtomSubstitutions;
    boolean success;
    SQLSubstituteBackMetaAtomIntoMolecule substitute;
    SQLMolecule sqlmolecule;
    public BuildThermoForMolecules() {
        initialize();
    }
    protected void initialize() {
        buildBenson = new BuildBensonThermodynamicFromThergas();
        connection = new ThermoSQLConnection();
        success = connection.connect();
        if (success) {
            sqlthermo = new SQLBensonThermodynamicBase(connection);
            sqlmolecule = new SQLMolecule(connection);
            String nancy = new String("NancyLinearForm");
            try {
                sqlMetaAtom = new SQLMetaAtomDefinitionFromMetaAtomInfo(connection);
                metaAtomSubstitutions = sqlMetaAtom.createSubstitutionSets(bensonAtomType);
                substitute = new SQLSubstituteBackMetaAtomIntoMolecule(nancy, connection);
            } catch (SQLException ex) {
                Logger.getLogger(BuildThermoForMolecules.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CDKException ex) {
                Logger.getLogger(BuildThermoForMolecules.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BuildThermoForMolecules.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BuildThermoForMolecules.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    public void initializeTable(String reference) throws SQLException {
        sqlthermo.initializeStructureInDatabase(reference);
        sqlmolecule.deleteFromSource(reference);
    }
    public String build(ThermoSQLConnection c, File f, boolean cmltest) throws JThergasReadException, FileNotFoundException, IOException {
        String referenceS = f.toString();
        return build(c,f,referenceS,cmltest);
    }

        /** Build the benson tables from file
     *
     * The raw Thergas information file (f) is parsed with
     * {@link JThergasReadStructureThermo} and forms a vector of
     * {@link JThermgasThermoStructureDataPoint} classes.
     *  Then the {@link BuildBensonTable#setUpDatabase(boolean, java.lang.String)
     * method is called to set up the database
     *
     * @param c 
     * @param f The file to parse
     * @param reference The reference name of the set of molecules
     * @param cmltest true if a cmltest is to be performed on {@link BensonThermodynamicBase}
     * @return
     * @throws JThergasReadException
     * @throws FileNotFoundException
     * @throws IOException 
     *
     */
    public String build(ThermoSQLConnection c, File f, String reference, boolean cmltest) throws JThergasReadException, FileNotFoundException, IOException {
        String errorString = "No Parsing Errors Detected";
        String sqlerror = "No Database Errors Detected";
        sourceS = reference;
        readThergasTable = new JThergasReadStructureThermo();
        try {
            readThergasTable.readAndParse(f);
        } catch (JThergasReadException ex) {
            errorString = ex.toString();
            Logger.getLogger(BuildBensonTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            setUpDatabase(c,cmltest, reference);
        } catch (CDKException ex) {
            Logger.getLogger(BuildThermoForMolecules.class.getName()).log(Level.SEVERE, null, ex);
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
     * @param c 
     * @param cmltest true if a cmltest is to be performed on {@link BensonThermodynamicBase}
     * @param reference The reference string name
     * @throws java.sql.SQLException
     */
    public void setUpDatabase(ThermoSQLConnection c, boolean cmltest, String reference) throws SQLException, CDKException {
        String errorString = "";
        Vector<JThermgasThermoStructureDataPoint> data = readThergasTable.getData();
        for (int i = 0; i < data.size(); i++) {
            JThermgasThermoStructureDataPoint point = readThergasTable.getData().elementAt(i);
            try {
                addMoleculeStructureToDatabase(point,c);
                BensonThermodynamicBase thermo = addBensonToDatabase(point, reference);
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
    protected void addMoleculeStructureToDatabase(JThermgasThermoStructureDataPoint point,ThermoSQLConnection c) throws CDKException, SQLException {
        try {
            Molecule molecule = buildBenson.buildMolecule(point,c);
            substitute.substitute(molecule);
           StructureAsCML cmlstruct = new StructureAsCML(molecule);
            Molecule substituted = metaAtomSubstitutions.substitute(cmlstruct);
            System.out.println(cmlstruct.getCmlStructureString());

            sqlmolecule.addToDatabase(molecule, sourceS);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BuildThermoForMolecules.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BuildThermoForMolecules.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(BuildThermoForMolecules.class.getName()).log(Level.SEVERE, null, ex);
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
    private BensonThermodynamicBase addBensonToDatabase(JThermgasThermoStructureDataPoint point, String reference) throws SQLException {
        BensonThermodynamicBase thermo = buildBenson.buildBensonThermodynamicBase(point, reference);
        System.out.println(thermo.toString());
        sqlthermo.deleteSourceFromDatabase(thermo.getID());
        sqlthermo.addToDatabase(thermo);
        return thermo;
    }
    public void deleteDatabaseFromSource(String reference) throws SQLException {
        SQLMolecule sqlmol = new SQLMolecule(connection);
        sqlmol.deleteFromSource(reference);
    }
}
