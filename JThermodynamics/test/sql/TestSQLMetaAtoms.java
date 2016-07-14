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
public class TestSQLMetaAtoms {

    public TestSQLMetaAtoms() {
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
    public void MetaInfoDatabase() {
        try {


            ThermoSQLConnection connect = new ThermoSQLConnection();
            if (connect.connect()) {
                SQLMetaAtomInfo sqlinfo = new SQLMetaAtomInfo(connect);
                /*
                sqlinfo.initializeStructureInDatabase();
                MetaAtomInfo info = new MetaAtomInfo();
                info.setElementName("CarbonAtom");
                info.setMetaAtomName("c");
                info.setMetaAtomType("BensonAtom");
                sqlinfo.addToDatabase(info);
                
                MetaAtomInfo infoCO = new MetaAtomInfo();
                infoCO.setElementName("AldehydeKetone");
                infoCO.setMetaAtomName("co");
                infoCO.setMetaAtomType("BensonAtom");
                sqlinfo.addToDatabase(infoCO);
                */
                
                
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