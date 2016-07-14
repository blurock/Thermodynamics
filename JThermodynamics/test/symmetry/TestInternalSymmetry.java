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
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.CalculateInternalSymmetryCorrection;
import thermo.exception.ThermodynamicException;
import thermo.test.GenerateStructures;
import static org.junit.Assert.*;

/**
 *
 * @author edwardblurock
 */
public class TestInternalSymmetry {

    public TestInternalSymmetry() {
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
    public void TestInternalSymmetry() {
        try {
            StructureAsCML cmlstruct = GenerateStructures.createPropane();
            Molecule propane = cmlstruct.getMolecule();


            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            CalculateInternalSymmetryCorrection calculate = new CalculateInternalSymmetryCorrection(connect);

            SetOfBensonThermodynamicBase set = new SetOfBensonThermodynamicBase();
            calculate.calculate(propane,set);

            System.out.println("\n========================= Corrections =========================\n" +
                    set.toString()
                             + "========================= Corrections =========================");



        } catch (ThermodynamicException ex) {
            Logger.getLogger(TestInternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestInternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestInternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestInternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Test
    public void TestInternalSymmetryOfMethylPropane() {
        try {
            StructureAsCML cmlstruct = GenerateStructures.create2methylpropane();
            Molecule propane2m = cmlstruct.getMolecule();
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            CalculateInternalSymmetryCorrection calculate = new CalculateInternalSymmetryCorrection(connect);
            SetOfBensonThermodynamicBase set = new SetOfBensonThermodynamicBase();
            calculate.calculate(propane2m, set);
            System.out.println("\n========================= Corrections =========================\n" + set.toString() + "========================= Corrections =========================");
        } catch (CDKException ex) {
            Logger.getLogger(TestInternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ThermodynamicException ex) {
            Logger.getLogger(TestInternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    public void TestInternalSymmetryOfMethylPropaneWithNancyForm() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(connect);
            //Molecule mol = nancy.convert("ch3/ch(ch3)/ch3");
            //Molecule mol = nancy.convert("ch3/ch2/ch2/ch2/ch3");
            //Molecule mol = nancy.convert("ch3/ch2/ch3");
            Molecule mol = nancy.convert("ch2(.)/ch3");
            CalculateInternalSymmetryCorrection calculate = new CalculateInternalSymmetryCorrection(connect);
            SetOfBensonThermodynamicBase set = new SetOfBensonThermodynamicBase();
            calculate.calculate(mol, set);
            System.out.println("\n========================= Corrections =========================\n" + set.toString() + "========================= Corrections =========================");
        } catch (SQLException ex) {
            Logger.getLogger(TestInternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ThermodynamicException ex) {
            Logger.getLogger(TestInternalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}