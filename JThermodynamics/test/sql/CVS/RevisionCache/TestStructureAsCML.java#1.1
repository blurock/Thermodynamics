/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sql;

import java.io.IOException;
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
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.structure.StructureAsCML;
import thermo.test.GenerateStructures;
import static org.junit.Assert.*;

/**
 *
 * @author edwardblurock
 */
public class TestStructureAsCML {

    public TestStructureAsCML() {
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
    public void addStructure() {
        try {
            StructureAsCML cmlstruct = GenerateStructures.createMethyl();
            cmlstruct.setNameOfStructure("Test");
            ThermoSQLConnection connect = new ThermoSQLConnection();
            if (connect.connect()) {
                SQLStructureAsCML sqlstruct = new SQLStructureAsCML(connect);
                sqlstruct.deleteByKey(cmlstruct.getNameOfStructure());
                sqlstruct.addToDatabase(cmlstruct);
                }
        } catch (SQLException ex) {
            Logger.getLogger(TestStructureAsCML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestStructureAsCML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestStructureAsCML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestStructureAsCML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}