/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package symmetry;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.Molecule;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.symmetry.CalculateSymmetryCorrection;
import static org.junit.Assert.*;
import thermo.exception.ThermodynamicException;

/**
 *
 * @author edwardblurock
 */
public class TestSymmetryCorrections {

    public TestSymmetryCorrections() {
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
    public void symmetryCorrections() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            //StructureAsCML cmlstruct = GenerateStructures.createPropane();
            NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(connect);
            //Molecule mol = nancy.convert("ch(c///ch)(ch3)(oh)");
            //Molecule mol = nancy.convert("ch2(c///ch)2");
            //Molecule mol = nancy.convert("ch3/ch2/ch3");
            //Molecule mol = nancy.convert("ch3/ch2/ch2/ch2/ch3");
            //Molecule mol = nancy.convert("ch3/ch3");
            //Molecule mol = nancy.convert("ch3/c///c/ch3");
            //Molecule mol = nancy.convert("ch///c/ch3");
            //Molecule mol = nancy.convert("ch3/c(oh)3");
            //Molecule mol = nancy.convert("ch///c/c(oh)3");
            Molecule mol = nancy.convert("ch3/ch(oh)/c///ch");
            //Molecule mol = nancy.convert("ch3/ch2/ch3");
            CalculateSymmetryCorrection symcorrection = new CalculateSymmetryCorrection(connect);
            SetOfBensonThermodynamicBase set = symcorrection.calculate(mol);
            System.out.println(
                    "\n========================= Corrections =========================\n"
                    + set.toString()
                    + "========================= Corrections =========================");
        } catch (ThermodynamicException ex) {
            Logger.getLogger(TestSymmetryCorrections.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestSymmetryCorrections.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

}