/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.symmetry.SetOfSymmetryDefinitions;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;

/**
 *
 * @author edwardblurock
 */
public class SQLSetOfSymmetryDefinitions extends SetOfSymmetryDefinitions {
    ThermoSQLConnection connect;
    SQLSymmetryDefinition sqlSymmetry;
    public SQLSetOfSymmetryDefinitions(ThermoSQLConnection c, String type) throws SQLException {
        connect = c;
        sqlSymmetry = new SQLSymmetryDefinition(c);
        Statement statement = connect.createStatement();
        String isomersql = "SELECT SymmetryName FROM SymmetryDefinition WHERE SymmetryType=\""
                + type + "\";";
        ResultSet results = statement.executeQuery(isomersql);
        while(results.next()) {
            String name = results.getString("SymmetryName");
            HashSet vec = sqlSymmetry.retrieveStructuresFromDatabase(name);
            Iterator<SymmetryDefinition> iter = vec.iterator();
            while(iter.hasNext()) {
                SymmetryDefinition def = iter.next();
                this.add(def);
            }
        }
    }

    public SQLSetOfSymmetryDefinitions(ThermoSQLConnection c) throws SQLException {
         connect = c;
        sqlSymmetry = new SQLSymmetryDefinition(c);
        Statement statement = connect.createStatement();
        HashSet retrieveDatabase = sqlSymmetry.retrieveDatabase();
        Iterator<SymmetryDefinition> iterator = retrieveDatabase.iterator();
        while(iterator.hasNext()) {
            SymmetryDefinition def = iterator.next();
            this.add(def);
        }
   }

}
