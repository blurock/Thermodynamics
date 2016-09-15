/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
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
import thermo.data.structure.structure.DB.SQLSubstituteBackMetaAtomIntoMolecule;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author reaction
 */
public class TestSQLSubstituteBackMetaAtomIntoMolecule {

    public TestSQLSubstituteBackMetaAtomIntoMolecule() {
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
    public void NancyLinearFormTest() {
    ThermoSQLConnection connection = new ThermoSQLConnection();
    if(connection.connect()) {
            try {
                String nancy = new String("NancyLinearForm");
                SQLSubstituteBackMetaAtomIntoMolecule substitute = new SQLSubstituteBackMetaAtomIntoMolecule(nancy, connection);

                Iterator<MetaAtomDefinition> imeta = substitute.iterator();
                while(imeta.hasNext()) {
                    MetaAtomDefinition metaatom = imeta.next();
                    System.out.println(metaatom.toString());
                }

        AtomContainer mol = new AtomContainer();
        mol.setID("Aldehyde");
        Atom at1 = new Atom("co");
        Atom at2 = new Atom("H");
        Atom at3 = new Atom("H");
        Bond bnd1 = new Bond(at1, at2);
        Bond bnd2 = new Bond(at1, at3);
        mol.addAtom(at1);
        mol.addAtom(at2);
        mol.addAtom(at3);
        mol.addBond(bnd1);
        mol.addBond(bnd2);

        System.out.println("Start Substitution");
        System.out.println("Molecule Before Subsitution:\n" + mol.toString());
        substitute.substitute(mol);
        StructureAsCML subbed = new StructureAsCML(mol);
        System.out.println("Molecule After Substitution:\n" + subbed.getCmlStructureString());


            } catch (SQLException ex) {
                Logger.getLogger(TestSQLSubstituteBackMetaAtomIntoMolecule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CDKException ex) {
                Logger.getLogger(TestSQLSubstituteBackMetaAtomIntoMolecule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TestSQLSubstituteBackMetaAtomIntoMolecule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TestSQLSubstituteBackMetaAtomIntoMolecule.class.getName()).log(Level.SEVERE, null, ex);
            }




    }

    }

}