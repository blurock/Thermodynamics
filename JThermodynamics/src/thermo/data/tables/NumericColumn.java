/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.tables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/** Numeric Column
 * Basically an {@link ArrayList} of {@link Double} values
 * It is the main element of the {@link  CalculationMatrix structure}.
 *
 * It is derived from {@link ArrayList<Double>}
 *
 * @author edwardblurock
 */
public class NumericColumn extends ArrayList<Double> {
    /** empty constructor
     *
     */
    public NumericColumn() {
    }
    /** Constructor with dimension
     *  
     * @param dim the dimension
     */
    public NumericColumn(int dim) {
        super(dim);
    }
    /** Append a value to the column
     *
     * @param value the double value to add to the end of the column
     */
    public void addValue(double value) {
        this.add(new Double(value));
    }
    /** Add a set of double values to a column
     *
     * @param values The array of double values
     */
    public void add(double[] values) {
        for(int i=0;i<values.length;i++) {
            this.addValue(values[i]);
        }
    }
    /** Add a set of values represented as a delimited string
     *
     * @param line The string of space/tab separated values
     */
    public void add(String line) {
        StringTokenizer tok = new StringTokenizer(line);
        while(tok.hasMoreTokens()) {
            String numS = tok.nextToken();
            Double numD = new Double(numS);
            this.add(numD);
        }
    }
    /** Modify the value in position i with a value
     *
     * @param value The modified value
     * @param i the index in the column
     * @throws Exception If the index is outside the range
     */
    public void modify(double value, int i) throws Exception {
        if(i < this.size() && i > 0) {
            Double valueD = new Double(value);
            this.add(i, value);
        } else {
            String errorS = "Adding to vector beyond dimensions: " + i + " > " + this.size();
            throw new Exception(errorS);
        }
    }
    /** Find the nearest lower bound in the column for the value
     *
     * The column values are sequencially searched until the value
     * is between two column values. The index of the lower value
     * is returned
     *
     * @param value The value to search for
     * @return The index of the largest column value which is less than the input value
     * @throws NumberFormatException If an index cannot be found (value outside of range)
     */
    public int lowerBound(double value) throws NumberFormatException {
        int index = 0;
        Iterator<Double> iter = this.iterator();
        boolean notdone = true;
        while(notdone) {
            Double vD = iter.next();
            if(vD.doubleValue() > value) {
                notdone = false;
            } else {
                notdone = iter.hasNext();
                index++;
            }
        }
        if(index >= this.size()) throw new NumberFormatException("Out of Bound");

        return index-1;
    }
    /** The fractional distance of the value between the lower and upper column values
     *
     * @param index The index of the lower bound
     * @param value The value
     * @return The fraction distance between C(index) and C(index+1)
     */
    public double fractionBetweenValues(int index, double value) {
        double vlower = this.get(index);
        double vupper = this.get(index+1);
        double fraction = (value - vlower)/(vupper - vlower);
        return fraction;
    }
    /** Convert the column to a string
     *
     * @return column as string (can be used as string in constructor)
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        Iterator<Double> iter = this.iterator();
        while(iter.hasNext()) {
            Double num = iter.next();
            buf.append(num.toString());
            buf.append("\t");
        }
        return buf.toString();
    }
}
