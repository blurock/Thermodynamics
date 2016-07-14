/*
 */

package jThergas.data.read;

import java.util.StringTokenizer;
import jThergas.exceptions.JThergasReadException;

/** Class to parse the 3 to 4 lines of the Thergas structure files.
 *
 * With the use of readBlock, the next block is read in. The lines of the
 * block meet minimal requirements.
 *
 * The current overall line number and block number are kept track of.
 *
 * @author blurock
 */
public class JThergasTokenizer extends StringTokenizer {
    private int lineNumber = 0;
    private int blockNumber = 0;
    /**
     * The four lines that form the block are filled in with readBlock
     */
    public String line1;
    public String line1a;
    public String line2;
    public String line3;
// Place holder for current line
    private String current;
    
    /** The blocks are tagged as 'groups' it is set by a line beginning
     * with a 'g'
     */
    public boolean group = false;
    
    String blankS = new String(" ");
    JThergasTokenizer(String input) {
        super(input,"\n");
    }

    /** Read a single block in the Thergas Structure file consisting
     * of three (or four) lines meeting minimal requirements.
     *
     * There has to be at least three lines if this routine is called.
     *
     * If an error occurs, a JThergasReadException is thrown with the
     * block line number.
     * The minimal requirements for the block are:
     * <ul>
     * <li> Line 1:
     *      <ul>
     *      <li> Has to be greater than 9 characters
     *      <li> characters 7 and 8 are " 1"
     *      </ul>
     * <li> Line 1a:
     *      <ul>
     *      <li> If first character is a blank, then 1a, otherwise 2
     *      </ul>
     * <li> Line 2:
     *      <ul>
     *      <li> characters 7 and 8 are " 2"
     *      </ul>
     * <li> Line 3:
     *      <ul>
     *      <li> characters 7 and 8 are " 3"
     *      </ul>
     * </ul>
     *
     * @throws jthergas.exceptions.JThergasReadException
     */
    public void readBlock() throws JThergasReadException {
        if(this.countTokens() < 3) {
            JThergasReadException ex = new JThergasReadException("Premature End of File (incomplete block)");
            ex.assignLineNumber(getLineNumber());
            throw ex;
        }
        
        line1 = getNextToken();
        if(line1.length() < 9) lineError(1);
        //System.out.println("line1: '" + line1.substring(7, 9) + "'");
        if(!line1.substring(7, 9).equals(" 1")) lineError(1);
        line2 = this.getNextToken();
        //System.out.println(("'" + line2.substring(0, 1) + "'  " +line2.substring(0, 1).compareTo(blankS)));
        if(!line2.substring(0,1).equals(blankS)) {
            line1a = line2;
            line2 = this.getNextToken();
        } else {
            line1a = "";
        }
        //System.out.println("line2: '" + line2.substring(7, 9) + "'");
        if(!line2.substring(7, 9).equals(" 2")) lineError(2);
        line3 = this.getNextToken();
        if(!line3.substring(7, 9).equals(" 3")) lineError(3);
        blockNumber++;
    }
/** Constructs the exception JThergasReadException and throws it

 * @param n The line number of the error
 * @throws jthergas.exceptions.JThergasReadException
 */
    private void lineError(int n)  throws JThergasReadException {
            String error = "Invalid Block at block: " + getBlockNumber()
                    + " line: " + n
                    +" \n'" + current + "'";
            JThergasReadException ex = new JThergasReadException(error);
            ex.assignLineNumber(getLineNumber());
            throw ex;
    }
    /** Gets the next line skipping over non-block lines
     *
     * Lines that begin with 'g' are labeled as a group
     * and the next line is retrieved
     *
     * Lines that begin with 'c', 'a' or '!' are comments
     * and the next line is retrieved.
     *
     * @return The next block line
     */
    public String getNextToken() {
        current = this.nextToken();
        lineNumber++;
        if(current.startsWith("g")) {
            group = true;
            current = this.getNextToken();
        }
        while(current.startsWith("c") || current.startsWith("a") || current.startsWith("!")) {
            current = this.getNextToken();
        }
        return current;
    }

    /** Retrieve the current line number
     * @return The total line number within the file
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /** Retrieve the current block number
     * @return The block number read
     */
    public int getBlockNumber() {
        return blockNumber;
    }
}
