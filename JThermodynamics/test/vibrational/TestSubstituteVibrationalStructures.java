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
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.vibrational.SetOfVibrationalStructureCounts;
import thermo.data.structure.structure.vibrational.SubstituteVibrationalStructures;
import thermo.test.GenerateStructures;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestSubstituteVibrationalStructures {

    public TestSubstituteVibrationalStructures() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testButene() {
        try {
            System.out.println("======================== Butene  Test=========================");
            StructureAsCML cmlstruct = GenerateStructures.createFromSmiles("C=CCC");
            System.out.println(cmlstruct.getCmlStructureString());
            
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            SubstituteVibrationalStructures substitute = new SubstituteVibrationalStructures(connect);
            
            SetOfVibrationalStructureCounts counts = substitute.findSubstitutions(cmlstruct.getMolecule());
            System.out.println(counts.toString());
            
        } catch (SQLException ex) {
            Logger.getLogger(TestSubstituteVibrationalStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestSubstituteVibrationalStructures.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println("==============================================================");
        
    }
    /**
     * 
     */
    @Test
    public void testButeneRadical() {
        try {
            System.out.println("============  Butane Radical Count Test  =====================");
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            NancyLinearFormToMolecule nancylinear = new NancyLinearFormToMolecule(connect);
            Molecule buteneradical = nancylinear.convert("ch2(.)/ch2/ch2/ch3");
            StructureAsCML cmlstruct = new StructureAsCML(buteneradical);
            System.out.println(cmlstruct.getCmlStructureString());

            SubstituteVibrationalStructures substitute = new SubstituteVibrationalStructures(connect);
            SetOfVibrationalStructureCounts counts = substitute.findSubstitutions(buteneradical);
            System.out.println(counts.toString());
        } catch (CDKException ex) {
            Logger.getLogger(TestSubstituteVibrationalStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestSubstituteVibrationalStructures.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println("==============================================================");
    }
}