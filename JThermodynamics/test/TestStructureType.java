/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureType;
import thermo.data.structure.structure.StructureType;

/**
 *
 * @author blurock
 */
public class TestStructureType {

    private String type;
    private String name;

    public TestStructureType() {
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
    public void SQLSTructureType() {
        type = "Molecule";
        name = "Aldehyde";
        ThermoSQLConnection connect = new ThermoSQLConnection();



        if (connect.connect()) {
            String ans = "SUCCESS";
            SQLStructureType sqltype = new SQLStructureType(connect);


            StructureType t = new StructureType();
            t.setTypeOfStructure(type);
            t.setNameOfStructure(name);
            try {
                sqltype.deleteElement(t);
            } catch (SQLException ex) {
                System.out.println("Was not there yet (Not necessarilty an error): " + sqltype.keyFromStructure(t));
            }

            try {
                sqltype.addToDatabase(t);
            } catch (SQLException ex) {
                Logger.getLogger(TestStructureType.class.getName()).log(Level.SEVERE, null, ex);
                ans = "ERROR";
            }

            t.setTypeOfStructure("Substructure");
            try {
                sqltype.deleteElement(t);

            } catch (SQLException ex) {
                System.out.println("Was not there yet (Not necessarilty an error): " + sqltype.keyFromStructure(t));
            }
            try {

                sqltype.addToDatabase(t);
            } catch (SQLException ex) {
                Logger.getLogger(TestStructureType.class.getName()).log(Level.SEVERE, null, ex);
            }

            HashSet vec;
            try {
                vec = sqltype.retrieveStructuresOfTypeFromDatabase("Molecule");
                System.out.println("Elements of Type 'Molecule'");
                Iterator<StructureType> iter = vec.iterator();
                for (int i = 0; i < vec.size(); i++) {
                    StructureType element = iter.next();
                    System.out.println(element.writeAsString());
                }

            } catch (SQLException ex) {
                Logger.getLogger(TestStructureType.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            System.out.println("ERROR: Failed SQL connection");
        }
    }
}