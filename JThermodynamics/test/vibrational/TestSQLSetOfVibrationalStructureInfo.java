/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vibrational;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.vibrational.DB.SQLSetOfVibrationalStructureInfo;
import thermo.data.structure.structure.vibrational.SetOfVibrationalStructureInfo;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestSQLSetOfVibrationalStructureInfo {

    public TestSQLSetOfVibrationalStructureInfo() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void test() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            SQLSetOfVibrationalStructureInfo sqlset = new SQLSetOfVibrationalStructureInfo(connect);

            SetOfVibrationalStructureInfo set = sqlset.getSetOfVibrationalStructureInfo();
            System.out.println(set.toString());
        } catch (SQLException ex) {
            Logger.getLogger(TestSQLSetOfVibrationalStructureInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}