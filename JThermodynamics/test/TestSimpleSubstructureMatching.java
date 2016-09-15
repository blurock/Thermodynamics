/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.mcss.RMap;
import thermo.data.structure.structure.matching.GetSubstructureMatches;
import org.openscience.cdk.CDKConstants;
import thermo.data.structure.structure.matching.QueryAtomWithMetaAtoms;
/**
 *
 * @author edwardblurock
 */
public class TestSimpleSubstructureMatching {

    public TestSimpleSubstructureMatching() {
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
    public void MethaneInMethane() {
        // Build Single Bonded Carbon
	   AtomContainer mol = new AtomContainer();
        QueryAtomWithMetaAtoms at1 = new QueryAtomWithMetaAtoms(new Atom("C"));
        QueryAtomWithMetaAtoms at2 = new QueryAtomWithMetaAtoms(new Atom("H"));
        QueryAtomWithMetaAtoms at3 = new QueryAtomWithMetaAtoms(new Atom("H"));
        QueryAtomWithMetaAtoms at4 = new QueryAtomWithMetaAtoms(new Atom("R"));
        QueryAtomWithMetaAtoms at5 = new QueryAtomWithMetaAtoms(new Atom("R"));
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

    private void getAndPrintAtomMatches(AtomContainer mol1, AtomContainer mol2) {
        try {
            List<List<RMap>> matches = UniversalIsomorphismTester.getSubgraphMaps(mol1, mol2);
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
    private void getAndPrintBondMatches(AtomContainer mol1, AtomContainer mol2) {
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

    private void getAndPrintMatcheForAtom(AtomContainer mol1, AtomContainer mol2, int atmnum) {
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