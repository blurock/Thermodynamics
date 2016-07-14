package cml;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IBond.Order;
import thermo.data.structure.structure.StructureAsCML;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestStructureAsCML {

    public TestStructureAsCML() {
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
    public void CMLStructureString() {
    
        Molecule mol = new Molecule();
        mol.setID("Aldehyde");
        Atom at1 = new Atom("C");
        Atom at2 = new Atom("H");
        Atom at3 = new Atom("H");
        Atom at4 = new Atom("O");
        Bond bnd1 = new Bond(at1, at2);
        Bond bnd2 = new Bond(at1, at3);
        Bond bnd3 = new Bond(at1, at4,Order.DOUBLE);
        mol.addAtom(at1);
        mol.addAtom(at2);
        mol.addAtom(at3);
        mol.addAtom(at4);
        mol.addBond(bnd1);
        mol.addBond(bnd2);
        mol.addBond(bnd3);
            
            
            StructureAsCML cml = null;
        try {
            System.out.println("Transform Aldehyde to CML string");
            cml = new StructureAsCML(mol);
            System.out.println(cml.getCmlStructureString());
            
            System.out.println("From CML string to Molecule");
            StructureAsCML cml1 = new StructureAsCML("Aldehyde", cml.getCmlStructureString());
            Molecule mol1 = cml.getMolecule();
        System.out.println("The molecule retrieved: " + mol1.getID());
        System.out.println("Number of Atoms:        " + mol1.getAtomCount());
        System.out.println("Number of Bonds:        " + mol1.getBondCount());
            
        } catch (CDKException ex) {
            Logger.getLogger(TestStructureAsCML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}