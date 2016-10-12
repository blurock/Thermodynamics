/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package symmetry;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import thermo.data.structure.structure.symmetry.CML.CMLListOfSymmetryPairs;
import thermo.data.structure.structure.symmetry.ListOfSymmetryPairs;
import thermo.data.structure.structure.symmetry.SymmetryPair;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestListOfSymmetryPairs {

    public TestListOfSymmetryPairs() {
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
    public void TestListOfSymmetryPairs() {
        try {
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

            String cmlpairsS = cmlpairs.restore();

            System.out.println("CML Symmetry List" + cmlpairsS);

            CMLListOfSymmetryPairs cmlpairs1 = new CMLListOfSymmetryPairs();
            cmlpairs1.parse(cmlpairsS);

            ListOfSymmetryPairs pairlist2 = (ListOfSymmetryPairs) cmlpairs1.getStructure();

            System.out.println("Original:    " + pairlist.toString());
            System.out.println("CML Version: " + pairlist2.toString());
        } catch (ValidityException ex) {
            Logger.getLogger(TestListOfSymmetryPairs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParsingException ex) {
            Logger.getLogger(TestListOfSymmetryPairs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestListOfSymmetryPairs.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

}