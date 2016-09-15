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
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLMolecule;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import static org.junit.Assert.*;

/**
 *
 * @author edwardblurock
 */
public class FindMoleculeInDatabase {

    public FindMoleculeInDatabase() {
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
    public void find() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            if (!connect.connect())
               Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.SEVERE,"Could not make SQL connection");
            SQLMolecule sqlmolecule = new SQLMolecule(connect);

            String molS = "ch3/ch2/ch2/ch3";
            NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(connect);
            AtomContainer molecule = nancy.convert(molS);
            String findmolecule = sqlmolecule.findInDatabase(molecule);
            System.out.println("Should have found Butane: " + findmolecule);
        } catch (SQLException ex) {
            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

}