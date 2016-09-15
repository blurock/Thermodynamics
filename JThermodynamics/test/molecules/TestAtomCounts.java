/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package molecules;

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
import thermo.data.structure.structure.AtomCounts;
import thermo.data.structure.structure.StructureAsCML;
import thermo.test.GenerateStructures;

/**
 *
 * @author edwardblurock
 */
public class TestAtomCounts {

    public TestAtomCounts() {
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
    public void simpleTest() {
        try {
            StructureAsCML methyl = GenerateStructures.createMethyl();
            AtomContainer atoms = (AtomContainer) methyl.getMolecule();
            AtomCounts counts = new AtomCounts(atoms);

            System.out.println("Methyl Test: " + counts.isomerName());
            String[] atomnames = counts.getAtomStringArray(4);
            int[] atomcounts = counts.correspondingAtomCount(4);
            for(int i=0;i<atomnames.length;i++) {
                System.out.println("Atom: " + atomnames[i] + "\t Count:" + atomcounts[i]);
            }

        } catch (CDKException ex) {
            Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestAtomCounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}