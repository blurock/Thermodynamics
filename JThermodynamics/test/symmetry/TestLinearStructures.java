/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package symmetry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormMetaAtoms;
import thermo.data.structure.linearform.NancyLinearFormToGeneralStructure;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;
import thermo.data.structure.structure.DB.SQLMetaAtomInfo;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.SubstituteBackMetaAtomsIntoMolecule;
import thermo.data.structure.structure.matching.SubstituteLinearStructures;
import static org.junit.Assert.*;

/**
 *
 * @author edwardblurock
 */
public class TestLinearStructures {

    public TestLinearStructures() {
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
    public void linearStructuresInDatabase() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            SQLMetaAtomInfo sqlmeta = new SQLMetaAtomInfo(connect);
            NancyLinearFormToMolecule linear = new NancyLinearFormToMolecule(connect);

            Molecule mol = linear.convert("'*'C///CR");
            StructureAsCML cmlstruct = new StructureAsCML(mol);
            System.out.println(cmlstruct.getCmlStructureString());

            NancyLinearFormToGeneralStructure nancy = new NancyLinearFormToGeneralStructure(connect);
            Molecule converted = nancy.convert("'*'C///CR");
            StructureAsCML cmlconvert = new StructureAsCML(converted);
            System.out.println(cmlconvert.getCmlStructureString());

            
        } catch (CDKException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Test
    public void substituteInLinearAtoms() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();

            SubstituteLinearStructures subs = new SubstituteLinearStructures(connect);
            System.out.println(subs.toString());
            NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(connect);
            Molecule mol = nancy.convert("ch2(c///ch)/c///c/c///ch");
            StructureAsCML cmlstruct = new StructureAsCML(mol);
            System.out.println(cmlstruct.getCmlStructureString());
            System.out.println("===========================================");
            Molecule newmolecule = subs.substitute(cmlstruct);
            StructureAsCML newcmlstruct = new StructureAsCML(newmolecule);
            System.out.println(newcmlstruct.getCmlStructureString());


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}