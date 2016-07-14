
package thermo.data.tables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/** Calculation Matrix
 *
 * This is a matrix of values meant to be used for the estimation of a
 * two dimensional function.
 * The rows represent the x value
 * The columns represent the y value
 *
 * The row headers are the order set of x values.
 * The column headers are the ordered set of y values.
 * The function value is given by the matrix.
 *
 * Values inbetween values found in the row or column headers
 * are linearly extrapolated.
 *
 * It is derived from {@link ArrayList<NumericColumn>}
 * where each {@link NumericColumn} is a column in the matrix
 *
 * @author edwardblurock
 */
public class CalculationMatrix extends ArrayList<NumericColumn> {
    String nameOfData;
    String description;
    NumericColumn rowHeaderValues;
    NumericColumn columnHeaderValues;

    /** empty Constructore
     */
    public CalculationMatrix() {
    }
    /** Constructor with dimenstions
     *
     * @param dim The dimension
     */
    public CalculationMatrix(int dim) {
        super(dim);
    }
    /** Constructor with set of doubles
     * Add a set of doubles as a column
     * @param values array of simple doubles
     */
    public void addColumn(double[] values) {
        NumericColumn column = new NumericColumn();
        column.add(values);
    }
    /** Add the row header with string of delimited values
     *
     * @param line A string with space/tab delimited values
     */
    public void addRowHeader(String line) {
        rowHeaderValues = new NumericColumn();
        rowHeaderValues.add(line);
    }
    /** Add the column header with a string of space/tab delimited values
     *
     * @param line A string with space/tab delimited values
     */
    public void addColumnHeader(String line) {
        columnHeaderValues = new NumericColumn();
        columnHeaderValues.add(line);
    }
    /** Add the column with a string of space/tab delimited values
     *
     * @param line A string with space/tab delimited values
     */
    public void addColumn(String line) {
        NumericColumn column = new NumericColumn();
        column.add(line);
        this.add(column);
    }
    /** read whole matrix from a string
     * For example a string from a file
     *
     * Line 1: Name of table (one word)
     * line 2: Description of table
     * line 3: String of row header values
     * line 4: String of column header values
     * line 5-: String of column values
     *
     * @param matS The string with the matrix
     */
    public void readCalculationMatrixFromString(String matS) {
        StringTokenizer tok = new StringTokenizer(matS,"\n");
        if(tok.countTokens() > 4) {
            nameOfData = tok.nextToken();
            description = tok.nextToken();
            rowHeaderValues = new NumericColumn();
            rowHeaderValues.add(tok.nextToken());
            columnHeaderValues = new NumericColumn();
            columnHeaderValues.add(tok.nextToken());

            while(tok.hasMoreTokens()) {
                String line = tok.nextToken();
                this.addColumn(line);
            }
        } else {
            String error = "CalculationMatrix: wrong format (not enough lines): " + tok.countTokens();
            throw new NumberFormatException(error);
        }
    }
    /** Determine the matrix value of F(row,column)
     *
     * Find the functional value:
     *  For values between specific row or column header values,
     *  a linear extrapolation is made.
     *
     * @param row The row value
     * @param column The column Value
     * @return The extrapolated value
     */
    public double getFunctionalValue(double row, double column) {
        int indexRow = rowHeaderValues.lowerBound(row);
        int indexCol = columnHeaderValues.lowerBound(column);
        double fractionRow = rowHeaderValues.fractionBetweenValues(indexRow, row);
        double fractionColumn = columnHeaderValues.fractionBetweenValues(indexCol, column);

        NumericColumn lowerColumn = this.get(indexCol);
        NumericColumn upperColumn = this.get(indexCol+1);

        double valueLL = lowerColumn.get(indexRow);
        double valueUL = lowerColumn.get(indexRow+1);
        double valueLU = upperColumn.get(indexRow);
        double valueUU = upperColumn.get(indexRow+1);

        double valueAL = valueLL + fractionRow * (valueUL - valueLL);
        double valueAU = valueLU + fractionRow * (valueUU - valueLU);

        double valueA = valueAL + fractionColumn * (valueAU-valueAL);

        return valueA;
    }
    /** The matrix as a printable string
     *
     * This string is NOT the counterpart to the readCalculationMatrixFromString
     * function, it is meant to be readable
     *
     * @return the String value
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(this.nameOfData);
        buf.append("\n");
        buf.append(this.description);
        buf.append("\n");
        buf.append("    : \t");
        buf.append(columnHeaderValues.toString());
        buf.append("\n");
        //System.out.println("Size: " + this.size() + "\t R: " + rowHeaderValues.size() + "\t" + columnHeaderValues.size());
        for(int i=0;i<rowHeaderValues.size();i++) {
            Double rowvalue = rowHeaderValues.get(i);
            buf.append(rowvalue.toString());
            buf.append(":\t");
            for(int j=0;j<columnHeaderValues.size();j++) {
                NumericColumn colD = this.get(j);
                Double value = colD.get(i);
                buf.append(value.toString());
                buf.append("\t");
            }
            buf.append("\n");
        }
        return buf.toString();
    }

    /** Get column header values
     *
     * @return column header values
     */
    public NumericColumn getColumnHeaderValues() {
        return columnHeaderValues;
    }

    /** Set column header values
     *
     * @param columnHeaderValues column header values
     */
    public void setColumnHeaderValues(NumericColumn columnHeaderValues) {
        this.columnHeaderValues = columnHeaderValues;
    }

    /** Get the description
     *
     * @return the description string
     */
    public String getDescription() {
        return description;
    }

    /** Set the description
     *
     * @param description the description string
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Get the name of the data matrix
     *
     * @return the name of the data matrix
     */
    public String getNameOfData() {
        return nameOfData;
    }

    /** Set the name of the data matrix
     *
     * @param nameOfData the name of the data matrix
     */
    public void setNameOfData(String nameOfData) {
        this.nameOfData = nameOfData;
    }

    /** Get the row header values
     *
     * @return the row header values
     */
    public NumericColumn getRowHeaderValues() {
        return rowHeaderValues;
    }

    /** Set the row header values
     *
     * @param rowHeaderValues the row header values
     */
    public void setRowHeaderValues(NumericColumn rowHeaderValues) {
        this.rowHeaderValues = rowHeaderValues;
    }

}
