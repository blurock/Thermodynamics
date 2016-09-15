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
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.vibrational.SetOfVibrationalStructureCounts;
import thermo.data.structure.structure.vibrational.SubstituteVibrationalStructures;

/**
 *
 * @author blurock
 */
public class SetOfVibrationalCountsSubtract {

    public SetOfVibrationalCountsSubtract() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void substractButeneAndButeneRadical() {
        try {
            System.out.println("==  Butene and Butene Radical Subtraction Test  ==============");
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            NancyLinearFormToMolecule nancylinear = new NancyLinearFormToMolecule(connect);
            AtomContainer butene = nancylinear.convert("ch2//ch/ch2/ch3");
            AtomContainer buteneradical = nancylinear.convert("ch(.)//ch/ch2/ch3");

            SubstituteVibrationalStructures substitute = new SubstituteVibrationalStructures(connect);
            SetOfVibrationalStructureCounts buteneradicalcounts = substitute.findSubstitutions(buteneradical);
            System.out.println("Butene Radical Counts: " + buteneradicalcounts.toString());
            SetOfVibrationalStructureCounts butenecounts = substitute.findSubstitutions(butene);
            System.out.println("Butene Counts:         " + butenecounts.toString());
            
            butenecounts.subtract(buteneradicalcounts);
            System.out.println("Difference: " + butenecounts.toString());
        } catch (CDKException ex) {
            Logger.getLogger(TestSubstituteVibrationalStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestSubstituteVibrationalStructures.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println("==============================================================");
    }

}