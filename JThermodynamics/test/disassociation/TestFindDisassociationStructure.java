/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package disassociation;

import java.sql.SQLException;
import java.util.List;
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
import thermo.data.structure.disassociation.DB.SQLDisassociationEnergy;
import thermo.data.structure.linearform.NancyLinearFormToGeneralStructure;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.substructure.FindSubstructure;
import static org.junit.Assert.*;

/**
 *
 * @author edwardblurock
 */
public class TestFindDisassociationStructure {

    public TestFindDisassociationStructure() {
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
    public void testDisassociationStructure() {
        try {
            ThermoSQLConnection connection = new ThermoSQLConnection();
            connection.connect();
            NancyLinearFormToMolecule nancy = new NancyLinearFormToGeneralStructure(connection);
            //String nancyS = "ch2(.)ch2/ch2/ch2/ch2/ch3";
            String nancyS = "ch(.)//ch/ch2/ch2/ch2/ch3";
            //String nancyS = "ch3(.)";
            Molecule mol = nancy.convert(nancyS);
            FindSubstructure find = new FindSubstructure(mol, connection);
            SQLDisassociationEnergy sqldiss = new SQLDisassociationEnergy(connection);
            List<String> names = sqldiss.listOfDisassociationStructures();
            String name = find.findLargestSubstructure(names);
            System.out.println("The substructure of " + nancyS + " is " + name);

        } catch (CDKException ex) {
            Logger.getLogger(TestFindDisassociationStructure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestFindDisassociationStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}