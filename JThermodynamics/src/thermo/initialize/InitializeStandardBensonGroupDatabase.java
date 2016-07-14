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
import thermo.data.benson.thergas.BuildBensonTable;
import thermo.properties.SProperties;

/**
 *
 * @author blurock
 */
public class InitializeStandardBensonGroupDatabase {
    BuildBensonTable buildBenson;
    
    /**
     * 
     */
    public InitializeStandardBensonGroupDatabase() {
        buildBenson = new BuildBensonTable();
    }
    public void initialize() throws SQLException {
        buildBenson.initializeTable();
    }
    public void addToDatabase() throws JThergasReadException, FileNotFoundException, IOException {
        String standardS = "Standard Benson Source";
        String bensonGroupInfoResource = SProperties.getProperty("thermo.database.bensongroup.resourcefile");
        InputStream inp = getClass().getResourceAsStream(bensonGroupInfoResource);
        addToDatabase(inp,standardS);
    }
    public void addToDatabase(InputStream inp, String reference) throws JThergasReadException, FileNotFoundException, IOException {
        buildBenson.build(inp, reference, false);        
    }
}
