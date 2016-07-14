/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vibrational;

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
import thermo.data.structure.structure.AddHydrogenToSingleRadical;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.vibrational.CalculateVibrationalCorrectionForRadical;
import thermo.exception.NotARadicalException;
import thermo.test.GenerateStructures;
import static org.junit.Assert.*;

/**
 *
 * @author edwardblurock
 */
public class TestVibrationalCorrectionForRadical {

    public TestVibrationalCorrectionForRadical() {
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
    public void addHydrogen() throws SQLException {
        try {
            ThermoSQLConnection c = new ThermoSQLConnection();
            c.connect();
            String nancy = "ch3/ch2/ch2(.)";
            NancyLinearFormToMolecule nancyform = new NancyLinearFormToMolecule(c);
            Molecule molecule = nancyform.convert(nancy);
            StructureAsCML cmlstruct = new StructureAsCML(molecule);
            System.out.println(cmlstruct.getCmlStructureString());

            AddHydrogenToSingleRadical formRH = new AddHydrogenToSingleRadical();
            Molecule RH = formRH.convert(molecule);
            StructureAsCML cmlstructRH = new StructureAsCML(RH);
            System.out.println(cmlstructRH.getCmlStructureString());


        } catch (NotARadicalException ex) {
            Logger.getLogger(TestVibrationalCorrectionForRadical.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestVibrationalCorrectionForRadical.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Test
    public void calculateCorrection() {
        try {
            ThermoSQLConnection c = new ThermoSQLConnection();
            c.connect();
            String nancy = "ch3/ch2/ch2(.)";
            NancyLinearFormToMolecule nancyform = new NancyLinearFormToMolecule(c);
            Molecule molecule = nancyform.convert(nancy);
            StructureAsCML cmlstruct = new StructureAsCML(molecule);
            System.out.println(cmlstruct.getCmlStructureString());

            CalculateVibrationalCorrectionForRadical vibration = new CalculateVibrationalCorrectionForRadical(c);
            SetOfBensonThermodynamicBase corrections = vibration.calculate(molecule);
            System.out.println(corrections.toString());

        } catch (IOException ex) {
            Logger.getLogger(TestVibrationalCorrectionForRadical.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotARadicalException ex) {
            Logger.getLogger(TestVibrationalCorrectionForRadical.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestVibrationalCorrectionForRadical.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestVibrationalCorrectionForRadical.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

}