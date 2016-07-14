/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package substructures;

import java.util.ArrayList;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.openscience.cdk.exception.CDKException;
import java.util.List;
import thermo.data.structure.disassociation.DB.SQLDisassociationEnergy;
import thermo.data.structure.substructure.FindSubstructure;
import org.openscience.cdk.Molecule;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.linearform.NancyLinearFormToGeneralStructure;
import thermo.data.benson.DB.ThermoSQLConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author edwardblurock
 */
public class TestFindSubstructure {

    public TestFindSubstructure() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testSubStructure() {
        try {
            ThermoSQLConnection connection = new ThermoSQLConnection();
            connection.connect();
            NancyLinearFormToMolecule nancy = new NancyLinearFormToGeneralStructure(connection);
            //String nancyS = "ch2(.)ch2/ch2/ch2/ch2/ch3";
            String nancyS = "ch(.)/ch2/ch2/ch2/ch2/ch3";
            //String nancyS = "ch3(.)";
            Molecule mol = nancy.convert(nancyS);
            FindSubstructure find = new FindSubstructure(mol, connection);
            //SQLDisassociationEnergy sqldiss = new SQLDisassociationEnergy(connection);
            //List<String> names = sqldiss.listOfDisassociationStructures();
            ArrayList<String> names = new ArrayList<String>();
            names.add("n-c3h7(*)");
            String name = find.findLargestSubstructure(names);
            System.out.println("The substructure of " + nancyS + " is " + name);

        } catch (CDKException ex) {
            Logger.getLogger(TestFindSubstructure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestFindSubstructure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}