/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package linearform;

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
import org.openscience.cdk.Isotope;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.config.isotopes.IsotopeHandler;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.ISingleElectron;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.DB.SQLSubstituteBackMetaAtomIntoMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.utilities.MoleculeUtilities;
import static org.junit.Assert.*;

/**
 *
 * @author blurock
 */
public class TestNancyLinearForm {

    NancyLinearFormToMolecule nancy = null;

    public TestNancyLinearForm() throws SQLException {
        ThermoSQLConnection connect = new ThermoSQLConnection();
        if (connect.connect()) {
            nancy = new NancyLinearFormToMolecule(connect);
        } else {
            throw new SQLException("Failure to connect to Thermodynamic Database");
        }
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

    private void convertToMolecule(String molS) throws CDKException, SQLException {
        ThermoSQLConnection connection = new ThermoSQLConnection();
    if(connection.connect()) {
            try {
                String nancylinear = "NancyLinearForm";
                SQLSubstituteBackMetaAtomIntoMolecule sqlsub = new SQLSubstituteBackMetaAtomIntoMolecule(nancylinear, connection);
                System.out.println("-------------------------------------------------------");
                Molecule molecule = nancy.convert(molS);
                Iterable<ISingleElectron> sing = molecule.singleElectrons();
                Iterator<ISingleElectron> iter = sing.iterator();
                while (iter.hasNext()) {
                    ISingleElectron ele = iter.next();
                    IAtom atm = ele.getAtom();
                    System.out.println(atm.getID());
                    System.out.println(molecule.getAtomNumber(atm));
                    System.out.println(molecule.getAtomNumber(atm));
                    molecule.removeElectronContainer(ele);
                }
                StructureAsCML cmlstruct = new StructureAsCML(molecule);
                System.out.println("Nancy Linear Form: " + molS);
                System.out.println(cmlstruct.getCmlStructureString());
                System.out.println("-------------------------------------------------------");
                sqlsub.substitute(molecule);
                
                StructureAsCML cmlstruct1 = new StructureAsCML(molecule);
                System.out.println(cmlstruct1.getCmlStructureString());
                System.out.println("-------------------------------------------------------");

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TestNancyLinearForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TestNancyLinearForm.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Test
    public void branchedAlkaneTest() {
        try {
            //String molS = "h";
            //String molS = "ch4";
            //String molS = "ch3(.)";
            //String molS = ".ch3";
            //String molS = "ch3/ch3";
            //String molS = "ch3/ch2/ch3";
            //String molS = ".c(//o)/h";
            //String molS = ".c(//o)h";
            String molS = "c(.)h2/c(/ch3)2/ch2/ch(/ch3)2";
            //String molS=" .ch(/ch3)/ch2/o/oh";
            //String molS = "c(.)h2/c(/ch3)2/ch2/ch(/ch3)2";
            //String molS = "c(#1)h2/ch2/ch2/ch2/o/1";
            //String molS = ".o/o/ch(/o/oh)/ch2/ch2/ch3";
            //String molS = "c(//o)2";
            //String molS = "c(//o)(ch3)";
            //String molS = ".c(//ch2)/h";
            //String molS = "ch2(ch2(.))/ch2/ch2/ch//ch2";
            //String molS = "ch2(#1)/ch2/ch2/1";
            //String molS = "c(#1)&c(#2)&ch&ch&ch&ch&1,1&c(ch2(.))&c(#3)&c(#4)&ch&2,3&ch&ch&ch&ch&4";
            //String molS = "c(#1)&ch&ch&ch&ch&ch&1,1/c(ch2(.))/ch3";
            //String molS = "ch3/c(ch3)h/ch3";
            //String molS = "F/c(X)h/R";
            //String molS = "\'co\'h/ch3";
            //String molS = "c'Br'4";
            //String molS = "cf2//ch2";
            //String molS = "c(ch3)h//ch/ch3";
            //String molS = "c(ch3)2//ch/ch3";
            //String molS ="ch2(o'no2')/ch(o'no2')/ch2/o'no2'";
            //String molS = "'co'(#1)/ch2/ch2/ch2/ch2/ch2/1";
            //String molS = "ch3/co/h";
            //String molS = "ch3/'Br'";
            //convertToMolecule("of2");
            //convertToMolecule("coh/ch3");
            //convertToMolecule("ch2(c///ch)/c///ch");
            convertToMolecule(molS);
        } catch (SQLException ex) {
            Logger.getLogger(TestNancyLinearForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestNancyLinearForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}