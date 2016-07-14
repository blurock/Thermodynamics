/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package symmetry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.DB.SQLSymmetryDefinition;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;
import thermo.data.structure.structure.symmetry.utilities.SubstructuresFromSymmetry;

/**
 *
 * @author edwardblurock
 */
public class TestStructuresFromSymmetry {

    public TestStructuresFromSymmetry() {
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
    public void testSubstitution() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            //StructureAsCML cmlstruct = GenerateStructures.createPropane();
            NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(connect);
            //Molecule mol = nancy.convert("ch2(c///ch)2");
            Molecule mol = nancy.convert("ch3ch2ch3");
            StructureAsCML cml = new StructureAsCML(mol);

            SQLSymmetryDefinition sqlSymmetry = new SQLSymmetryDefinition(connect);
            //String name = "ExternalSymmetry-C(B1)(B1)(B2)(B2)";
            String name = "ExternalSymmetry-C(B1)(B1)(B1)";
            HashSet vec = sqlSymmetry.retrieveStructuresFromDatabase(name);
            Iterator<SymmetryDefinition> siter = vec.iterator();
            SymmetryDefinition symmetry = siter.next();

            SubstructuresFromSymmetry gensubs = new SubstructuresFromSymmetry(symmetry);
            ArrayList<AtomContainer> subs = gensubs.generateStructures(cml.getMolecule());

            Iterator<AtomContainer> iter = subs.iterator();
            while(iter.hasNext()) {
                AtomContainer sub = iter.next();
                Molecule submol = new Molecule(sub);
                StructureAsCML cmlstruct = new StructureAsCML(submol);
                System.out.println(cmlstruct.getCmlStructureString());
            }



        } catch (CDKException ex) {
            Logger.getLogger(TestStructuresFromSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestStructuresFromSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}