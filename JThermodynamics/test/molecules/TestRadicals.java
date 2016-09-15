/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package molecules;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
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
import org.openscience.cdk.isomorphism.mcss.RMap;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToGeneralStructure;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.GetSubstructureMatches;
import thermo.data.structure.utilities.MoleculeUtilities;

/**
 *
 * @author edwardblurock
 */
public class TestRadicals {

    public TestRadicals() {
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
    public void findRadicalsInMolecule() {
        try {
            ThermoSQLConnection connection = new ThermoSQLConnection();
            connection.connect();
            NancyLinearFormToGeneralStructure nancy = new NancyLinearFormToGeneralStructure(connection);

            String molS = "ch2(.)ch3";

            AtomContainer mol = nancy.convert(molS);
            MoleculeUtilities.assignIDs(mol);
            List<IAtom> radicals = MoleculeUtilities.findSingleElectrons(mol);
            Iterator<IAtom> iter = radicals.iterator();
            while(iter.hasNext()) {
                IAtom atm = iter.next();
                System.out.println(atm.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestRadicals.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Test
    public void testMoleculeMatch() {
        try {
            ThermoSQLConnection connection = new ThermoSQLConnection();
            connection.connect();
            NancyLinearFormToGeneralStructure nancy = new NancyLinearFormToGeneralStructure(connection);

            String mol1S = "ch2(.)/ch3";
            String mol2S = "cR3(.)";
            String mol3S = "ch(.)//ch2";
            String mol4S = "ch(.)//R";
            AtomContainer mol1 = nancy.convert(mol1S);
            MoleculeUtilities.assignIDs(mol1);
            AtomContainer mol2 = nancy.convert(mol2S);
            MoleculeUtilities.assignIDs(mol2);
            AtomContainer mol3 = nancy.convert(mol3S);
            MoleculeUtilities.assignIDs(mol3);
            AtomContainer mol4 = nancy.convert(mol4S);
            MoleculeUtilities.assignIDs(mol4);

            GetSubstructureMatches matches = new GetSubstructureMatches();

            StructureAsCML cml1 = new StructureAsCML(mol1);
            System.out.println(cml1.getCmlStructureString());
            List< List<RMap> > match1 = matches.getAtomMatches(mol1, mol1);
            MoleculeUtilities.printSetOfCorrespondences(match1, mol1, mol1);

            StructureAsCML cml2 = new StructureAsCML(mol2);
            System.out.println(cml2.getCmlStructureString());
            List< List<RMap> > match2 = matches.getAtomMatches(mol1, cml2.getMolecule());
            MoleculeUtilities.printSetOfCorrespondences(match2, mol1, mol2);

            StructureAsCML cml3 = new StructureAsCML(mol3);
            System.out.println(cml3.getCmlStructureString());
            StructureAsCML cml4 = new StructureAsCML(mol4);
            System.out.println(cml4.getCmlStructureString());
            List< List<RMap> > match3 = matches.getAtomMatches(mol3, cml4.getMolecule());
            MoleculeUtilities.printSetOfCorrespondences(match3, mol3, mol4);


        } catch (CDKException ex) {
            Logger.getLogger(TestRadicals.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestRadicals.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}