/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.tables;

import jThergas.data.read.ReadFileToString;
import java.io.File;
import java.sql.SQLException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.tables.DB.SQLCalculationMatrix;

/** Build a {@link CalculationMatrix} from a file
 *
 * Read a {@link CalculationMatrix} from a file and put it into the (SQL) database.
 * The structure within the file is:
 *
 * Line 1: Name of table (one word)
 * line 2: Description of table
 * line 3: String of row header values
 * line 4: String of column header values
 * line 5-: String of column values
 *
 * @author edwardblurock
 */
public class BuildCalculationTable {
    /** Constructor
     *
     * The constructor reads and stores the {@link CalculationMatrix}
     *
     * @param fileF The file to read
     * @param connect the SQL connection
     * @throws SQLException
     */
    public BuildCalculationTable(File fileF, ThermoSQLConnection connect) throws SQLException {
        ReadFileToString read = new ReadFileToString();
        read.read(fileF);
        String matrixS = read.outputString;

        CalculationMatrix matrix = new CalculationMatrix();
        matrix.readCalculationMatrixFromString(matrixS);

        System.out.println(matrix.toString());

        connect.connect();
        SQLCalculationMatrix sqlmatrix = new SQLCalculationMatrix(connect);
        sqlmatrix.addToDatabase(matrix);
    }

}
