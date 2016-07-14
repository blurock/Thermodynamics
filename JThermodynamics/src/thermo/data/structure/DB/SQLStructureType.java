/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.StructureType;

/**
 *
 * @author blurock
 */
public class SQLStructureType extends SQLStructureThermoAbstractInterface {

    public SQLStructureType(ThermoSQLConnection connect) {
        super(connect);
        tableName = "StructureType";
        tableKey = "KeyName";
    }

    @Override
    public String[] formulateInsert(Object structure) {
        StructureType type = (StructureType) structure;
        String typecom = "INSERT INTO StructureType (KeyName, ElementName, StructureType) " + "VALUES(" 
                + "\"" + keyFromStructure(type) + "\","
                + "\"" + type.getNameOfStructure() + "\"," 
                + "\"" + type.getTypeOfStructure() + "\"); ";

        String[] vec = new String[1];
        vec[0] = typecom;
        return vec;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        StructureType struct = (StructureType) structure;
        String sqlquery = "SELECT ElementName, StructureType From StructureType WHERE "
                + "StructureType=\"" + struct.getTypeOfStructure() + "\""
                + " AND ElementName = \"" + struct.getNameOfStructure() + "\""
                + ";";
        Statement statement = database.createStatement();
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean ans = elements.first();
        return ans;
    }
    public HashSet querySet(Object structure) throws SQLException {
        StructureType type = (StructureType) structure;
        HashSet vec = null;
        if(type.getNameOfStructure() != null && type.getTypeOfStructure() == null) {
            vec = retrieveStructuresFromDatabase(type.getNameOfStructure());
        } else if(type.getNameOfStructure() == null && type.getTypeOfStructure() != null) {
            vec = retrieveStructuresOfTypeFromDatabase(type.getTypeOfStructure());
        } else if(type.getNameOfStructure() == null && type.getTypeOfStructure() == null) {
            vec = retrieveDatabase();
        } else if(type.getNameOfStructure() != null && type.getTypeOfStructure() != null) {
            if(query(structure)){
                vec = new HashSet();
                vec.add(structure);
            }
        }
        return vec;
    }

    public HashSet retrieveStructuresOfTypeFromDatabase(String type)  throws SQLException  {
        HashSet vec = null;
       String sqlquery = "SELECT ElementName, StructureType From StructureType WHERE StructureType=\""
                + type + "\";";
        //System.out.println(sqlquery);
        Statement statement = database.createStatement();
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean next = elements.first();
        if(next) {
            vec = new HashSet();
        }
        while(next) {
            StructureType struct = new StructureType();
            struct.setTypeOfStructure(elements.getString("StructureType"));
            struct.setNameOfStructure(elements.getString("ElementName"));
            vec.add(struct);
            next = elements.next();
        }
        return vec;
    }
    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
       HashSet vec = new HashSet();
       String sqlquery = "SELECT ElementName, StructureType "
               + "From StructureType WHERE ElementName=\""
                + name + "\";";
        //System.out.println(sqlquery);
        Statement statement = database.createStatement();
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean next = elements.first();
        while(next) {
            StructureType struct = new StructureType();
            struct.setTypeOfStructure(elements.getString("StructureType"));
            struct.setNameOfStructure(elements.getString("ElementName"));
            vec.add(struct);
            next = elements.next();
        }
        return vec;
        
    }

    @Override
    public String keyFromStructure(Object structure) {
        StructureType type = (StructureType) structure;
        String keyname = type.getNameOfStructure() + "." + type.getTypeOfStructure();
        return keyname;
    }
}
