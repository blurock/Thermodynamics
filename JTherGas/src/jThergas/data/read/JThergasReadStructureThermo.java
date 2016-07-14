/*
 */

package jThergas.data.read;

import jThergas.data.JThermgasThermoStructureDataPoint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import jThergas.exceptions.JThergasReadException;

/** Top level class to parse an entire file of thermo data points.
 *
 * <ul>
 * <li> {@link JThergasReadStructureThermo#readAndParseFile(java.io.File) readAndParseFile}:
 * top level routine to parse file. Reading the
 *     file to a string and then calling {@link JThergasReadStructureThermo#parseSet() parseSet()}
 * <li> {@link JThergasReadStructureThermo#parseSet() parseSet()}: Parses over the blocks, until no lines are left,  calling
 *     {@link JThergasReadStructureThermo#parseBlock() parseBlock()}
 * <li> {@link JThergasReadStructureThermo#parseBlock() parseBlock()}:
 * Parse a single block with {@link JThermgasThermoStructureDataPoint} class
 * </ul>
 *
 * The result is a vector of {@link JThermgasThermoStructureDataPoint} classes
 * with the parsed thermodynamic data. This data is accessed by {@link JThergasReadStructureThermo#getData() getData()}.
 *
 * @author blurock
 */
public class JThergasReadStructureThermo {
    // The block parsing tokenizer
    JThergasTokenizer fileTokenized;
    // The vector of parsed block information
    private Vector<JThermgasThermoStructureDataPoint> data;
    // accumulated error information (used before exception is thrown
    StringBuffer errorBuffer = new StringBuffer();
    
    /** The constructor
     */
    public JThergasReadStructureThermo() {
        
    }
    public void readAndParse(InputStream inp) throws JThergasReadException, IOException {
         data = new Vector<JThermgasThermoStructureDataPoint>();
        ReadFileToString read = new ReadFileToString();
        InputStreamReader inpreader = new InputStreamReader(inp);
        BufferedReader breader = new BufferedReader(inpreader);
        readAndParse(breader);
    }
    /** Top level procedure to read in the file of thermo information
     *
     * The vector of parsed blocks is initialized to the empty vector
     *
     * This reads in the file to a string (ReadFileToString) and
     * then calls {@link JThergasReadStructureThermo#parseSet() parseSet()}.
     * A tokenizer is set up ({@link JThergasTokenizer}) to aid in
     * parsing the blocks.
     *
     * @param f The file (name)
     * @throws jthergas.exceptions.JThergasReadException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void readAndParse(File f)   throws JThergasReadException, FileNotFoundException, IOException {
        data = new Vector<JThermgasThermoStructureDataPoint>();
        FileReader reader = new FileReader(f);
        BufferedReader breader = new BufferedReader(reader);
        readAndParse(breader);
        }
    /** Top level procedure to read in the file of thermo information
     *
     * The vector of parsed blocks is initialized to the empty vector
     *
     * This reads in the file to a string (ReadFileToString) and
     * then calls {@link JThergasReadStructureThermo#parseSet() parseSet()}.
     * A tokenizer is set up ({@link JThergasTokenizer}) to aid in
     * parsing the blocks.
     *
     * @param breader The buffered reader with Thergas information
     * @throws jthergas.exceptions.JThergasReadException
     * @throws IOException 
     */
        public void readAndParse(BufferedReader breader) throws IOException, JThergasReadException {
        ReadFileToString read = new ReadFileToString();
        read.read(breader);
         fileTokenized = new JThergasTokenizer(read.outputString);
        parseSet();
    }
    /** Parse the data if already in a string
     * 
     * This parses the string with the call to
     * {@link JThergasReadStructureThermo#parseSet() parseSet()}.
     * A tokenizer is set up ({@link JThergasTokenizer}) to aid in
     * parsing the blocks.
     * 
     * 
     * @param datastring The Thergas information in a string
     * @throws jthergas.exceptions.JThergasReadException
     */
    public void readAndParse(String datastring) throws JThergasReadException {
        data = new Vector<JThermgasThermoStructureDataPoint>();
        fileTokenized = new JThergasTokenizer(datastring);
        parseSet();
    }
    /** Create the class to parse the block data.
     *
     * This allows overloading, if other parsing is required
     * {@link JThermgasThermoStructureDataPoint } is the base.
     *
     * @return {@link JThermgasThermoStructureDataPoint } structure
     */
    public JThermgasThermoStructureDataPoint getNewThermoDataPoint() {
        return new JThermgasThermoStructureDataPoint();
    }
    /** Loop over blocks
     *
     * If the lines end with not enough to form a complete block,
     * and exception error is returned.
     * This loops and calls {@link JThergasReadStructureThermo#parseBlock() parseBlock()};
     *
     * @throws jthergas.exceptions.JThergasReadException
     */
    protected void parseSet()   throws JThergasReadException {
        while(fileTokenized.countTokens() > 2) {
            parseBlock();
        }
        if(errorBuffer.length() > 0) {
            throw new JThergasReadException("Error found in the following Blocks: not added to database\n" + errorBuffer.toString());
        }
    }
    
    /** A single block is parsed and fills a {@link JThermgasThermoStructureDataPoint} structure
     *
     * The block string is retrieved with the tokenizer, and then
     * parsed, filling in the JThermgasThermoStructureDataPoint class.
     * The class is then stored in the data vector.
     *
     * @throws jthergas.exceptions.JThergasReadException
     */
    protected void parseBlock()   throws JThergasReadException {
        JThermgasThermoStructureDataPoint point = getNewThermoDataPoint();
        fileTokenized.readBlock();
        //System.out.println("Parse Structure Data:");
        //System.out.println(fileTokenized.line1 + "\n" + fileTokenized.line1a);
        //System.out.println("Parse Therm Data:");
        //System.out.println(fileTokenized.line2);
        
        //System.out.println("Parse Atomic Data:");
        //System.out.println(fileTokenized.line3);
        try {
        point.parse(fileTokenized);
        //System.out.println(point.writeToString());
        } catch(JThergasReadException ex) {
            errorBuffer.append("Exception: ==================================================================\n");
            errorBuffer.append(ex.writeToString()).append("\n");
            errorBuffer.append("------------ The Block--------------------\n");
            errorBuffer.append(fileTokenized.line1).append("\n");
            if(fileTokenized.line1a.length() > 0)
                errorBuffer.append(fileTokenized.line1a).append("\n");
            errorBuffer.append(fileTokenized.line2).append("\n");
            errorBuffer.append(fileTokenized.line3).append("\n");
        } catch(NumberFormatException ex) {
            errorBuffer.append("Exception: ==================================================================\n");
            errorBuffer.append("------------ The Block--------------------\n");
            errorBuffer.append(fileTokenized.line1).append("\n");
            if(fileTokenized.line1a.length() > 0)
                errorBuffer.append(fileTokenized.line1a).append("\n");
            errorBuffer.append(fileTokenized.line2).append("\n");
            errorBuffer.append(fileTokenized.line3).append("\n");
        }
        getData().add(point);
        
    }

    /** The set of parsed blocks in a vector
     *
     * @return The vedctor of parsed blocks
     */
    public Vector<JThermgasThermoStructureDataPoint> getData() {
        return data;
    }

}
