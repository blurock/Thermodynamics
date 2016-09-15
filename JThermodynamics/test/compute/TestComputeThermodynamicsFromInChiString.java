/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package compute;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.exception.CDKException;
import thermo.compute.ComputeThermodynamicsFromMolecule;
import thermo.data.benson.DB.ThermoSQLConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.structure.structure.StructureAsCML;
import thermo.exception.ThermodynamicComputeException;

/**
 *
 * @author edwardblurock
 */
public class TestComputeThermodynamicsFromInChiString {

    public TestComputeThermodynamicsFromInChiString() {
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
    public void inchiAromatic() {
        InChIGeneratorFactory inchifactory = null;
            String inchi = "InChI=1S/C10H10/c1-2-6-10-8-4-3-7-9(10)5-1/h1-3,5-7H,4,8H2";
            System.out.println("InChI Aromatic: " + inchi);
            ThermoSQLConnection c = new ThermoSQLConnection();
            c.connect();
          try {
            if(inchifactory == null) {
                try {
                    inchifactory = InChIGeneratorFactory.getInstance();
                } catch (CDKException ex) {
                    Logger.getLogger(TestComputeThermodynamicsFromInChiString.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            InChIToStructure istruct = inchifactory.getInChIToStructure(inchi, DefaultChemObjectBuilder.getInstance());
            AtomContainer cont = new AtomContainer(istruct.getAtomContainer());
            System.out.println("CML version of InChI structure");
            StructureAsCML cmlstruct = new StructureAsCML(cont);

            System.out.println(cmlstruct.getCmlStructureString());

    }
        catch (CDKException ex) {
            Logger.getLogger(TestComputeThermodynamicsFromInChiString.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
    @Test
    public void computeFromInChiString() {
        try {
            String inchi = "InChI=1S/C10H10/c1-2-6-10-8-4-3-7-9(10)5-1/h1-3,5-7H,4,8H2";
            System.out.println(System.getProperty("os.name"));
            System.out.println(System.getProperty("os.arch"));
            System.out.println(System.getProperty("os.version"));
            ThermoSQLConnection c = new ThermoSQLConnection();
            c.connect();
            ComputeThermodynamicsFromMolecule compute = new ComputeThermodynamicsFromMolecule(c);
            System.out.println("Start computing");
            ThermodynamicInformation value = (ThermodynamicInformation) (SetOfBensonThermodynamicBase) compute.computeThermodynamicsFromInChI(inchi);
            System.out.println("Done");
            System.out.println(value.toString());
        } catch (ThermodynamicComputeException ex) {
            System.out.println("\nException");
            System.out.println(ex.toString());
            Logger.getLogger(TestComputeThermodynamicsFromInChiString.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    }