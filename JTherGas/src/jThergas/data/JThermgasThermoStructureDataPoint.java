
package jThergas.data;

import jThergas.data.read.JThergasTokenizer;
import jThergas.data.structure.JThergasStructureData;
import jThergas.data.structure.JThergasAtomicStructure;
import jThergas.data.thermo.JThergasThermoData;
import jThergas.exceptions.JThergasReadException;

/** The information in a single Thergas block of thermo information
 *
 * The information of each line is parsed using three data structures:
 * <ul>
 * <li> {@link  jthergas.data.structure.JThergasStructureData JThergasStructureData}
 * <li> {@link JThergasAtomicStructure}
 * <li> {@link JThergasThermoData}
 * </ul>
 * All the information of the block is held in these three classes.
 * 
 * The parse procedure parses
 * @version 2008.10
 * @author blurock
 */
public class JThermgasThermoStructureDataPoint {
    /**
     * This gives the number of atoms
     */
    private JThergasThermoData thermodynamics;
    private JThergasStructureData structure;
    private JThergasAtomicStructure atomicStructure;
    
    
    /** Parse through a single block of Thermodynamic information
     * {@link JThergasTokenizer} is used to retrieve the 3 or 4 lines of the block
     * {@link jthergas.data.thermo.JThergasThermoData},
     * {@link jthergas.data.structure.JThergasStructureData} and
     * {@link jthergas.data.structure.JThergasAtomicStructure}
     * are used to parse the information out of the lines
     * 
     * @param fileTokenized The line information
     * @throws jthergas.exceptions.JThergasReadException
     */
    public  void parse(JThergasTokenizer fileTokenized)  throws JThergasReadException {
        thermodynamics = new JThergasThermoData();
        structure = new JThergasStructureData();
        atomicStructure = new JThergasAtomicStructure();
        
        try {
            getStructure().parse(fileTokenized.line1, fileTokenized.line1a,fileTokenized.group);
            getThermodynamics().parse(fileTokenized.line2);
            getAtomicStructure().parse(fileTokenized.line3);
        } catch(JThergasReadException ex) {
            throw new JThergasReadException("ERROR at line: " + fileTokenized.getLineNumber() + ", block: " + fileTokenized.getBlockNumber() + "\n" + ex.writeToString());
        }
    }
    /** This writes the data in (close to) the standard form of Thergas
     * 
     * @return The multi-lined string with the information.
     */
    public String writeToString() {
        StringBuffer buf = new StringBuffer();
        String structureS = getStructure().writeToString();
        buf.append(structureS + "\n");
        String thermoS = getThermodynamics().writeToString();
        buf.append(thermoS + "\n");
        String atomicS = getAtomicStructure().writeToString();
        buf.append(atomicS + "\n");
        return buf.toString();
    }

    /** Get thermodynamic information
     *
     * @return thermodynamic information
     */
    public JThergasThermoData getThermodynamics() {
        return thermodynamics;
    }

    /** Get the structural information
     *
     * @return the structural information
     */
    public JThergasStructureData getStructure() {
        return structure;
    }

    /** Get the atomic structure information
     *
     * @return the atomic structure information
     */
    public JThergasAtomicStructure getAtomicStructure() {
        return atomicStructure;
    }

}
