/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package substructures;

import java.io.IOException;
import java.util.Hashtable;
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
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.utilities.DetermineSymmetryAssignmentsFromConnections;
import thermo.test.GenerateStructures;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestDetermineSymmetryAssignments {

    public TestDetermineSymmetryAssignments() {
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
    public void testDetermineSymmetry() {
        Molecule ch3a = new Molecule();
        Molecule ch3b = new Molecule();
        Molecule ch3c = new Molecule();
        Molecule ch3d = new Molecule();
        
        Atom at1a = new Atom("C");
        Atom at1b = new Atom("C");
        Atom at1c = new Atom("C");
        Atom at1d = new Atom("C");
        ch3a.addAtom(at1a);
        ch3b.addAtom(at1b);
        ch3c.addAtom(at1c);
        ch3d.addAtom(at1d);
        
        Atom at2a = new Atom("C");
        Atom at3a = new Atom("H");
        Atom at4a = new Atom("H");
        Atom at2b = new Atom("H");
        Atom at3b = new Atom("H");
        Atom at4b = new Atom("H");
        Atom at2c = new Atom("H");
        Atom at3c = new Atom("H");
        Atom at4c = new Atom("H");
        Atom at2d = new Atom("H");
        Atom at3d = new Atom("H");
        Atom at4d = new Atom("C");

        ch3a.addAtom(at2a);
        ch3b.addAtom(at2b);
        ch3c.addAtom(at2c);
        ch3d.addAtom(at2d);
        
        ch3a.addAtom(at3a);
        ch3b.addAtom(at3b);
        ch3c.addAtom(at3c);
        ch3d.addAtom(at3d);

        ch3a.addAtom(at4a);
        ch3b.addAtom(at4b);
        ch3c.addAtom(at4c);
        ch3d.addAtom(at4d);

        Bond bnd1a = new Bond(at1a,at2a);
        Bond bnd2a = new Bond(at1a,at3a);
        Bond bnd3a = new Bond(at1a,at4a);
        ch3a.addBond(bnd1a);
        ch3a.addBond(bnd2a);
        ch3a.addBond(bnd3a);
        
        Bond bnd1b = new Bond(at1b,at2b);
        Bond bnd2b = new Bond(at1b,at3b);
        Bond bnd3b = new Bond(at1b,at4b);
        ch3b.addBond(bnd1b);
        ch3b.addBond(bnd2b);
        ch3b.addBond(bnd3b);
        
        Bond bnd1c = new Bond(at1c,at2c);
        Bond bnd2c = new Bond(at1c,at3c);
        Bond bnd3c = new Bond(at1c,at4c);
        ch3c.addBond(bnd1c);
        ch3c.addBond(bnd2c);
        ch3c.addBond(bnd3c);

        Bond bnd1d = new Bond(at1d,at2d);
        Bond bnd2d = new Bond(at1d,at3d);
        Bond bnd3d = new Bond(at1d,at4d);
        ch3d.addBond(bnd1d);
        ch3d.addBond(bnd2d);
        ch3d.addBond(bnd3d);
        
        Hashtable<String,IAtomContainer> connections = new Hashtable<String,IAtomContainer>();
        String ch3aS = "CH3A";
        String ch3bS = "CH3B";
        String ch3cS = "CH3C";
        String ch3dS = "CH3D";
        connections.put(ch3aS, ch3a);
        connections.put(ch3bS, ch3b);
        connections.put(ch3cS, ch3c);
        connections.put(ch3dS, ch3d);
                
        DetermineSymmetryAssignmentsFromConnections determine = new DetermineSymmetryAssignmentsFromConnections();
        try {
            determine.determineSymmetryAssignments(connections);
            System.out.println(determine.toString());
        } catch (CDKException ex) {
            Logger.getLogger(TestDetermineSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @Test
    public void testDetermineSymmetryPart2() throws CDKException{
        try {
            StructureAsCML ethaneCML1 = GenerateStructures.createEthane();
            StructureAsCML ethaneCML2 = GenerateStructures.createEthane();
            StructureAsCML ethaneCML3 = GenerateStructures.createEthane();
            
            StructureAsCML ch3choCML1 = GenerateStructures.createCH3CHO();
            StructureAsCML ch3choCML2 = GenerateStructures.createCH3CHO();
            
            StructureAsCML propaneCML1 = GenerateStructures.createPropane();
            
            Hashtable<String,IAtomContainer> connections = new Hashtable<String,IAtomContainer>();
            String key1 = "ethane1";
            String key2 = "ethane2";
            String key3 = "ethane3";
            String key4 = "ch3cho1";
            String key5 = "ch3cho2";
            String key6 = "propane1";
                    
            connections.put(key1, ethaneCML1.getMolecule());
            connections.put(key3, ethaneCML3.getMolecule());
            connections.put(key5, ch3choCML2.getMolecule());
            connections.put(key6, propaneCML1.getMolecule());
            connections.put(key2, ethaneCML2.getMolecule());
            connections.put(key4, ch3choCML1.getMolecule());
            
            DetermineSymmetryAssignmentsFromConnections determine = new DetermineSymmetryAssignmentsFromConnections();
            determine.determineSymmetryAssignments(connections);
            System.out.println(determine.toString());
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestDetermineSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestDetermineSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}