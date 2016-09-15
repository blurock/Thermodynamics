/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package symmetry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.CML.CMLListOfSymmetryPairs;
import thermo.data.structure.structure.symmetry.CalculateOpticalSymmetryCorrection;
import thermo.data.structure.structure.symmetry.DetermineSymmetryFromSingleDefinition;
import thermo.data.structure.structure.symmetry.DetermineTotalOpticalSymmetry;
import thermo.data.structure.structure.symmetry.ListOfSymmetryPairs;
import thermo.data.structure.structure.symmetry.OpticalSymmetry;
import thermo.data.structure.structure.symmetry.SetOfSymmetryDefinitions;
import thermo.data.structure.structure.symmetry.SymmetryPair;
import thermo.exception.ThermodynamicException;
import thermo.test.GenerateStructures;

/**
 *
 * @author blurock
 */
public class TestOpticalSymmetry {

    public TestOpticalSymmetry() {
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
    public void assymmetricCarbon() {
        try {
            // Assymetric
            String opticalS = new String("Optical"); 
            // General Carbon (carbon atom with 4 unspecified atoms)
            StructureAsCML struct = GenerateStructures.createGeneralCarbon();
            System.out.println(struct.getCmlStructureString());
            // All four connections different for optical symmetry
            SymmetryPair pair1 = new SymmetryPair("Group1", "a2");
            SymmetryPair pair2 = new SymmetryPair("Group2", "a3");
            SymmetryPair pair3 = new SymmetryPair("Group3", "a4");
            SymmetryPair pair4 = new SymmetryPair("Group4", "a5");
            
            ListOfSymmetryPairs pairlist = new ListOfSymmetryPairs();
            pairlist.add(pair1);
            pairlist.add(pair2);
            pairlist.add(pair3);
            pairlist.add(pair4);
            
            CMLListOfSymmetryPairs cmlpairs = new CMLListOfSymmetryPairs();
            cmlpairs.setStructure(pairlist);
            String cml = cmlpairs.restore();
            CMLListOfSymmetryPairs cmlpairs1 = new CMLListOfSymmetryPairs();
            cmlpairs1.parse(cml);
            System.out.println(cmlpairs1.toString());
            
            OpticalSymmetry optical = new OpticalSymmetry(opticalS,struct,pairlist);
            
            SetOfSymmetryDefinitions setofdefs = new SetOfSymmetryDefinitions();
            setofdefs.add(optical);
            
            DetermineSymmetryFromSingleDefinition singledef = new DetermineSymmetryFromSingleDefinition();
            
            DetermineTotalOpticalSymmetry total = new DetermineTotalOpticalSymmetry(singledef, setofdefs);
            
            String smiles = "[H]OC([H])(Cl)Br";
            //String smiles = "CCC(C)O[H]";
            StructureAsCML testmol = GenerateStructures.createFromSmiles(smiles);
            System.out.println(testmol.getCmlStructureString());
            AtomContainer molecule = testmol.getMolecule();
            int opticalsymmetry = total.determineSymmetry(molecule);
            System.out.println("Total Symmetry of  " + smiles + " is " +  opticalsymmetry);
        } catch (ValidityException ex) {
            Logger.getLogger(TestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParsingException ex) {
            Logger.getLogger(TestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @Test
    public void testOpticalCorrection() {
        try {
            String smiles = "[H]OC([H])(Cl)Br";
            //String smiles = "CCC(C)O[H]";
            StructureAsCML testmol = GenerateStructures.createFromSmiles(smiles);
            System.out.println(testmol.getCmlStructureString());
            AtomContainer molecule = testmol.getMolecule();

            ThermoSQLConnection connect= new ThermoSQLConnection();
            connect.connect();
            CalculateOpticalSymmetryCorrection calc = new CalculateOpticalSymmetryCorrection(connect);

            SetOfBensonThermodynamicBase set = new SetOfBensonThermodynamicBase();
            calc.calculate(molecule,set);

            System.out.println(set.toString());


        } catch (ThermodynamicException ex) {
            Logger.getLogger(TestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}