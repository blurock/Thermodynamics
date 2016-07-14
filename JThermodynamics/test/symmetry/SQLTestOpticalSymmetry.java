/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package symmetry;

import java.io.IOException;
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
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.DB.SQLSymmetryDefinition;
import thermo.data.structure.structure.symmetry.OpticalSymmetry;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;
import thermo.data.structure.structure.symmetry.SymmetryPair;

/**
 *
 * @author blurock
 */
public class SQLTestOpticalSymmetry {

    public SQLTestOpticalSymmetry() {
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
    public void SQLTestOpticalSymmetry() {
        try {
            String opticalS = new String("TestOptical"); 
            // General Carbon (carbon atom with 4 unspecified atoms)
            //StructureAsCML struct = GenerateStructures.createGeneralCarbon();
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            SQLStructureAsCML sqlstruct = new SQLStructureAsCML(connect);
            Iterator<StructureAsCML> iter = sqlstruct.retrieveStructuresFromDatabase("CarbonAtom").iterator();
            StructureAsCML structcml = iter.next();
            System.out.println(structcml.getCmlStructureString());
            // All four connections different for optical symmetry
            SymmetryPair pair1 = new SymmetryPair("Group1", "a2");
            SymmetryPair pair2 = new SymmetryPair("Group2", "a3");
            SymmetryPair pair3 = new SymmetryPair("Group3", "a4");
            SymmetryPair pair4 = new SymmetryPair("Group4", "a5");
            
            ArrayList<SymmetryPair> pairlist = new ArrayList();
            pairlist.add(pair1);
            pairlist.add(pair2);
            pairlist.add(pair3);
            pairlist.add(pair4);

            OpticalSymmetry optical = new OpticalSymmetry(opticalS, structcml, pairlist);
            System.out.println("============== Created Optical Symmetry ===================\n" + optical.toString());
            if(connect.connect()) {
                
            SQLSymmetryDefinition sqldef = new SQLSymmetryDefinition(connect);
            sqldef.addToDatabase(optical);
            
            SQLSymmetryDefinition sqldef2 = new SQLSymmetryDefinition(connect);
            
            HashSet vec = sqldef2.retrieveStructuresFromDatabase(opticalS);
            Iterator<SymmetryDefinition> diter = vec.iterator();
            SymmetryDefinition optical2 = diter.next();
            System.out.println("============== Retrieved from Datbase ===================\n" + optical2.toString());
            connect.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLTestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(SQLTestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLTestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SQLTestOpticalSymmetry.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}