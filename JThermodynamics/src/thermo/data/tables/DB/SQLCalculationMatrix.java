/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.tables.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.tables.CalculationMatrix;
import thermo.data.tables.NumericColumn;

/**
 *
 * @author edwardblurock
 */
public class SQLCalculationMatrix extends SQLStructureThermoAbstractInterface {

    public SQLCalculationMatrix(ThermoSQLConnection connect) {
        super(connect);
        tableName = "Vectors";
        tableKey = "MatrixName";
    }
    @Override
    public String[] formulateInsert(Object structure) {
        CalculationMatrix matrix = (CalculationMatrix) structure;
        int n = matrix.getColumnHeaderValues().size();
        String[] commands = new String[n+3];
        String matrixName = matrix.getNameOfData();
        String elementNameRow    = matrixName + ".RowHeader";
        String elementNameColumn = matrixName + ".ColumnHeader";
        String elementNameDescription = matrixName + ".Description";
        commands[0] = "INSERT INTO Vectors (ElementName, MatrixName, Position, Vector) "
                + "Values("
                + "\"" + elementNameRow + "\","
                + "\"" + matrixName + "\","
                + "\"" + "RowHeader" + "\","
                + "\"" + matrix.getRowHeaderValues().toString() + "\""
                + ")";
        commands[1] = "INSERT INTO Vectors (ElementName, MatrixName, Position, Vector) "
                + "Values("
                + "\"" + elementNameColumn + "\","
                + "\"" + matrixName + "\","
                + "\"" + "ColumnHeader" + "\","
                + "\"" + matrix.getColumnHeaderValues().toString() + "\""
                + ")";
        commands[2] = "INSERT INTO Vectors (ElementName, MatrixName, Position, Vector) "
                + "Values("
                + "\"" + elementNameDescription + "\","
                + "\"" + matrixName + "\","
                + "\"" + "Description" + "\","
                + "\"" + matrix.getDescription() + "\""
                + ")";
        System.out.println("Dimension: " + n);
        int index = 3;
        for(int i=0; i<n; i++) {
            System.out.println("Index: " + i);
            NumericColumn column = matrix.get(i);

            String value = column.toString();
            Integer positionI = new Integer(i);
            String elementName = matrixName + ".C" + positionI.toString();
            commands[index] = "INSERT INTO Vectors (ElementName, MatrixName, Position, Vector) "
                + "Values("
                + "\"" + elementName + "\","
                + "\"" + matrixName + "\","
                + "\"" + "Columns" + "\","
                + "\"" + i + " " + column.toString() + "\""
                + ")";
            System.out.println(index + ":  " + commands[index]);
            index++;
       }
        return commands;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        CalculationMatrix matrix = (CalculationMatrix) structure;
        String sqlquery = "SELECT * FROM Vectors WHERE MatrixName=\"" + matrix.getNameOfData() + "\";";
        Statement statement = database.createStatement();
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean ans = elements.first();
        return ans;
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
        Statement statement = database.createStatement();

        String sqlqueryRow = "SELECT * FROM Vectors WHERE MatrixName=\"" + name + "\""
                 + " AND ElementName=\"" + name + ".RowHeader\""
                 + ";";
        ResultSet elementRow = statement.executeQuery(sqlqueryRow);
        elementRow.first();
        NumericColumn rowRow = new NumericColumn();
        String linerow = elementRow.getString("Vector");
        rowRow.add(linerow);

         String sqlqueryColumn = "SELECT * FROM Vectors WHERE MatrixName=\"" + name + "\""
                 + " AND ElementName=\"" + name + ".ColumnHeader\""
                 + ";";
        ResultSet elementColumn = statement.executeQuery(sqlqueryColumn);
        elementColumn.first();
        NumericColumn rowColumn = new NumericColumn();
        String linecolumn = elementColumn.getString("Vector");
        rowColumn.add(linecolumn);

        String sqlqueryDesc = "SELECT * FROM Vectors WHERE MatrixName=\"" + name + "\""
                 + " AND Position=\"Description\""
                 + ";";
        ResultSet elementDesc = statement.executeQuery(sqlqueryDesc);
        elementDesc.first();
        String description = elementDesc.getString("Vector");

        String sqlqueryVectorOfColumns = "SELECT * FROM Vectors WHERE MatrixName=\"" + name + "\""
                 + " AND Position=\"Columns\""
                 + ";";
        ResultSet elementVector = statement.executeQuery(sqlqueryVectorOfColumns);
        boolean t = elementVector.first();
        HashMap<Integer,NumericColumn> temp = new HashMap<Integer,NumericColumn>();
        while(t) {
            String line = elementVector.getString("Vector");
            int pos = line.indexOf(" ");
            Integer indexI = new Integer(line.substring(0,pos));
            String vecS = line.substring(pos).trim();
            //System.out.println("Read: " + indexI.intValue() + ":  " + vecS);
            NumericColumn col = new NumericColumn();
            col.add(vecS);
           temp.put(indexI,col);
           t = elementVector.next();
        }
        CalculationMatrix matrix = new CalculationMatrix();
        matrix.setColumnHeaderValues(rowColumn);
        matrix.setRowHeaderValues(rowRow);
        matrix.setNameOfData(name);
        matrix.setDescription(description);
        for(int i=0;i<temp.size();i++) {
            Integer iI = new Integer(i);
            NumericColumn col = temp.get(iI);
            matrix.add(col);
        }
        HashSet ans = new HashSet();
        ans.add(matrix);
        return ans;
    }
    private NumericColumn translateColumn(ResultSet set) throws SQLException {
        NumericColumn column = new NumericColumn();
        String line = set.getString("Vector");
        column.add(line);
        return column;
    }
    @Override
    public String keyFromStructure(Object structure) {
        CalculationMatrix matrix = (CalculationMatrix) structure;

        String name = matrix.getNameOfData();

        return name;
    }
}
