/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo;

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
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.HeatCapacityAtInfinity;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.test.GenerateStructures;
import static org.junit.Assert.*;

/**
 *
 * @author edwardblurock
 */
public class TestHeatCapacityAtInfinity {
    static ThermoSQLConnection connection;
    public TestHeatCapacityAtInfinity() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        connection = new ThermoSQLConnection();
        connection.connect();
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
    public void testSingleAtom() {
    	AtomContainer mol = new AtomContainer();
        mol.setID("He");
        Atom at1 = new Atom("He");
        mol.addAtom(at1);
        double cp = calculate(mol);
        System.out.println("Heat Capacity of Single Molecule: " + cp);
    }
    @Test
    public void testLinearMolecule() {
        try {
            NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(connection);
            AtomContainer mol = nancy.convert("'Br'/'Br'");
            System.out.println("Heat Capacity of Br2 (linear): " + calculate(mol));
            mol = nancy.convert("h/c///c/h");
            System.out.println("Heat Capacity of HCCH (linear): " + calculate(mol));
            mol = nancy.convert("ch///c/c///ch");
            System.out.println("Heat Capacity of HCCCCH (linear): " + calculate(mol));
        } catch (SQLException ex) {
            Logger.getLogger(TestHeatCapacityAtInfinity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Test
    public void testGeneralMolecule() {
        try {
            NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(connection);
            AtomContainer mol = nancy.convert("ch4");
            System.out.println("Heat Capacity of ch4: " + calculate(mol));
            mol = nancy.convert("ch3/ch2/ch3");
            System.out.println("Heat Capacity of propane: " + calculate(mol));
            mol = nancy.convert("ch3/ch2/ch2/ch3");
            System.out.println("Heat Capacity of butane: " + calculate(mol));

        } catch (SQLException ex) {
            Logger.getLogger(TestHeatCapacityAtInfinity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    double calculate(AtomContainer mol) {
        double cp = 0.0;
            try {
                HeatCapacityAtInfinity cpinf = new HeatCapacityAtInfinity(connection);
                cp = cpinf.heatCapacityAtInfinity(mol);
            } catch (SQLException ex) {
                Logger.getLogger(TestHeatCapacityAtInfinity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CDKException ex) {
                Logger.getLogger(TestHeatCapacityAtInfinity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TestHeatCapacityAtInfinity.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TestHeatCapacityAtInfinity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return cp;
    }
}