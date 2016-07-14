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
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author blurock
 */
public class SQLStructureAsCML extends SQLStructureThermoAbstractInterface {

    public SQLStructureAsCML(ThermoSQLConnection connect) {
        super(connect);
        tableName = "CMLStructures";
        tableKey = "ElementName";
    }
    
    public String cmlStructureStringToSQLString(String cml) {
        StringBuilder buf = new StringBuilder();
        byte[] bytes = cml.getBytes();
        for(int i=0;i<bytes.length;i++) {
            if(bytes[i] == '"') {
                buf.append("\\\"");
            } else {
                buf.append((char) bytes[i]);
            }
        }
        return buf.toString();
    }
    @Override
    public String[] formulateInsert(Object structure) {
        StructureAsCML struct = (StructureAsCML) structure;
                     String sqlcommand = "INSERT INTO CMLStructures (ElementName, CMLStructure,Source) "
                + "VALUES("
                + "\"" + struct.getNameOfStructure() +"\", "
                + "\"" + cmlStructureStringToSQLString(struct.getCmlStructureString()) + "\","
                + "\"" + struct.getSource() + "\""
                + ");";
     //System.out.println(struct);
     String[] commands = new String[1];
     commands[0] = sqlcommand;
     return commands;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        StructureAsCML struct = (StructureAsCML) structure;
        
        return true;
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
        HashSet vec = new HashSet();
       String sqlquery = "SELECT ElementName, CMLStructure From CMLStructures WHERE ElementName=\""
                + name + "\";";
        //System.out.println(sqlquery);
        Statement statement = database.createStatement();
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean next = elements.first();
        while(next) {
            StructureAsCML struct = new StructureAsCML();
            struct.setCmlStructureString(elements.getString("CMLStructure"));
            struct.setNameOfStructure(elements.getString("ElementName"));
            vec.add(struct);
            next = elements.next();
        }
        return vec;
    }

    @Override
    public String keyFromStructure(Object structure) {
        StructureAsCML struct = (StructureAsCML) structure;
        return struct.getNameOfStructure();
    }
    public void deleteFromSource(String src) throws SQLException {
        String[] molecules = findStructuresOfSource(src);
        for(int i=0;i<molecules.length;i++) {
            deleteByKey(molecules[i]);
        }
    }
    public String[] findStructuresOfSource(String src) throws SQLException {
        Statement statement = database.createStatement();
        String command = "SELECT ElementName FROM CMLStructures WHERE Source=\""
                + src + "\";";
        ResultSet results = statement.executeQuery(command);
        HashSet<String> namesV = new HashSet<String>();
        while(results.next()) {
            namesV.add(results.getString("ElementName"));
        }
        String[] names = new String[namesV.size()];
        names = namesV.toArray(names);
        return names;
    }

}
