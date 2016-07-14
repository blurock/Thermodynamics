/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
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
import org.openscience.cdk.isomorphism.mcss.RMap;
import thermo.data.structure.structure.NormalizeMoleculeFromCMLStructure;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.GetSubstructureMatches;

/**
 *
 * @author blurock
 */
public class TestStructureMatching {

    public TestStructureMatching() {
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
    public void MethaneInMethane() {
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

        getAndPrintAtomMatches(mol, mol);
    }

    @Test
    public void testGeneralCarbonInAlkane() throws CDKException, IOException {
        try {
            // Build Single Bonded Carbon
            Molecule mol = new Molecule();
            Atom at1 = new Atom("C");
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

            Molecule ethane = new Molecule();
            Atom eat1 = new Atom("C");
            Atom eat2 = new Atom("C");
            Atom eat3 = new Atom("H");
            Atom eat4 = new Atom("H");
            Atom eat5 = new Atom("H");
            Atom eat6 = new Atom("H");
            Atom eat7 = new Atom("H");
            Atom eat8 = new Atom("H");
            ethane.addAtom(eat1);
            ethane.addAtom(eat2);
            ethane.addAtom(eat3);
            ethane.addAtom(eat4);
            ethane.addAtom(eat5);
            ethane.addAtom(eat6);
            ethane.addAtom(eat7);
            ethane.addAtom(eat8);

            Bond ebnd1 = new Bond(eat1, eat2);
            Bond ebnd2 = new Bond(eat1, eat3);
            Bond ebnd3 = new Bond(eat1, eat4);
            Bond ebnd4 = new Bond(eat1, eat5);
            Bond ebnd5 = new Bond(eat2, eat6);
            Bond ebnd6 = new Bond(eat2, eat7);
            Bond ebnd7 = new Bond(eat2, eat8);
            ethane.addBond(ebnd1);
            ethane.addBond(ebnd2);
            ethane.addBond(ebnd3);
            ethane.addBond(ebnd4);
            ethane.addBond(ebnd5);
            ethane.addBond(ebnd6);
            ethane.addBond(ebnd7);

            StructureAsCML cmlcarbon = new StructureAsCML(mol);
            StructureAsCML cmlethane = new StructureAsCML(ethane);

            NormalizeMoleculeFromCMLStructure norm = new NormalizeMoleculeFromCMLStructure();
            Molecule ncarbon = norm.getNormalizedMolecule(cmlcarbon);
            Molecule nethane = norm.getNormalizedMolecule(cmlethane);

            GetSubstructureMatches matches = new GetSubstructureMatches();
            //getAndPrintSingleAtomMatches(nethane,ncarbon);
            getAndPrintAtomMatches(nethane, ncarbon);
            getAndPrintBondMatches(nethane, ncarbon);
            getAndPrintMatcheForAtom(nethane,ncarbon,0);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestStructureMatching.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestStructureMatching.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getAndPrintAtomMatches(Molecule mol1, Molecule mol2) {
        GetSubstructureMatches match = new GetSubstructureMatches();
        try {
            List<List<RMap>> matches = match.getAtomMatches(mol1, mol2);
            System.out.println("Number of Atom Matchings: " + matches.size());


            for (int i = 0; i < matches.size(); i++) {
                List<RMap> lst = matches.get(i);
                System.out.println("--------------Mapping  " + lst.size());
                for (int j = 0; j < lst.size(); j++) {
                    RMap m = lst.get(j);
                    System.out.println("M1= " + m.getId1() + ", M2= " + m.getId2());
                }
            }
        } catch (CDKException ex) {
            Logger.getLogger(TestStructureMatching.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void getAndPrintBondMatches(Molecule mol1, Molecule mol2) {
        GetSubstructureMatches match = new GetSubstructureMatches();
        try {
            List<List<RMap>> matches = match.getBondMatches(mol1, mol2);
            System.out.println("Number of Bond Matchings: " + matches.size());


            for (int i = 0; i < matches.size(); i++) {
                List<RMap> lst = matches.get(i);
                System.out.println("--------------Mapping   " + lst.size());
                for (int j = 0; j < lst.size(); j++) {
                    RMap m = lst.get(j);
                    System.out.println("M1= " + m.getId1() + ", M2= " + m.getId2());
                }
            }
        } catch (CDKException ex) {
            Logger.getLogger(TestStructureMatching.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void getAndPrintMatcheForAtom(Molecule mol1, Molecule mol2, int atmnum) {
        GetSubstructureMatches match = new GetSubstructureMatches();
        try {
             Integer[] seta = match.getMatchesForAtom(mol1, mol2, atmnum);
            for(int i=0; i<seta.length;i++) {
                System.out.println(seta[i].toString() + "\t");
            }
            System.out.println("\n");
        } catch (CDKException ex) {
            Logger.getLogger(TestStructureMatching.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}