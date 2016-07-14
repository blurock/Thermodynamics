/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IBond.Order;
import thermo.data.structure.structure.NormalizeMoleculeFromCMLStructure;
import thermo.data.structure.structure.StructureAsCML;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestMoleculeNormalization {

    public TestMoleculeNormalization() {
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
    public void normalizationOfMethane() {
        
        
        System.out.println("=========================== Normalize Methane =============================");
        try {

            // Build Single Bonded Carbon
            Molecule mol = new Molecule();
            Atom at1 = new Atom("C");
            Atom at2 = new Atom("H");
            Atom at3 = new Atom("H");
            Atom at4 = new Atom("H");
            Atom at5 = new Atom("H");
            mol.addAtom(at1);
            mol.addAtom(at2);
            mol.addAtom(at3);
            mol.addAtom(at4);
            mol.addAtom(at5);

            Bond bnd1 = new Bond(at1, at2);
            Bond bnd2 = new Bond(at1, at3);
            Bond bnd3 = new Bond(at1, at4);
            Bond bnd4 = new Bond(at1, at5);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);
            mol.addBond(bnd4);

            StructureAsCML cml = new StructureAsCML(mol);
            NormalizeMoleculeFromCMLStructure normalize = new NormalizeMoleculeFromCMLStructure();

            Molecule normed = normalize.getNormalizedMolecule(cml);
        } catch (CDKException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OptionalDataException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    
    }
    public void normalizationOfMethaneWithoutHydrogens() {
        
        
        System.out.println("=========================== Normalize Carbon Without Hydrogens =============================");
        try {

            // Build Single Bonded Carbon
            Molecule mol = new Molecule();
            Atom at1 = new Atom("C");
            mol.addAtom(at1);

            StructureAsCML cml = new StructureAsCML(mol);
            NormalizeMoleculeFromCMLStructure normalize = new NormalizeMoleculeFromCMLStructure();

            Molecule normed = normalize.getNormalizedMolecule(cml);
        } catch (CDKException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OptionalDataException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    
    }
    @Test
    public void normalizationOfCOWithHydrogen() {
        
        
        System.out.println("=========================== Aldehyde=============================");
        try {

            // Build Single Bonded Carbon
            Molecule mol = new Molecule();
            Atom at1 = new Atom("C");
            Atom at2 = new Atom("O");
            Atom at3 = new Atom("H");
            Atom at4 = new Atom("H");
            mol.addAtom(at1);
            mol.addAtom(at2);
            mol.addAtom(at3);
            mol.addAtom(at4);

            Bond bnd1 = new Bond(at1, at2,Order.DOUBLE);
            Bond bnd2 = new Bond(at1, at3);
            Bond bnd3 = new Bond(at1, at4);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);

            StructureAsCML cml = new StructureAsCML(mol);
            NormalizeMoleculeFromCMLStructure normalize = new NormalizeMoleculeFromCMLStructure();

            Molecule normed = normalize.getNormalizedMolecule(cml);
        } catch (CDKException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OptionalDataException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    
    }
    @Test
    public void normalizationOfCOWithoutHydrogen() {
        
        
        System.out.println("=========================== CO without hydrogens =============================");
        try {

            // Build Single Bonded Carbon
            Molecule mol = new Molecule();
            Atom at1 = new Atom("C");
            Atom at2 = new Atom("O");
            mol.addAtom(at1);
            mol.addAtom(at2);

            Bond bnd1 = new Bond(at1, at2,Order.DOUBLE);
            mol.addBond(bnd1);

            StructureAsCML cml = new StructureAsCML(mol);
            NormalizeMoleculeFromCMLStructure normalize = new NormalizeMoleculeFromCMLStructure();

            Molecule normed = normalize.getNormalizedMolecule(cml);
        } catch (CDKException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OptionalDataException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    
    }
    @Test
    public void normalizationOfCarbonUnspecifiedAtoms() {
        try {
        System.out.println("=========================== Normalize Carbon single bond with Unspecified =============================");

            // Build Single Bonded Carbon
            Molecule mol = new Molecule();
            Atom at1 = new Atom("C");
            IsotopeFactory.getInstance(DefaultChemObjectBuilder.getInstance()).configure(at1);
            Atom at2 = new Atom("Du");
            Atom at3 = new Atom("Du");
            Atom at4 = new Atom("Du");
            Atom at5 = new Atom("Du");
            mol.addAtom(at1);
            mol.addAtom(at2);
            mol.addAtom(at3);
            mol.addAtom(at4);
            mol.addAtom(at5);

            Bond bnd1 = new Bond(at1, at2);
            Bond bnd2 = new Bond(at1, at3);
            Bond bnd3 = new Bond(at1, at4);
            Bond bnd4 = new Bond(at1, at5);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);
            mol.addBond(bnd4);

            StructureAsCML cml = new StructureAsCML(mol);
            NormalizeMoleculeFromCMLStructure normalize = new NormalizeMoleculeFromCMLStructure();

            Molecule normed = normalize.getNormalizedMolecule(cml);
        } catch (CDKException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OptionalDataException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    
    }
    @Test
    public void normalizationOfCarbonDoubleBondWithUnspecifiedAtoms() {
        try {
        System.out.println("=========================== Normalize Carbon Double bond with Unspecified =============================");

            // Build Single Bonded Carbon
            Molecule mol = new Molecule();
            Atom at1 = new Atom("C");
            IsotopeFactory.getInstance(DefaultChemObjectBuilder.getInstance()).configure(at1);
            Atom at2 = new Atom("Du");
            Atom at3 = new Atom("Du");
            Atom at4 = new Atom("Du");
            mol.addAtom(at1);
            mol.addAtom(at2);
            mol.addAtom(at3);
            mol.addAtom(at4);

            Bond bnd1 = new Bond(at1, at2,Order.DOUBLE);
            Bond bnd2 = new Bond(at1, at3);
            Bond bnd3 = new Bond(at1, at4);
           mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);

            StructureAsCML cml = new StructureAsCML(mol);
            NormalizeMoleculeFromCMLStructure normalize = new NormalizeMoleculeFromCMLStructure();

            Molecule normed = normalize.getNormalizedMolecule(cml);
        } catch (CDKException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OptionalDataException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    
    }
    @Test
    public void aromaticRingTest() {
         System.out.println("=========================== Normalize Aromatic Ring =============================");
       
        try {
                    Molecule mol = new Molecule();
            Atom atm1 = new Atom("N");
            Atom atm2 = new Atom("C");
            Atom atm3 = new Atom("C");
            Atom atm4 = new Atom("C");
            Atom atm5 = new Atom("C");
            Atom atm6 = new Atom("C");
            Atom atm7 = new Atom("C");
            mol.addAtom(atm1);
            mol.addAtom(atm2);
            mol.addAtom(atm3);
            mol.addAtom(atm4);
            mol.addAtom(atm5);
            mol.addAtom(atm6);
            mol.addAtom(atm7);
            Bond bnd1 = new Bond(atm1, atm2);
            Bond bnd2 = new Bond(atm2, atm3, Order.DOUBLE);
            Bond bnd3 = new Bond(atm3, atm4);
            Bond bnd4 = new Bond(atm4, atm5, Order.DOUBLE);
            Bond bnd5 = new Bond(atm5, atm6);
            Bond bnd6 = new Bond(atm6, atm1, Order.DOUBLE);
            Bond bnd7 = new Bond(atm1, atm7);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);
            mol.addBond(bnd4);
            mol.addBond(bnd5);
            mol.addBond(bnd6);
            
            StructureAsCML cml = new StructureAsCML(mol);
            NormalizeMoleculeFromCMLStructure normalize = new NormalizeMoleculeFromCMLStructure();

            Molecule normed = normalize.getNormalizedMolecule(cml);
        } catch (CDKException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OptionalDataException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    

    }
    @Test
    public void unspecifiedAromaticRingTest() {
         System.out.println("=========================== Normalize Aromatic Ring =============================");
       
        try {
                    Molecule mol = new Molecule();
            Atom atm1 = new Atom("C");
            Atom atm2 = new Atom("Du");
            Atom atm3 = new Atom("Du");
            Atom atm4 = new Atom("Du");
            Atom atm5 = new Atom("Du");
            Atom atm6 = new Atom("Du");
            Atom atm7 = new Atom("Du");
            mol.addAtom(atm1);
            mol.addAtom(atm2);
            mol.addAtom(atm3);
            mol.addAtom(atm4);
            mol.addAtom(atm5);
            mol.addAtom(atm6);
            mol.addAtom(atm7);
            Bond bnd1 = new Bond(atm1, atm2, Order.SINGLE);
            Bond bnd2 = new Bond(atm2, atm3, Order.DOUBLE);
            Bond bnd3 = new Bond(atm3, atm4, Order.SINGLE);
            Bond bnd4 = new Bond(atm4, atm5, Order.DOUBLE);
            Bond bnd5 = new Bond(atm5, atm6, Order.SINGLE);
            Bond bnd6 = new Bond(atm6, atm1, Order.DOUBLE);
            Bond bnd7 = new Bond(atm1, atm7, Order.SINGLE);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);
            mol.addBond(bnd4);
            mol.addBond(bnd5);
            mol.addBond(bnd6);
            
            StructureAsCML cml = new StructureAsCML(mol);
            NormalizeMoleculeFromCMLStructure normalize = new NormalizeMoleculeFromCMLStructure();

            Molecule normed = normalize.getNormalizedMolecule(cml);
        } catch (CDKException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OptionalDataException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMoleculeNormalization.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    

    }
}