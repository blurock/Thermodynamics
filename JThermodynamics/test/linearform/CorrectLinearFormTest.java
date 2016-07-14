/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package linearform;

import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.CorrectLinearForm;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class CorrectLinearFormTest {

    CorrectLinearForm correct;
    public CorrectLinearFormTest() throws SQLException {
        ThermoSQLConnection connect = new ThermoSQLConnection();
        if(connect.connect()) {
            correct = new CorrectLinearForm(connect);
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private void correctLinearForm(String nancy) {
        String corrected = correct.correctNancyLinearForm(nancy);
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Original<" + nancy + "> transformed to <" + corrected + ">");
        System.out.println("-------------------------------------------------------------------------");
    }
    @Test
    public void test() {
        System.out.println(correct.toString());
        correctLinearForm("ch4");
        correctLinearForm("coh2");
        correctLinearForm("'co'h2");
        correctLinearForm("ono");
        correctLinearForm("no2f");
        correctLinearForm("no3/ch3");
    }

}