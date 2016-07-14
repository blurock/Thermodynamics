package cml;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import thermo.data.structure.structure.CML.CMLMetaAtomInfo;
import thermo.data.structure.structure.MetaAtomInfo;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestMetaAtomCML {

    public TestMetaAtomCML() {
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
    public void TestMetaAtomAsCML() {
        try {
            MetaAtomInfo info = new MetaAtomInfo();

            info.setElementName("Molecule");
            info.setMetaAtomName("MetaAtomMolecule");
            info.setMetaAtomType("MoleculeType");

            System.out.println(info.toString());
            CMLMetaAtomInfo infocml0 = new CMLMetaAtomInfo();
            infocml0.setStructure(info);
            String infocmlS = infocml0.restore();
            System.out.println(infocmlS);

            CMLMetaAtomInfo infocml1 = new CMLMetaAtomInfo();
            infocml1.parse(infocmlS);
            MetaAtomInfo info1 = (MetaAtomInfo) infocml1.getStructure();
            
            System.out.println(info1.toString());
        } catch (ParsingException ex) {
            Logger.getLogger(TestMetaAtomCML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestMetaAtomCML.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}