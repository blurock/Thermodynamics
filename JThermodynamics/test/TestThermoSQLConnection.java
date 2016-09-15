/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import cml.TestStructureAsCML;
import java.sql.SQLException;
import java.util.HashSet;
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
import org.openscience.cdk.interfaces.IBond.Order;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author blurock
 */
public class TestThermoSQLConnection {

    public TestThermoSQLConnection() {
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
    public void hello() {
    	AtomContainer mol = new AtomContainer();
        mol.setID("Aldehyde");
        Atom at1 = new Atom("C");
        Atom at2 = new Atom("H");
        Atom at3 = new Atom("H");
        Atom at4 = new Atom("O");
        Bond bnd1 = new Bond(at1, at2);
        Bond bnd2 = new Bond(at1, at3);
        Bond bnd3 = new Bond(at1, at4,Order.DOUBLE);
        mol.addAtom(at1);
        mol.addAtom(at2);
        mol.addAtom(at3);
        mol.addAtom(at4);
        mol.addBond(bnd1);
        mol.addBond(bnd2);
        mol.addBond(bnd3);

        try {


            StructureAsCML cml = new StructureAsCML(mol);
            System.out.println(cml.getCmlStructureString());
            ThermoSQLConnection connect = new ThermoSQLConnection();

            if (connect.connect()) {
                SQLStructureAsCML sqlcml = new SQLStructureAsCML(connect);
                sqlcml.initializeStructureInDatabase();
                sqlcml.addToDatabase(cml);

                HashSet vec = sqlcml.retrieveStructuresFromDatabase(mol.getID());
                Iterator<StructureAsCML> iter = vec.iterator();
                StructureAsCML cml2 = iter.next();
                AtomContainer mol2 = cml2.getMolecule();
                System.out.println("ID;         " + mol2.getID());
                System.out.println("Atom Count: " + mol2.getAtomCount());
                System.out.println("Bond Count: " + mol2.getBondCount());

                connect.close();
            } else {
                Logger.getLogger(TestStructureAsCML.class.getName()).log(Level.SEVERE, "Failed SQL Connection");
                System.out.println("Failed SQL connection");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TestStructureAsCML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestStructureAsCML.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}