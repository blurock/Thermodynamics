/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package substructures;

import thermo.data.structure.structure.matching.GetSubstructureMatches;
import org.openscience.cdk.isomorphism.mcss.RMap;
import java.util.List;
import java.sql.SQLException;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.exception.CDKException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToGeneralStructure;

/**
 *
 * @author edwardblurock
 */
public class TestCyclicSubstructure {

    public TestCyclicSubstructure() {
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
    public void testCyclic() {
        try {
            ThermoSQLConnection connection = new ThermoSQLConnection();
            connection.connect();
            NancyLinearFormToMolecule nancyFormToMolecule = new NancyLinearFormToGeneralStructure(connection);
            GetSubstructureMatches matches = new GetSubstructureMatches();
            String moleculeS = "ch2(#1)/ch//ch/1";
            String substructureS = "c(#1)/c//c/1";
            AtomContainer molecule = nancyFormToMolecule.convert(moleculeS);
            AtomContainer substructure = nancyFormToMolecule.convert(substructureS);

            List<List<RMap>> atommaps = matches.getAtomMatches(molecule, substructure);
            System.out.println(atommaps.size());

        } catch (CDKException ex) {
            Logger.getLogger(TestCyclicSubstructure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestCyclicSubstructure.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}