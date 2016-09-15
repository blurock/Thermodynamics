/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sql;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.AtomCounts;
import thermo.data.structure.DB.SQLAtomCounts;
import thermo.data.structure.structure.StructureAsCML;
import thermo.test.GenerateStructures;

/**
 *
 * @author edwardblurock
 */
public class TestAtomCounts {

    ThermoSQLConnection connect;

    StructureAsCML butane;
    StructureAsCML methylpropane;

    AtomCounts M1;
    AtomCounts M2;
    public TestAtomCounts() {
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
            butane = GenerateStructures.createFromSmiles("CCCC");
            methylpropane = GenerateStructures.createFromSmiles("CC(C)C");
            connect = new ThermoSQLConnection();
            if (!connect.connect())
                Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE,"Could not make SQL connection");
        } catch (CDKException ex) {
            Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void TestCreateTwoIsomerAtomCounts() {
        CreateTwoIsomerAtomCounts();
    }
    public void CreateTwoIsomerAtomCounts() {
        try {
            M1 = new AtomCounts(butane.getMolecule());
            String M1S = new String("M1");
            M1.setMoleculeID(M1S);
            System.out.println("Isomer M1: " + M1.isomerName());

            M2 = new AtomCounts(methylpropane.getMolecule());
            String M2S = new String("M2");
            M2.setMoleculeID(M2S);
            System.out.println("Isomer M2: " + M1.isomerName());

        } catch (CDKException ex) {
            Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void InsertAtomCountsInDatabase() {
        CreateTwoIsomerAtomCounts();
            try {
                SQLAtomCounts sqlcounts = new SQLAtomCounts(connect);
                System.out.println("Add AtomCounts M1");
                sqlcounts.addToDatabase(M1);
                System.out.println("Add AtomCounts M2");
                sqlcounts.addToDatabase(M2);
            } catch (SQLException ex) {
                Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    @Test
    public void RetrieveIsomersFromDatabase() {
        CreateTwoIsomerAtomCounts();
        try {
            SQLAtomCounts sqlcounts = new SQLAtomCounts(connect);
            String[] isomers = sqlcounts.findIsomersInDatabase(M1);

            for(int i=0; i<isomers.length;i++) {
                System.out.println(i + ": " + isomers[i]);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    public void DeleteIsomersFromDatabase() {
        try {
            CreateTwoIsomerAtomCounts();
            SQLAtomCounts sqlcounts = new SQLAtomCounts(connect);
            sqlcounts.deleteElement(M1);
            sqlcounts.deleteElement(M2);
        } catch (SQLException ex) {
            Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}