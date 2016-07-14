/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.DB;

import java.util.HashSet;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import thermo.NameListSet;
import thermo.data.benson.DB.ThermoSQLConnection;

/**
 *
 * @author blurock
 */
public class SQLNameListSetTest {

    public SQLNameListSetTest() {
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

    /**
     * Test of retrieveStructuresFromDatabase method, of class SQLNameListSet.
     */
    @Test
    public void testSQLDatabase() throws Exception {
        System.out.println("retrieveStructuresFromDatabase");
        String name = "TestSet";
        NameListSet groups = new NameListSet(name);
        String[] names = new String[3];
        names[0] = "Name1";
        names[1] = "Name2";
        names[2] = "Name3";
        groups.add(names[0]);
        groups.add(names[1]);
        groups.add(names[2]);

        System.out.println("===== Groups with 3 names =======");
        System.out.println(groups.toString());
        ThermoSQLConnection connect = new ThermoSQLConnection();
        if (connect.connect()) {
            SQLNameListSet sqlset = new SQLNameListSet(connect);

            System.out.println("Clear Data in NameListSet");
            sqlset.initializeStructureInDatabase();

            System.out.print("Add to Database");
            sqlset.addToDatabase(groups);

            System.out.println("Retrieve From Database");
            HashSet expResult = null;
            HashSet result = sqlset.retrieveStructuresFromDatabase(name);
            Iterator riter = result.iterator();
            NameListSet databaseset = (NameListSet) riter.next();
            System.out.println(databaseset.toString());

            boolean ans = sqlset.addKeyToSet(name, "Name4");
            if(ans) {
                System.out.println("New Element Added");
            } else {
                throw new Exception("New Element not added (and should have been)");
            }
            boolean ans2 = sqlset.addKeyToSet(name, "Name1");
            if(!ans2) {
                System.out.println("New Element not Added (and this is correct)");
            } else {
                throw new Exception("Duplicate Element added (and should have been)");
            }
            
            boolean ans3 = sqlset.removeKeyFromSet(name, "Name1");
            if(ans3) {
                System.out.println("Element Removed");
            } else {
                throw new Exception("Element not removed");
            }

            HashSet result2 = sqlset.retrieveStructuresFromDatabase(name);
            Iterator iter = result2.iterator();
            NameListSet databaseset2 = (NameListSet) iter.next();
            System.out.println("With new Element and one removed\n" + databaseset2.toString());
            
            String[] keys = sqlset.getListOfSets();
            System.out.println("Keys: ");
            for(int i=0;i<keys.length;i++) {
                System.out.print(keys[i] + "\n");
            }
            System.out.print("\n");
            
            HashSet vec1 = sqlset.retrieveDatabase();
            
            System.out.println("Delete From Database");
            sqlset.deleteElement(databaseset);
            
            HashSet vec2 = sqlset.retrieveDatabase();
            if(vec2.size() == vec1.size() - 3) {
                System.out.println("Deleted one element of three names");
            } else {
                throw new Exception("Did not delete -  Before: " + vec1.size() + "After: " + vec2.size());
            }
             
            connect.close();
        } else {
            throw new Exception("Cannot Connect to database");
        }
    }
}