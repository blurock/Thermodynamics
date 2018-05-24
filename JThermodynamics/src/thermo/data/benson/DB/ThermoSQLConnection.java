/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import thermo.properties.SProperties;
/**
 *
 * @author blurock
 */
public class ThermoSQLConnection {
    private Connection connection;
    
    /** Connect to the SQL 'Thermodynamics' database
     *
     * Using the SQL password for the 'Thermodynamics' database,
     * the {@link Connection} is made.
     *
     * The database connection is made through
     * 'jdbc:mysql://127.0.0.1:3306/Thermodynamics'
     *
     * @return true if the connection is successful
     */
    public boolean connect() {
        
        
            String conS = SProperties.getProperty("thermo.database.connection");
            String user = SProperties.getProperty("thermo.database.dbuser");
            String pass = SProperties.getProperty("thermo.database.dbpassword");
            return connect(conS, user, pass);
    }
    /**
     *
     * @param conS
     * @param user
     * @param pass
     * @return
     */
    public boolean connect(String conS, String user, String pass) {
        boolean success = true;
            try {
                Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.INFO,
                        "connect()\t " + conS + "\t  " + user + ": " + pass + "\n");
                            
                
            //Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.INFO,
                    "connect()\t " + conS + "\t  " + user + ": " + pass + "\n");
            connection = DriverManager.getConnection(conS, user, pass);
            if(connection != null) {
//                Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.INFO,"Successful connection");
            } else {
                Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.INFO,"Unsuccessful connection");
            }
        } catch (SQLException ex) {
                    Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.INFO,
                "Trying to connect and failed with SQLException");

            System.out.println(ex.toString());
            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch(Exception ex)
	    {
                    Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.INFO,
                "Trying to connect and failed with simple exception");
            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.SEVERE, ex.toString());
            success = false;
	    }
        return success;
    }
    /** Close the connection
     *
     * @return true if successful
     */
    public boolean close() {
        boolean success = true;
        try {

            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }
    /** Create a {@link Statement} from the {@link Connection}
     *
     * @return the {@link Statement} to be able to form SQL queries on the thermodynamics database
     * @throws java.sql.SQLException
     */
    public Statement createStatement() throws SQLException
    {
        return connection.createStatement();
    }
    
    /** Create a {@link PreparedStatement} from the {@link Connection}
     *
     * @param s The query
     * @return a {@link PreparedStatement} from the {@link Connection} with the query
     * @throws java.sql.SQLException
     */
    public PreparedStatement createPreparedStatement(String s) throws SQLException
    {
        return connection.prepareStatement(s);
    }
    
    /** Set auto commit for the connection.
     *
     * @param on
     * @throws java.sql.SQLException
     */
    public void setAutoCommit(boolean on) throws SQLException
    {
        connection.setAutoCommit(on);
    }

}
