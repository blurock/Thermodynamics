/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.BensonGroupStructure;
import thermo.data.benson.BensonGroupStructuresFromMolecule;
import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.NormalizeMoleculeFromCMLStructure;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.SubstituteMetaAtom;
import thermo.test.GenerateStructures;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestBensonGroupStructuresFromMolecule {

    public TestBensonGroupStructuresFromMolecule() {
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
   public void testWithEthane() {
        try {
            StructureAsCML cmlstruct = GenerateStructures.createEthane();
            BensonGroupStructuresFromMolecule generate = new BensonGroupStructuresFromMolecule();
            
            List<BensonGroupStructure> structures = generate.deriveBensonGroupStructures(cmlstruct.getMolecule());
            printListOfBensonStructures("Ehtane",structures);
            
        } catch (CDKException ex) {
            Logger.getLogger(TestBensonGroupStructuresFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestBensonGroupStructuresFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestBensonGroupStructuresFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
   }
   @Test
   public void testWithCH3CHOWithoutCO() {
        try {
            StructureAsCML cmlstruct = GenerateStructures.createCH3CHO();
            BensonGroupStructuresFromMolecule generate = new BensonGroupStructuresFromMolecule();
            
            List<BensonGroupStructure> structures = generate.deriveBensonGroupStructures(cmlstruct.getMolecule());
            printListOfBensonStructures("CH3CHO without CO substitution",structures);
            
        } catch (CDKException ex) {
            Logger.getLogger(TestBensonGroupStructuresFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestBensonGroupStructuresFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestBensonGroupStructuresFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
   }
   @Test
   public void testWithCH3CHOWithCO() {
        try {
            StructureAsCML cmlstruct = GenerateStructures.createCH3CHO();
            
            
            StructureAsCML generalco = GenerateStructures.createGeneralKetone();
            MetaAtomDefinition carbonmeta = new MetaAtomDefinition("co",generalco);
            SubstituteMetaAtom substitute = new SubstituteMetaAtom(carbonmeta);
            NormalizeMoleculeFromCMLStructure norm = new NormalizeMoleculeFromCMLStructure();
            Molecule molecule = norm.getNormalizedMolecule(cmlstruct);

            substitute.substitute(molecule);
            
            BensonGroupStructuresFromMolecule generate = new BensonGroupStructuresFromMolecule();
            List<BensonGroupStructure> structures = generate.deriveBensonGroupStructures(molecule);
            printListOfBensonStructures("CH3CHO with CO substitution",structures);
            
        } catch (CDKException ex) {
            Logger.getLogger(TestBensonGroupStructuresFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestBensonGroupStructuresFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestBensonGroupStructuresFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
   }
   private void printListOfBensonStructures(String title, List<BensonGroupStructure> structures) {
            System.out.println("Benson Structures: " + title);
            Iterator<BensonGroupStructure> i = structures.iterator();
            while(i.hasNext()) {
                BensonGroupStructure struct = i.next();
                System.out.println(struct.toString());
            }

   }
}