/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.test.GenerateStructures;
import static org.junit.Assert.*;

/**
 *
 * @author edwardblurock
 */
public class TestSQLMolecule {
    ThermoSQLConnection connect;

    Molecule butane;
    Molecule methylpropane;

    public TestSQLMolecule() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        try {
            StructureAsCML butaneCML = GenerateStructures.createFromSmiles("CCCC");
            StructureAsCML methylpropaneCML = GenerateStructures.createFromSmiles("CC(C)C");
            butane = butaneCML.getMolecule();
            methylpropane = methylpropaneCML.getMolecule();
            butane.setID("TestButane");
            methylpropane.setID("TestMethylPropane");
        } catch (CDKException ex) {
            Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void addIsomersToDatabase() {
        try {
            connect = new ThermoSQLConnection();
            if (!connect.connect())
               Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE,"Could not make SQL connection");
            Statement statement = connect.createStatement();
            String datm = "DELETE FROM AtomCounts;";
            statement.executeUpdate(datm);
            String dmol = "DELETE FROM DatabaseMolecule;";
            statement.executeUpdate(dmol);

            SQLMolecule molecule = new SQLMolecule(connect);
            String src = "Test";
            molecule.addToDatabase(butane, src);
            molecule.addToDatabase(methylpropane, src);
        } catch (SQLException ex) {
            Logger.getLogger(TestSQLMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestSQLMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Test
    public void findMoleculeMatchInDatabase() {
        try {
            connect = new ThermoSQLConnection();
            if (!connect.connect())
               Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE,"Could not make SQL connection");
            SQLMolecule sqlmolecule = new SQLMolecule(connect);
            String findbutane = sqlmolecule.findInDatabase(butane);
            System.out.println("Should have found Butane: " + findbutane);
            String findmethylpropane = sqlmolecule.findInDatabase(methylpropane);
            System.out.println("Should have found Methylpropane: " + findmethylpropane);
        } catch (SQLException ex) {
            Logger.getLogger(TestSQLMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestSQLMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    public void deleteTestMoleculesCase() {
        try {
            connect = new ThermoSQLConnection();
            if (!connect.connect()) {
                Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE, "Could not make SQL connection");
            }
            SQLMolecule sqlmolecule = new SQLMolecule(connect);
            String src = "Test";

            String[] fromsrc = sqlmolecule.findMoleculesOfSource(src);
            for(int i=0;i<fromsrc.length;i++) {
                System.out.println("From Test Source(" + i + "): " + fromsrc[i]);
            }

            sqlmolecule.deleteFromSource(src);
        } catch (SQLException ex) {
            Logger.getLogger(TestSQLMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}