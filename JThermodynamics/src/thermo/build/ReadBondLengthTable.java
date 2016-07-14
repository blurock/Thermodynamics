/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.build;

import jThergas.data.read.ReadFileToString;
import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.bonds.BondLength;
import thermo.data.structure.bonds.BondLengthTable;
import thermo.data.structure.bonds.DB.SQLBondLength;

/** Read in the database
 *
 * @author edwardblurock
 */
public class ReadBondLengthTable extends BondLengthTable {
    ThermoSQLConnection connection;
    ReadFileToString readFileToString;
    SQLBondLength sqlbondlength;

    /** constructor
     *
     * @param c The SQL connector
     */
    public ReadBondLengthTable(ThermoSQLConnection c) {
        connection = c;
        readFileToString = new ReadFileToString();
        sqlbondlength = new SQLBondLength(connection);
    }
    /** Read the data from a file
     *
     * @param filename The name of the file
     * @param Source  The source description
     * @throws SQLException if an error in adding to the database
     */
    public void read(String filename, String Source) throws SQLException {
        File f = new File(filename);
        readFileToString.read(f);
        this.readFromString(readFileToString.outputString, Source);
        Iterator<BondLength> iterator = this.iterator();
        while(iterator.hasNext()) {
            BondLength bond = iterator.next();
            sqlbondlength.addToDatabase(bond);
        }
    }

}
