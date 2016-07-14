/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package disassociation;

import java.io.File;
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
import thermo.build.ReadDisassociationData;
import thermo.data.benson.DB.ThermoSQLConnection;
import static org.junit.Assert.*;
import thermo.exception.ThermodynamicException;

/**
 *
 * @author edwardblurock
 */
public class TestReadDisassociationEnergy {

    public TestReadDisassociationEnergy() {
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
    public void readDisassociationEnergy() throws SQLException {
        try {
            String filenameS = "disassociation.don";
            File filenameF = new File(filenameS);
            String sourceS = "Standard";
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            ReadDisassociationData read = new ReadDisassociationData(connect);

                read.build(filenameF, sourceS);

        } catch (CDKException ex) {
            Logger.getLogger(TestReadDisassociationEnergy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestReadDisassociationEnergy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestReadDisassociationEnergy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ThermodynamicException ex) {
                Logger.getLogger(TestReadDisassociationEnergy.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

}