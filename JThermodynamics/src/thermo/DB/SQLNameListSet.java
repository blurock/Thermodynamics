/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import thermo.NameListSet;
import thermo.data.benson.DB.*;

/**
 *
 * @author blurock
 */
public class SQLNameListSet extends SQLStructureThermoAbstractInterface{

    public SQLNameListSet(ThermoSQLConnection connect) {
        super(connect);
        tableName = "NameListSet";
        tableKey = "GroupName";
    }
    @Override
    public String[] formulateInsert(Object structure) {
        NameListSet groups = (NameListSet) structure;
        String[] names = groups.toArray();
        String[] commands = new String[names.length];
        
        for(int i=0;i<names.length;i++) {
        String command = "INSERT INTO NameListSet (GroupName, Name) " 
                + "VALUES("
                + "\"" + groups.getSetName() + "\", "
                + "\"" + names[i] + "\");";
        commands[i] = command;
        }
        return commands;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        boolean ans = false;
        NameListSet set = (NameListSet) structure;
        if(set.getSetName().length() != 0) {
            HashSet vec = retrieveStructuresFromDatabase(set.getSetName());
            Iterator iter = vec.iterator();
            if(vec.size() == 1) {
                NameListSet compare = (NameListSet) iter.next();
                ans = compare.equals(set);
            }
        }
        return ans;
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
        HashSet vec = new HashSet();
        Statement statement = database.createStatement();
        String sqlquery = "SELECT Name FROM NameListSet WHERE GroupName=\""
                + name + "\";";
        System.out.println(sqlquery);
        NameListSet set = new NameListSet(name);
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean next = elements.first();
        while(next) {
            set.add(elements.getString("Name"));
            next = elements.next();
        }
        vec.add(set);
        return vec;
        
    }

    public String[] getListOfSets() throws SQLException {
        String sqlquery = "SELECT DISTINCT GroupName FROM NameListSet;";
        Statement statement = database.createStatement();
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean next = elements.first();
        HashSet<String> set = new HashSet();
        while(next) {
            set.add(elements.getString("GroupName"));
            next = elements.next();
        }
        String[] names = new String[set.size()];
        set.toArray(names);
        return names;
    }
    public boolean addKeyToSet(String setname, String name) throws SQLException {
        String testquery = "SELECT Name FROM NameListSet WHERE " 
                + "GroupName=\"" + setname + "\""
                + " AND "
                + "Name=\"" + name + "\";";
        Statement statement = database.createStatement();
        ResultSet elements = statement.executeQuery(testquery);
        boolean next = elements.first();
        if(!next) {
                    String command = "INSERT INTO NameListSet (GroupName, Name) " 
                + "VALUES("
                + "\"" + setname + "\", "
                + "\"" + name + "\");";
                statement.executeUpdate(command);
        }
        return !next;
    }
    public boolean removeKeyFromSet(String setname,String name) throws SQLException {
        String testquery = "SELECT Name FROM NameListSet WHERE " 
                + "GroupName=\"" + setname + "\""
                + " AND "
                + "Name=\"" + name + "\";";
        Statement statement = database.createStatement();
        ResultSet elements = statement.executeQuery(testquery);
        boolean next = elements.first();
        if(next) {
        String delete = "DELETE FROM NameListSet WHERE " 
                + "GroupName=\"" + setname + "\""
                + " AND "
                + "Name=\"" + name + "\";";
                statement.executeUpdate(delete);
        }
        return next;
        
    }
    @Override
    public String keyFromStructure(Object structure) {
        NameListSet set = (NameListSet) structure;
        return set.getSetName();
    }
}
