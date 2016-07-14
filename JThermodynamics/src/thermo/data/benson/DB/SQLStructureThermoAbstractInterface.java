/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson.DB;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * The abstract class for accessing a Thermodyamic table
 *
 * Derived classes fill in four abstract classes: <ul> <li> formulateInsert: set
 * of array of SQL commands to insert structure <li> query: Fill in single
 * structure information (for example, ElementName) <li>
 * retrieveStructuresFromDatabase: Retrieve a set of structures, given a name
 * <li> keyFromStructure produce the table key from the structure </ul>
 *
 * Bases on these abstract classes, several generic methods are provided: <ul>
 * <li> addToDatabase Add the structure to the database <li> databaseCopy
 * Produce string to copy entire database <li> deleteByKey Delete element by key
 * name <li> deleteElement Delete element by structure <li>
 * initializeStructureInDatabase Initialize the database <li> retrieveDatabase
 * </ul>
 *
 * @author blurock
 */
public abstract class SQLStructureThermoAbstractInterface {

    public boolean printSQLCommand = false;
    /**
     * The Thermodynamics database connection
     *
     */
    protected ThermoSQLConnection database;
    /**
     * The table name within the Thermodyanic database This should be provided
     * by default by the derived class
     */
    protected String tableName = "";
    /**
     * The table element that holds the key name
     *
     */
    protected String tableKey = "";

    /**
     * The constructor
     *
     * @param connect The Thermodynamics database connection
     */
    public SQLStructureThermoAbstractInterface(ThermoSQLConnection connect) {
        database = connect;
    }

    /**
     * Provide array of SQL commands to insert structure
     *
     * Used by {@link SQLStructureThermoAbstractInterface#addToDatabase(java.lang.Object)
     * }
     * and {@link SQLStructureThermoAbstractInterface#databaseCopy() }
     *
     * @param structure The structure to insert into database
     * @return The SQL commands
     */
    public abstract String[] formulateInsert(Object structure);

    /**
     * A query about the structure
     *
     * The intent of this method is to either decide whether the structure can
     * be found within the database, or to fill in the structure with data, such
     * as the name or identifier found within the structure.
     *
     * @param structure
     * @return true if search successful
     * @throws java.sql.SQLException
     */
    public abstract boolean query(Object structure) throws SQLException;

    /**
     * Retrieve the set of structures that have the given name
     *
     * The exact meaning of the string name is defined in the method itself.
     * Meant to be used when, for example, a structure has multiple components
     * within the database, The name is the identifier that binds them together.
     *
     * @param name The identifier
     * @return The vector of structures from the database
     * @throws java.sql.SQLException
     */
    public abstract HashSet retrieveStructuresFromDatabase(String name) throws SQLException;

    /**
     * Construct the key string from the structure
     *
     * @param structure The structure
     * @return The key string
     */
    abstract public String keyFromStructure(Object structure);

    /**
     * Insert a structure into the database
     *
     * This method uses the {@link SQLStructureThermoAbstractInterface#formulateInsert(java.lang.Object)
     * }
     * abstract method to create the list of SQL commands needed.
     *
     * @param structure The structure to insert
     * @throws java.sql.SQLException
     */
    public void addToDatabase(Object structure) throws SQLException {
        Statement statement = database.createStatement();
        String[] commands = formulateInsert(structure);
        for (int i = 0; i < commands.length; i++) {
            if (printSQLCommand) {
                System.out.println(commands[i]);
            }
            statement.execute(commands[i]);
        }
    }

    /**
     * The list of SQL commands (as a single string) to copy the entire table
     * This method uses the {@link SQLStructureThermoAbstractInterface#formulateInsert(java.lang.Object)
     * }
     * abstract method to create the list of SQL commands needed. These are then
     * concatenated into a single string.
     *
     * @return The list of SQL commands (as a single string) to copy the entire
     * table
     * @throws SQLException
     */
    public String databaseCopy() throws SQLException {
        StringBuilder buf = new StringBuilder();
        HashSet vec = retrieveDatabase();
        Iterator iter = vec.iterator();
        while(iter.hasNext()) {
            String[] commands = formulateInsert(iter.next());
             for (int j = 0; j < commands.length; j++) {
                buf.append(commands[j]);
       }
        }
         return buf.toString();
    }

    /**
     * Retrieve all the elements in the database
     *
     * First the set of keynames is retrieved. From this set, each element is
     * retrieved (using {@link SQLStructureThermoAbstractInterface#retrieveStructuresFromDatabase(java.lang.String)
     * }) and put in the vector
     *
     * @return The vector of structures
     * @throws java.sql.SQLException
     */
    public ArrayList<String> retrieveDatabaseNames() throws SQLException {
        ArrayList<String> set = new ArrayList<String>();
        Statement statement = database.createStatement();
        String sqlquery = "SELECT " + tableKey + " From " + tableName + ";";
        ResultSet names = statement.executeQuery(sqlquery);
        boolean next = names.first();
        while (next) {
            String name = names.getString(tableKey);
            set.add(name);
            next = names.next();
        }
        return set;
    }

    /**
     * Retrieve all the elements in the database
     *
     * First the set of keynames is retrieved. From this set, each element is
     * retrieved (using {@link SQLStructureThermoAbstractInterface#retrieveStructuresFromDatabase(java.lang.String)
     * }) and put in the vector
     *
     * @return The vector of structures
     * @throws java.sql.SQLException
     */
    public HashSet retrieveDatabase() throws SQLException {
        HashSet set = new HashSet();
        ArrayList<String> names = retrieveDatabaseNames();
        Iterator<String> iter = names.iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            HashSet vecstr = retrieveStructuresFromDatabase(name);
            Iterator iterset = vecstr.iterator();
            set.add(iterset.next());
        }
        return set;
    }

    /**
     * Close the connection
     *
     */
    public void close() {
        database.close();
    }

    /**
     * Erase the database elements
     *
     * @throws java.sql.SQLException
     */
    public void initializeStructureInDatabase() throws SQLException {
        Statement statement = database.createStatement();
        statement.execute("DELETE FROM " + tableName + ";");
    }

    /**
     * Delete structure from table (using derived key)
     *
     * The structure is used to derive the key name. The keyname is used to
     * delete the table element
     *
     * @param structure The structure to delete
     * @throws java.sql.SQLException
     */
    public void deleteElement(Object structure) throws SQLException {
        String keyname = keyFromStructure(structure);
        deleteByKey(keyname);
    }

    /**
     * Delete table element given by key name
     *
     * @param keyname The key name of the element to delete.
     * @throws java.sql.SQLException
     */
    public void deleteByKey(String keyname) throws SQLException {
        String sqlcommand = "DELETE FROM " + tableName + " WHERE " + tableKey + " = \"" + keyname + "\";";
        //System.out.println(sqlcommand);
        Statement statement = database.createStatement();
        statement.execute(sqlcommand);
    }

    public void deleteElements(String[] keys) throws SQLException {
        for (int i = 0; i < keys.length; i++) {
            deleteByKey(keys[i]);
        }

    }
}
