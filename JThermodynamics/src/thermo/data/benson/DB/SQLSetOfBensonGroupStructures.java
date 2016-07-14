/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson.DB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import thermo.data.benson.BensonGroupStructure;
import thermo.data.benson.SetOfBensonGroupStructures;

/**
 *
 * @author blurock
 */
public class SQLSetOfBensonGroupStructures {
    ThermoSQLConnection connection;
    SQLGroupElement sqlelement;
    SetOfBensonGroupStructures structures;
    
    public SQLSetOfBensonGroupStructures(SetOfBensonGroupStructures struct, ThermoSQLConnection connect) {
        connection = connect;
        sqlelement = new SQLGroupElement(connect);
        structures = struct;
    }
    public String[] fillInNamesForStructures() throws SQLException {
        ArrayList<String> namelist = new ArrayList<String>();
        Iterator<BensonGroupStructure> i = structures.iterator();
        while (i.hasNext()) {
        BensonGroupStructure grp = i.next();
        grp.setStructureName(null);
        String[] names = sqlelement.queryNameSet(grp);
        if(names.length > 1) {
            Logger.getLogger(SQLSetOfBensonGroupStructures.class.getName()).log(Level.INFO,
                    "More than one name for (supposidly) unique BensonGroup\n" 
                    + grp.toString());
        }
        if(names.length >= 1) {
            String name = names[0];
            namelist.add(name);
            grp.setStructureName(name);
        } else {
            Logger.getLogger(SQLSetOfBensonGroupStructures.class.getName()).log(Level.INFO,
                    "Group Has no corresponding structure in database\n" 
                    + grp.toString());
        }
        }
        String[] lst = new String[namelist.size()];
        namelist.toArray(lst);
        return lst;
    }
}
