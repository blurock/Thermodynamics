/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package substructures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.utilities.DetermineSetOfSymmetryAssignments;
import thermo.data.structure.structure.symmetry.SetOfSymmetryAssignments;
import thermo.data.structure.structure.symmetry.SetOfSymmetryMatches;
import thermo.data.structure.structure.symmetry.SymmetryPair;
import thermo.data.structure.structure.symmetry.utilities.DetermineSymmetryAssignmentsFromConnections;
import thermo.test.GenerateStructures;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestFindAllSetsOfSymmetryAssignments {

    public TestFindAllSetsOfSymmetryAssignments() {
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
    public void TestWithEthane() {
        try {
            StructureAsCML ethane = GenerateStructures.createEthane();
            Molecule mol = new Molecule();
            Atom atm1 = new Atom("C");
            Atom atm2 = new Atom("Du");
            Atom atm3 = new Atom("Du");
            Atom atm4 = new Atom("Du");
            mol.addAtom(atm1);
            mol.addAtom(atm2);
            mol.addAtom(atm3);
            mol.addAtom(atm4);
            
            Bond bnd1 = new Bond(atm1,atm2);
            Bond bnd2 = new Bond(atm1,atm3);
            Bond bnd3 = new Bond(atm1,atm4);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);
            StructureAsCML molCML = new StructureAsCML(mol);
            
            ArrayList<SymmetryPair> pairlist = new ArrayList<SymmetryPair>();
            SymmetryPair pair1 = new SymmetryPair("Group1","a2");
            SymmetryPair pair2 = new SymmetryPair("Group1","a3");
            SymmetryPair pair3 = new SymmetryPair("Group1","a4");
            //SymmetryPair pair4 = new SymmetryPair("Group2","a5");
            pairlist.add(pair1);
            pairlist.add(pair2);
            pairlist.add(pair3);
            //pairlist.add(pair4);
            String symname = "MethylGroup";
            DetermineSymmetryAssignmentsFromConnections assign = new DetermineSymmetryAssignmentsFromConnections();
            DetermineSetOfSymmetryAssignments determine = new DetermineSetOfSymmetryAssignments(symname, molCML, pairlist,assign);
            System.out.println("--------------------------------------------------------");
            System.out.println(determine.toString());
            Molecule e = ethane.getMolecule();
            List<SetOfSymmetryAssignments> sets = determine.findAllSetsOfSymmetryAssignments(e);
            
            Iterator<SetOfSymmetryAssignments> i = sets.iterator();
            while(i.hasNext()) {
                SetOfSymmetryAssignments assignments = i.next();
                System.out.println("========================================================");
                System.out.println(assignments.toString());
            }
            System.out.println("____________________________________________________");
            SetOfSymmetryMatches finalmatches = determine.findIfMatchInStructures(e);
            System.out.println(finalmatches.toString());
             System.out.println("____________________________________________________");
            
        } catch (CDKException ex) {
            Logger.getLogger(TestFindAllSetsOfSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestFindAllSetsOfSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestFindAllSetsOfSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * 
     */
    @Test
    public void TestWithc4h10() {
        try {
            StructureAsCML c4h10 = GenerateStructures.create2methylpropane();
            StructureAsCML ethane = GenerateStructures.createEthane();
            Molecule mol = new Molecule();
            Atom atm1 = new Atom("C");
            Atom atm2 = new Atom("Du");
            Atom atm3 = new Atom("Du");
            Atom atm4 = new Atom("Du");
            mol.addAtom(atm1);
            mol.addAtom(atm2);
            mol.addAtom(atm3);
            mol.addAtom(atm4);
            
            Bond bnd1 = new Bond(atm1,atm2);
            Bond bnd2 = new Bond(atm1,atm3);
            Bond bnd3 = new Bond(atm1,atm4);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);
            StructureAsCML molCML = new StructureAsCML(mol);
            
            ArrayList<SymmetryPair> pairlist = new ArrayList<SymmetryPair>();
            SymmetryPair pair1 = new SymmetryPair("Group1","a2");
            SymmetryPair pair2 = new SymmetryPair("Group1","a3");
            SymmetryPair pair3 = new SymmetryPair("Group1","a4");
            //SymmetryPair pair4 = new SymmetryPair("Group2","a5");
            pairlist.add(pair1);
            pairlist.add(pair2);
            pairlist.add(pair3);
            //pairlist.add(pair4);
            String symname = "MethylGroup";
            DetermineSymmetryAssignmentsFromConnections assign = new DetermineSymmetryAssignmentsFromConnections();
            DetermineSetOfSymmetryAssignments determine = new DetermineSetOfSymmetryAssignments(symname, molCML, pairlist,assign);
            System.out.println("--------------------------------------------------------");
            System.out.println(determine.toString());
            Molecule e = c4h10.getMolecule();
            List<SetOfSymmetryAssignments> sets = determine.findAllSetsOfSymmetryAssignments(e);
            
            Iterator<SetOfSymmetryAssignments> i = sets.iterator();
            while(i.hasNext()) {
                SetOfSymmetryAssignments assignments = i.next();
                System.out.println("========================================================");
                System.out.println(assignments.toString());
            }
            System.out.println(c4h10.getCmlStructureString());
            System.out.println("____________________________________________________");
            SetOfSymmetryMatches finalmatches = determine.findIfMatchInStructures(e);
            System.out.println(finalmatches.toString());
             System.out.println("____________________________________________________");
            
        } catch (CDKException ex) {
            Logger.getLogger(TestFindAllSetsOfSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestFindAllSetsOfSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestFindAllSetsOfSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @Test
    public void TestWithButane() {
        try {
            StructureAsCML c4h10 = GenerateStructures.createFromSmiles("CCCC");
            Molecule mol = new Molecule();
            Atom atm1 = new Atom("C");
            Atom atm2 = new Atom("C");
            Atom atm3 = new Atom("Du");
            Atom atm4 = new Atom("Du");
           mol.addAtom(atm1);
            mol.addAtom(atm2);
            
            Bond bnd1 = new Bond(atm1,atm2);
            mol.addBond(bnd1);
            StructureAsCML molCML = new StructureAsCML(mol);
            
            ArrayList<SymmetryPair> pairlist = new ArrayList<SymmetryPair>();
            SymmetryPair pair1 = new SymmetryPair("Group1","a1");
            SymmetryPair pair2 = new SymmetryPair("Group1","a2");
            pairlist.add(pair1);
            pairlist.add(pair2);
            //pairlist.add(pair4);
            String symname = "SymmetricBond";
            DetermineSymmetryAssignmentsFromConnections assign = new DetermineSymmetryAssignmentsFromConnections();
            DetermineSetOfSymmetryAssignments determine = new DetermineSetOfSymmetryAssignments(symname, molCML, pairlist,assign);
            System.out.println("--------------------------------------------------------");
            System.out.println(determine.toString());
            Molecule e = c4h10.getMolecule();
            List<SetOfSymmetryAssignments> sets = determine.findAllSetsOfSymmetryAssignments(e);
            
            Iterator<SetOfSymmetryAssignments> i = sets.iterator();
            while(i.hasNext()) {
                SetOfSymmetryAssignments assignments = i.next();
                System.out.println("========================================================");
                System.out.println(assignments.toString());
            }
            System.out.println(c4h10.getCmlStructureString());
            System.out.println("____________________________________________________");
            SetOfSymmetryMatches finalmatches = determine.findIfMatchInStructures(e);
            System.out.println(finalmatches.toString());
             System.out.println("____________________________________________________");
            
        } catch (CDKException ex) {
            Logger.getLogger(TestFindAllSetsOfSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestFindAllSetsOfSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestFindAllSetsOfSymmetryAssignments.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}