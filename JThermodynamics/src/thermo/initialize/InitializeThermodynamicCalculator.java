/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.initialize;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import jThergas.exceptions.JThergasReadException;

/**
 *
 * @author blurock
 */
public class InitializeThermodynamicCalculator {
    InitializeStandardBensonGroupDatabase bensonGroupDatabase;
    public InitializeThermodynamicCalculator() {
         bensonGroupDatabase = new InitializeStandardBensonGroupDatabase();
    }
    
    public void initializeThermodynamicCalculator() throws SQLException, JThergasReadException, FileNotFoundException, IOException {
        initialize();
        addToDatabase();
    }
    public void initialize() throws SQLException {
        bensonGroupDatabase.initialize();
    }
    public void addToDatabase() throws JThergasReadException, FileNotFoundException, IOException {
        bensonGroupDatabase.addToDatabase();
    }
    
    public void addBensonGroups(InputStream inp, String reference) throws JThergasReadException, FileNotFoundException, IOException {
        bensonGroupDatabase.addToDatabase(inp, reference);
    }
}
