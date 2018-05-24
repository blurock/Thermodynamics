package jThergas.data.structure;

import jThergas.exceptions.JThergasReadException;
import java.util.Vector;

/**Parse the atomic structure line
     *
     * The atomic structure is represented by 20 I3 (three character integer)
     * fields with the number of the corresponding atom.
     * The atoms, in order are:
     * Br,C,Cd,Cl,Ch,F,Ge,H,Hg,I,N,O,P,Pb,S,Si,Sn,Ti,V,Zn
     *
 *
 * @author Edward S. Blurock
 */
public class JThergasAtomicStructure {
    Vector atoms = new Vector();
    int tableNumber;
    int groupNumber;
    String numberOfAtomsA1;
    int numberOfAtoms;
    String dateString;
    
    int referenceAtomNumbers[] = new int[20];
    String referenceAtomNames[] = new String[20];
    
    /** The constructor
     * this sets up the 20 atomic numbers of the elements in the line
     * 
     */
    public JThergasAtomicStructure() {
        referenceAtomNames[0] = "Br";
        referenceAtomNumbers[0] = 35;
        referenceAtomNames[1] = "C";
        referenceAtomNumbers[1] = 6;
        referenceAtomNames[2] = "Cd";
        referenceAtomNumbers[2] = 48;
        referenceAtomNames[3] = "Cl";
        referenceAtomNumbers[3] = 17;
        referenceAtomNames[4] = "Ch";
        referenceAtomNumbers[4] = 0;
        referenceAtomNames[5] = "F";
        referenceAtomNumbers[5] = 9;
        referenceAtomNames[6] = "Ge";
        referenceAtomNumbers[6] = 32;
        referenceAtomNames[7] = "H";
        referenceAtomNumbers[7] = 1;
        referenceAtomNames[8] = "Hg";
        referenceAtomNumbers[8] = 80;
        referenceAtomNames[9] = "I";
        referenceAtomNumbers[9] = 53;
        referenceAtomNames[10] = "N";
        referenceAtomNumbers[10] = 7;
        referenceAtomNames[11] = "O";
        referenceAtomNumbers[11] = 8;
        referenceAtomNames[12] = "P";
        referenceAtomNumbers[12] = 15;
        referenceAtomNames[13] = "Pb";
        referenceAtomNumbers[13] = 82;
        referenceAtomNames[14] = "S";
        referenceAtomNumbers[14] = 16;
        referenceAtomNames[15] = "Si";
        referenceAtomNumbers[15] = 14;
        referenceAtomNames[16] = "Sn";
        referenceAtomNumbers[16] = 50;
        referenceAtomNames[17] = "Ti";
        referenceAtomNumbers[17] = 22;
        referenceAtomNames[18] = "V";
        referenceAtomNumbers[18] = 23;
        referenceAtomNames[19] = "Zn";
        referenceAtomNumbers[19] = 30;
    }
    
    /** Parse the atomic structure line
     * <ul>
     * <li> (0,2) table number
     * <li> (3,6) group number
     * <li> (7,8) line number
     * <li> (11,12) number of atoms
     * <li> 20 I3  the number of each atom
     * <li> (76,79) date
     * ><ul>
     *
     *
     * @param line the atomic information line
     * @throws jthergas.exceptions.JThergasReadException
     */
    public void parse(String line) throws JThergasReadException {
        String tableNumberS     = line.substring(1,3).trim();
        String groupNumberS     = line.substring(3, 7).trim();
        String lineS            = line.substring(7,9);
        numberOfAtomsA1  =  line.substring(11,13);
        String numberOfAtomsS  = line.substring(13, 15).trim();
        int count = 0;
        String atomsS[] = new String[20];
        for(int p=15;p<75;p+=3) {
            atomsS[count++] = line.substring(p, p+3).trim();
        }
        dateString = line.substring(76);
        
        try {
        tableNumber    = Integer.parseInt(tableNumberS);
        groupNumber    = Integer.parseInt(groupNumberS);        
        numberOfAtoms = Integer.parseInt(numberOfAtomsS);
        for(int i = 0; i<20;i++) {
            if(atomsS[i].length() == 0) {
                boolean add = atoms.add(new Integer(0));
            }
            else {
                atoms.add(i,new Integer(atomsS[i]));
            }
        }
        
        } catch (NumberFormatException ex) {
            throw new JThergasReadException("Error in Parsing Atomic Structure Line:\n" + line);
        }

    }
    
    /** Write out the atomic info in the proper format.
     *
     * @return the line
     */
    public String writeToString() {
        StringBuilder buf = new StringBuilder();
        
        buf.append(" ");
        buf.append(String.format("%2d", tableNumber));
        buf.append(String.format("%4d", groupNumber));
        buf.append(" 3 ");
        buf.append(numberOfAtomsA1);
        buf.append(String.format("%2d",numberOfAtoms));
        for(int i=0;i<20;i++) {
            Integer value = (Integer) atoms.elementAt(i);
            buf.append(String.format("%3d", value));
        }
        buf.append("  ");
        buf.append(dateString);
        return buf.toString();
    }

	public Vector getAtoms() {
		return atoms;
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public String getNumberOfAtomsA1() {
		return numberOfAtomsA1;
	}

	public int getNumberOfAtoms() {
		return numberOfAtoms;
	}

	public String getDateString() {
		return dateString;
	}

	public int[] getReferenceAtomNumbers() {
		return referenceAtomNumbers;
	}

	public String[] getReferenceAtomNames() {
		return referenceAtomNames;
	}
    
}
