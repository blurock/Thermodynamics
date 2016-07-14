/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson.DB;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import thermo.data.benson.BensonGroupStructure;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonGroupStructures;
import thermo.data.benson.SetOfBensonThermodynamicBase;

/**
 *
 * @author blurock
 */
public class SQLSetOfBensonThermodynamicBase {

    ThermoSQLConnection connection;
    SetOfBensonThermodynamicBase thermoSet;
    SQLBensonThermodynamicBase sqlthermo;
    SQLGroupElement groupElement;
    
    public SQLSetOfBensonThermodynamicBase(SetOfBensonThermodynamicBase set, ThermoSQLConnection connect) {
        connection = connect;
        thermoSet = set;
        sqlthermo = new SQLBensonThermodynamicBase(connect);
        groupElement = new SQLGroupElement(connect);
    }

    public SetOfBensonThermodynamicBase setUpFromSetOfBensonGroupStructures(SetOfBensonGroupStructures bensonset) {
        SetOfBensonThermodynamicBase thermoset = new SetOfBensonThermodynamicBase();

        return thermoset;
    }

        public void setUpFromSetOfBensonGroupStructures(SetOfBensonGroupStructures bensonset,
                SetOfBensonThermodynamicBase thermoset) {
        
        Iterator<BensonGroupStructure> i = bensonset.iterator();
        while (i.hasNext()) {
            BensonGroupStructure benson = i.next();
            benson.setStructureName(null);
            try {
            groupElement.query(benson);
            String name = benson.getStructureName();
            HashSet vec = sqlthermo.retrieveStructuresFromDatabase(name);
            if (vec.size() > 1) {
                Logger.getLogger(SQLSetOfBensonGroupStructures.class.getName()).log(Level.INFO,
                        "More than one thermo group for (supposidly) unique BensonGroup\n{0}", name);
            }
            if (vec.size() >= 1) {
                Iterator<BensonThermodynamicBase> iter = vec.iterator();
                BensonThermodynamicBase thermo = iter.next();
                thermoset.add(thermo);
            } else {
                Logger.getLogger(SQLSetOfBensonGroupStructures.class.getName()).log(Level.INFO,
                        "Group Has no corresponding structure in database\n{0}", name);
            }
            } catch(SQLException ex) {
                String error = "Not Found: " + benson.toString();
                Logger.getLogger(SQLSetOfBensonGroupStructures.class.getName()).log(Level.INFO,error);
            }
        }
    }
}
