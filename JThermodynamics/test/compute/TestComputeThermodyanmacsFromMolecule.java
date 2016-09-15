/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package compute;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import thermo.compute.ComputeThermodynamicsFromMolecule;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.CML.CMLSetOfBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.NASAPolynomialFromBenson;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.structure.structure.StructureAsCML;
import thermo.exception.ThermodynamicComputeException;
import thermo.properties.ChemicalConstants;
import thermo.test.GenerateStructures;

/**
 *
 * @author blurock
 */
public class TestComputeThermodyanmacsFromMolecule {

    public TestComputeThermodyanmacsFromMolecule() {
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
    public void testSingleMolecule() {
        try {
            StringBuilder buf = new StringBuilder();
            //String nancy = "ch3/ch2/ch2/ch2/ch3";
            //String nancy = "ch2(.)/ch2/ch2/ch3";
            //String nancy = "ch3/c(ch3)2/ch2/c(ch3)2/ch2/c(ch3)2/ch2/ch(ch3)2";
            //String nancy = "ch3/c(#1)//ch/ch//ch/ch//ch/1";
            //String nancy = "ch3/ch(#1)/ch2/ch2/ch2/ch2/ch2/1";
            //String nancy = "c(ch3)4";
            //String nancy = "ch2(.)/ch2/ch3";
            //String nancy = "ch3/ch(.)/ch3";
            String nancy = "ch3/c(ch3)2/ch3";
            StructureAsCML cmlstruct = GenerateStructures.createPropane();
            ThermoSQLConnection c = new ThermoSQLConnection();
            c.connect();
            ComputeThermodynamicsFromMolecule compute = new ComputeThermodynamicsFromMolecule(c);
            //ThermodynamicInformation value = compute.computeThermodynamics(cmlstruct.getMolecule());
            ThermodynamicInformation value = (ThermodynamicInformation) (SetOfBensonThermodynamicBase) compute.computeThermodynamics(nancy);
            System.out.println(value.toString());

            CMLSetOfBensonThermodynamicBase cmlbenson = new CMLSetOfBensonThermodynamicBase();
            cmlbenson.setStructure(value);
            buf.append(cmlbenson.restore());
            System.out.println(buf.toString());

            NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) value);
            System.out.println("_________________");
            double e999 = nasa.computeEnthalpy(999.999);
            double diff = e999-nasa.computeEnthalpy(1000.0001);
            System.out.println(e999 + "\tDifference=\t" + diff);
            double cp999 = nasa.getHeatCapacity(999.999);
            double cpdiff = cp999-nasa.getHeatCapacity(1000.0001);
            System.out.println(cp999 + "\tDifference=\t" + cpdiff);
            double gas = ChemicalConstants.getGasConstantInCalsMolesK();
            for(double t=300;t<2300;t+=100) {

                double entropy = value.computeEntropy(t)/gas;
                double enthalpy = value.computeEnthalpy(t)*1000.0/(gas*t);
                double cp = value.getHeatCapacity(t)/gas;

                double Nentropy = nasa.computeEntropy(t)/gas - entropy;
                double Nenthalpy = nasa.computeEnthalpy(t)/(gas*t) - enthalpy;
                double Ncp = nasa.getHeatCapacity(t)/gas - cp;
                System.out.println(t + "\t" + cp + "\t" + enthalpy + "\t" + entropy+ "\t"
                        + Ncp + "\t" + Nenthalpy + "\t" + Nentropy);
            }
        } catch (ThermodynamicComputeException ex) {
            Logger.getLogger(TestComputeThermodyanmacsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestComputeThermodyanmacsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestComputeThermodyanmacsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestComputeThermodyanmacsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    public void testSingleMoleculeAsNancyLinearForm() {
        try {
            ThermoSQLConnection c = new ThermoSQLConnection();
            c.connect();
            ComputeThermodynamicsFromMolecule compute = new ComputeThermodynamicsFromMolecule(c);
            //String nancy = "ch3/c(ch3)//o";
            //String nancy =  "ch3/ch2/ch3";
            //String nancy = "ch3/ch(oh)/c///ch";
            //String nancy = "ch3/co/c///ch";
            //String nancy = "ch3/c(#1)&ch&ch&ch&ch&ch&1";
            String nancy = "ch3/c(ch3)2/ch2/ch(ch3)/ch3";
            ThermodynamicInformation value = compute.computeThermodynamics(nancy);
            System.out.println(value.toString());
            
        } catch (ThermodynamicComputeException ex) {
            Logger.getLogger(TestComputeThermodyanmacsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}