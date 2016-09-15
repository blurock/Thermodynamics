/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gauche;

import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.linearform.NancyLinearFormToGeneralStructure;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.gauche.ComputeGaucheInteractions;
import static org.junit.Assert.*;

/**
 *
 * @author edwardblurock
 */
public class GaucheTest {

    public GaucheTest() {
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
    public void firstTest() {
        try {
            ThermoSQLConnection connection = new ThermoSQLConnection();
            connection.connect();
            ComputeGaucheInteractions gauche = new ComputeGaucheInteractions(connection);

            NancyLinearFormToMolecule nancyFormToMolecule = new NancyLinearFormToGeneralStructure(connection);
            String moleculeS = "ch3/c(ch3)2/ch2/c(ch3)2/ch2/c(ch3)2/ch2/ch(ch3)2";
            AtomContainer molecule = nancyFormToMolecule.convert(moleculeS);

            SetOfBensonThermodynamicBase thermo = new SetOfBensonThermodynamicBase();

            gauche.compute(molecule, thermo);
            System.out.println(thermo.toString());

        } catch (SQLException ex) {
            Logger.getLogger(GaucheTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(GaucheTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}