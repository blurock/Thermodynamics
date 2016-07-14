/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package molecules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jThergas.exceptions.JThergasReadException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.thergas.BuildThermoForMolecules;
import static org.junit.Assert.*;

/**
 *
 * @author reaction
 */
public class BuildThermoForMoleculesTest {

    public BuildThermoForMoleculesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void smallTestOfSeveralTestBlocks() {
        BuildThermoForMolecules build = new BuildThermoForMolecules();
        ThermoSQLConnection connection = new ThermoSQLConnection();
        connection.connect();
        try {

            System.out.println("Initial Database");
            String source = "mol-small.don";
            build.initializeTable(source);
        } catch (SQLException ex) {
            Logger.getLogger(BuildThermoForMoleculesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Read in File");
        File bensonFile = new File("mol-small.don");
            try {
                build.build(connection, bensonFile, false);
            } catch (JThergasReadException ex) {
                Logger.getLogger(BuildThermoForMoleculesTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuildThermoForMoleculesTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BuildThermoForMoleculesTest.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
    @Test
    public void deleteTestFromDatabase() {
        try {
            BuildThermoForMolecules build = new BuildThermoForMolecules();
            String source = "mol-small.don";
            build.deleteDatabaseFromSource(source);
        } catch (SQLException ex) {
            Logger.getLogger(BuildThermoForMoleculesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}