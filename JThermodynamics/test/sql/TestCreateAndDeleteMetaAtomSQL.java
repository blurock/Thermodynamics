/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sql;

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
import thermo.data.structure.structure.DB.SQLMetaAtomInfo;
import thermo.data.structure.structure.MetaAtomInfo;

/**
 *
 * @author blurock
 */
public class TestCreateAndDeleteMetaAtomSQL {

    public TestCreateAndDeleteMetaAtomSQL() {
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
    public void createAndDeleteSQLMetaAtomInfo() {
        try {
                ThermoSQLConnection connect = new ThermoSQLConnection();
            if (connect.connect()) {
                SQLMetaAtomInfo sqlinfo = new SQLMetaAtomInfo(connect);
                
                
                MetaAtomInfo infoCO = new MetaAtomInfo();
                infoCO.setElementName("AldehydeKetone");
                infoCO.setMetaAtomName("ald");
                infoCO.setMetaAtomType("BensonAtom");
                sqlinfo.addToDatabase(infoCO);
                
                sqlinfo.deleteElement(infoCO);
                
                HashSet vec = sqlinfo.retrieveDatabase();
                Iterator i = vec.iterator();
                while (i.hasNext()) {
                    MetaAtomInfo inf = (MetaAtomInfo) i.next();
                    System.out.println(inf.toString());
                }
                vec = sqlinfo.retrieveMetaAtomTypesFromDatabase("BensonAtom");
                i = vec.iterator();
                while (i.hasNext()) {
                    MetaAtomInfo inf = (MetaAtomInfo) i.next();
                    System.out.println(inf.toString());
                }
               

            }
        } catch (SQLException ex) {
            Logger.getLogger(TestSQLMetaAtoms.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}