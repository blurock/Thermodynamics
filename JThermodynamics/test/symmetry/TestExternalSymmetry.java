/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package symmetry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.symmetry.CalculateExternalSymmetryCorrection;
import thermo.exception.ThermodynamicException;

/**
 *
 * @author edwardblurock
 */
public class TestExternalSymmetry {

    public TestExternalSymmetry() {
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
    public void testExternalSymmetry() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();

            //StructureAsCML cmlstruct = GenerateStructures.createPropane();
            NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(connect);
            //Molecule mol = nancy.convert("ch2(c///ch)2");
            //Molecule mol = nancy.convert("ch3/ch2/ch2/ch3");
            //Molecule mol = nancy.convert("ch3/co/ch3");
            //Molecule mol = nancy.convert("ch3/ch2/ch2/ch2/ch3");
            //Molecule mol = nancy.convert("ch3/ch3");
            //Molecule mol = nancy.convert("ch3/c///c/ch3");
            //Molecule mol = nancy.convert("ch///c/ch3");
            //Molecule mol = nancy.convert("ch3/c(oh)3");
            //Molecule mol = nancy.convert("ch///c/c(oh)3");
            AtomContainer mol = nancy.convert("ch3/c(ch3)2/ch3");
            CalculateExternalSymmetryCorrection calculate = new CalculateExternalSymmetryCorrection(connect);
            SetOfBensonThermodynamicBase set = new SetOfBensonThermodynamicBase();
            calculate.calculate(mol, set);
            System.out.println(
                    "\n========================= Corrections =========================\n"
                    + set.toString()
                    + "========================= Corrections =========================");
        } catch (ThermodynamicException ex) {
            Logger.getLogger(TestExternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestExternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}