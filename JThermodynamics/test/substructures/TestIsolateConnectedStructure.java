/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package substructures;

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
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.IsolateConnectedStructure;
import thermo.test.GenerateStructures;

/**
 *
 * @author blurock
 */
public class TestIsolateConnectedStructure {

    /**
     * 
     */
    public TestIsolateConnectedStructure() {
    }

    /**
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * 
     */
    @Before
    public void setUp() {
    }

    /**
     * 
     */
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    /**
     * 
     */
    @Test
    public void testWithMethane() {
        try {
            StructureAsCML methaneCML =  GenerateStructures.createGeneralCarbon();
            IAtomContainer methane = methaneCML.getMolecule();
            IsolateConnectedStructure isolate = new IsolateConnectedStructure();
            IAtom carbon = methane.getAtom(0);
            IAtom connected = methane.getAtom(1);
            IAtomContainer substructure = isolate.IsolateConnectedStructure(methane, carbon, connected);
            System.out.println("*#Atoms: " + substructure.getAtomCount() + ", #Bonds: " + substructure.getBondCount());
            System.out.println(substructure.toString());
        } catch (CDKException ex) {
            Logger.getLogger(TestIsolateConnectedStructure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestIsolateConnectedStructure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestIsolateConnectedStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 
     */
    @Test
    public void testEthane() {
        try {
            StructureAsCML ethaneCML =  GenerateStructures.createEthane();
            IAtomContainer ethane = ethaneCML.getMolecule();
            IsolateConnectedStructure isolate = new IsolateConnectedStructure();
            IAtom firstcarbon = ethane.getAtom(0);
            IAtom secondcarbon = ethane.getAtom(1);
            IAtomContainer substructure = isolate.IsolateConnectedStructure(ethane, firstcarbon, secondcarbon);
            System.out.println("*#Atoms: " + substructure.getAtomCount() + ", #Bonds: " + substructure.getBondCount());
            System.out.println(substructure.toString());
        } catch (CDKException ex) {
            Logger.getLogger(TestIsolateConnectedStructure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestIsolateConnectedStructure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestIsolateConnectedStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 
     */
    @Test
    public void testPropane() {
        try {
            StructureAsCML propaneCML =  GenerateStructures.createPropane();
            IAtomContainer propane = propaneCML.getMolecule();
            IsolateConnectedStructure isolate = new IsolateConnectedStructure();
            IAtom firstcarbon = propane.getAtom(0);
            IAtom secondcarbon = propane.getAtom(1);
            IAtomContainer substructure = isolate.IsolateConnectedStructure(propane, firstcarbon, secondcarbon);
            System.out.println("*#Atoms: " + substructure.getAtomCount() + ", #Bonds: " + substructure.getBondCount());
            AtomContainer mol = new AtomContainer(substructure);
            StructureAsCML cmlsub = new StructureAsCML(mol);
            System.out.println(cmlsub.getCmlStructureString());
        } catch (CDKException ex) {
            Logger.getLogger(TestIsolateConnectedStructure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestIsolateConnectedStructure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestIsolateConnectedStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}