/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.NormalizeMoleculeFromCMLStructure;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.SubstituteMetaAtom;
import thermo.test.GenerateStructures;

/**
 *
 * @author blurock
 */
public class TestMetaAtomSubstitution {

    public TestMetaAtomSubstitution() {
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
    public void TestSingleSubstitution() {
        try {
            System.out.println("--------------------- Substitute in c.sp3 --------------------------");
            StructureAsCML carbon = GenerateStructures.createGeneralCarbon();
            StructureAsCML ethane = GenerateStructures.createEthane();
            
            MetaAtomDefinition carbonmeta = new MetaAtomDefinition("c.sp3",carbon);
            System.out.println(carbonmeta.toString());
            
            SubstituteMetaAtom substitute = new SubstituteMetaAtom(carbonmeta);
            NormalizeMoleculeFromCMLStructure norm = new NormalizeMoleculeFromCMLStructure();
            AtomContainer molecule = norm.getNormalizedMolecule(ethane);

            substitute.substitute(molecule);
             StructureAsCML newcml = new StructureAsCML(molecule);
            System.out.println(newcml.getCmlStructureString());
           
            
        } catch (CDKException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    @Test
    public void TestCOSubstitution() {
        try {
            System.out.println("--------------------- Substitute in CO --------------------------");
            StructureAsCML generalco = GenerateStructures.createGeneralKetone();
            StructureAsCML ch3cho = GenerateStructures.createCH3CHO();
            
            MetaAtomDefinition carbonmeta = new MetaAtomDefinition("co",generalco);
            System.out.println(carbonmeta.toString());
            
            SubstituteMetaAtom substitute = new SubstituteMetaAtom(carbonmeta);
            NormalizeMoleculeFromCMLStructure norm = new NormalizeMoleculeFromCMLStructure();
            AtomContainer molecule = norm.getNormalizedMolecule(ch3cho);

            substitute.substitute(molecule);
            
            StructureAsCML newcml = new StructureAsCML(molecule);
            System.out.println(newcml.getCmlStructureString());
            
        } catch (CDKException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    @Test
        public void TestSubstituteMethyl() {
        try {
            System.out.println("--------------------- Substitute in c.sp3 into ethane--------------------------");
            StructureAsCML carbon = GenerateStructures.createMethyl();
            StructureAsCML ethane = GenerateStructures.createEthane();
            
            MetaAtomDefinition carbonmeta = new MetaAtomDefinition("ch3",carbon);
            System.out.println(carbonmeta.toString());
            
            SubstituteMetaAtom substitute = new SubstituteMetaAtom(carbonmeta);
            NormalizeMoleculeFromCMLStructure norm = new NormalizeMoleculeFromCMLStructure();
            AtomContainer molecule = norm.getNormalizedMolecule(ethane);

            substitute.substitute(molecule);
             StructureAsCML newcml = new StructureAsCML(molecule);
            System.out.println(newcml.getCmlStructureString());
           
            
        } catch (CDKException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    @Test
    public void TestSetOfMetaAtoms() {
           System.out.println("--------------------- Substitute in c and co  into ch3cho--------------------------");
         try {
            SetOfMetaAtomsForSubstitution set = new SetOfMetaAtomsForSubstitution();

            StructureAsCML generalco = GenerateStructures.createGeneralKetone();
            set.addDefinition("co", generalco);
            StructureAsCML carbon = GenerateStructures.createGeneralCarbon();
            set.addDefinition("c", carbon);

            StructureAsCML ch3cho = GenerateStructures.createCH3CHO();
            AtomContainer mol = set.substitute(ch3cho);

            StructureAsCML molcml = new StructureAsCML(mol);
            System.out.println(molcml.getCmlStructureString());
        } catch (CDKException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMetaAtomSubstitution.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}