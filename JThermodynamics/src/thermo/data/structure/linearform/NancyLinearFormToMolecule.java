/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.linearform;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.SingleElectron;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.utilities.MoleculeUtilities;

/** Convert the Nancy Linear form string to Molecule
 *
 * A call to convert(String linearform) produces the
 * Molecule class directly from the Nancy linear form.
 * This is the top level routine calling:
 *  <ul>
 * <li> processString(): producing the linked list of AtomGroupStringNode
 *       representing the linear form.
 * <li> convertToMolecule(): Which takes the AtomGroupStringNode class and
 *       produces the Molecule class. This makes the first call to the
 *       recursive routine addNodeToMolecule.
 * </ul>
 *
 * The atom labels used in the linear form are the ones used for the atoms
 * in the Molecule form (except that they are converted to uppercase). Further
 * processing of the these atoms, especially the meta atoms is done elsewhere.
 *
 * @author blurock
 */
public class NancyLinearFormToMolecule {
    public boolean detectAromaticity = false;
    CorrectLinearForm correctForm;
    String linearForm;
    /** The current character.
     *
     * This keeps track of the current parsing position.
     * This routine is accessed and updated only through the
     * following procedures:
     * <ul>
     * <li> currentCharacter(): Where the current position is accessed without
     * updating the pointer
     * <li> currentCharacter(int n) Accesses the nth character beyond the
     * current character without updating the pointer.
     * <li> nextCharacter(): The pointer is incremented and the next character
     * returned.
     * </ul>
     *
     *
     */
    int currentCharPosition;
    /** The Nancy linear form converted to a linked list
     *  This is the first atom element in the linked list
     */
    public AtomGroupStringNode moleculeNodeForm;
    /** Keeps track of rings when converting from the
     * linked list form to Molecule form
     * <ul>
     * <li> Integer: The ring number used in the Nancy Linear form
     * <li> Atom: The Molecule atom for the ring connection
     * </ul>
     */
    Hashtable<Integer, Atom> ringConnections;
    /** Keeps track of rings when converting from the
     * linked list form to Molecule form
     * <ul>
     * <li> Integer: The ring number used in the Nancy Linear form
     * <li> AtomGroupStringNode: The linked list atom for the ring connection
     * </ul>
     */
    Hashtable<Integer, AtomGroupStringNode> ringInfo;
    private ThermoSQLConnection connection;

    /** The constructor
     * @param c
     * @throws SQLException 
     */
    public NancyLinearFormToMolecule(ThermoSQLConnection c) throws SQLException {
        connection = c;
        correctForm = new CorrectLinearForm(connection);
        ringConnections = new Hashtable<Integer, Atom>();
        ringInfo = new Hashtable<Integer, AtomGroupStringNode>();
    }

    /** The top call to convert the Nancy linear form to a Molecule
     *
     * @param linearform The string in Nancy linear form
     * @return The molecule
     */
    public Molecule convert(String linearform) throws SQLException {
            Molecule molecule = null;
            try {
                linearForm = correctForm.correctNancyLinearForm(linearform);
                currentCharPosition = 0;
                moleculeNodeForm = processString();
//        System.out.println("============= Top =============");
//        System.out.println(moleculeNodeForm.toString());
//        System.out.println("-------------------------------------");
                molecule = convertToMolecule();
                //StructureAsCML cml = new StructureAsCML(molecule);
                //molecule = cml.getMolecule();
            } catch (NullPointerException ex) {
                String reason = "Illegal Nancy Linear Form: '" + linearform + "'";
                throw new SQLException(reason);
            }

        return molecule;
    }
/** The main (top) loop for processing the linear form
 *
 * The Nancy linear form is made up of atom groups,
 * consisting of a main atom with some simple connections,
 * connected together with specific bonding information.
 *
 * This routine goes linearly through the set of atom groups.
 * Each call to atomGroup() parses both the atom and simple connecting
 * groups (and addition information about radicals and rings) and finally
 * the bonding of that group to the next atom.
 *
 * Basic structure:
 * <ul>
 * <li> Check whether the string is parsed: doneParsing()
 * <li> Isolate the first entity (atom or atom group)
 * <li> Loop through the next entities (atom or atom group)
 * </ul>
 *
 * The first atom listed is the top node. The next in the series is
 * given in the 'mainConnection' of that node. This forms a simple
 * linked list. The first node listed is the first link and that is
 * what is returned by this procedure.
 *
 * @return The first atom in the string of atoms.
 */
    AtomGroupStringNode processString() throws SQLException {
        AtomGroupStringNode top = null;
        boolean notdone = !doneParsing();
        top = atomGroup();
//        System.out.println("processString():  top=\n " + top.toString());
        AtomGroupStringNode node = top;
        while (node != null) {
            AtomGroupStringNode next = atomGroup();
            if (next != null) {
//                System.out.println("processString():  next=\n " + next.toString());
                node.addMainConnection(next);
            }
            node = next;
        }
        return top;
    }
/** The current parsing character without updating pointer
 * @return The current character
 */
    char currentCharacter() {
        return currentCharacter(0);
    }

/** The nth character from the current parsing character without updating pointer
 * @return The current character
 */
    char currentCharacter(int n) {
        char c = 0;
        if (currentCharPosition + n < linearForm.length()) {
            c = linearForm.charAt(currentCharPosition + n);
        }
        return c;
    }
/** The increment the point and return the next parsing character
 * @return The next character
 */
    char nextCharacter() {
        currentCharPosition++;
        return currentCharacter();
    }
/**
 * 
 * @param node The current parsing node
 * @param molecule The molecule being build
 * @param previous The previous node, this atom will be connected to it
 * @param prevatm The previous atom (of previous node) used for bonding
 * @param mainconnection true if a 'main' connection (distinquishes between connecting central atoms and subatoms)
 */
    private void addNodeToMolecule(AtomGroupStringNode node,
                        Molecule molecule,
                        AtomGroupStringNode previous,
                        Atom prevatm,
                        boolean mainconnection,
                        int bondorder) throws NullPointerException {
//        System.out.println("addNodeToMolecule:");
//        System.out.println(node.toString());

        LinearFormAtomGroupString atminfo = node.atomElement;
        Atom atm = null;
        String atmS = atminfo.atomGroup.toUpperCase();
        if (atminfo.endOfCycle) {
            Integer cycle = new Integer(atminfo.cycleNumber);
            Atom connect = ringConnections.get(cycle);
            Bond bnd = new Bond(connect, prevatm);
            bnd.setFlag(CDKConstants.ISAROMATIC, previous.atomElement.aromaticBond);
            molecule.addBond(bnd);
        } else if (atminfo.bridgeToCycleAtom) {
            Integer cycle = new Integer(atminfo.cycleNumber);
        //atm = ringConnections.get(cycle);
        //node = ringInfo.get(cycle);
        } else if (singleAtom(atmS)) {
            atm = new Atom(atmS);
            if (atminfo.aromaticBond) {
            }
        } else {
            // Need to change
            atm = new Atom(atminfo.atomGroup.toUpperCase());
        }
        if (atm != null) {
            atm.setFlag(CDKConstants.ISAROMATIC, atminfo.aromaticBond);
            molecule.addAtom(atm);
            if (atminfo.radical) {
                SingleElectron electron = new SingleElectron(atm);
                molecule.addElectronContainer(electron);
            }
            if (atminfo.cycleNumber > 0) {
                Integer cycle = new Integer(atminfo.cycleNumber);
                ringConnections.put(cycle, atm);
                ringInfo.put(cycle, node);
            }
            if (previous != null) {
                LinearFormAtomGroupString prevatminfo = previous.atomElement;
                int bndorder = atminfo.bondOrder;
                //if(bondorder > 1)
                    //System.out.println(" Bond order: " + bndorder + ": " + bondorder);
                //if(bondorder > 1) bndorder = bondorder;
                //if(!mainconnection)
                    //bndorder = atminfo.bondOrder;
                if (!atminfo.bridgeToCycleAtom) {
                    if (bndorder == 1) {
                        Bond bnd = new Bond(atm, prevatm, Order.SINGLE);
                        molecule.addBond(bnd);
                        boolean aromatic = prevatm.getFlag(CDKConstants.ISAROMATIC) && atm.getFlag(CDKConstants.ISAROMATIC);
                        bnd.setFlag(CDKConstants.ISAROMATIC, aromatic);
                        if (atminfo.multiplicity > 1) {
                            int multiplicity = atminfo.multiplicity - 1;
                            while (multiplicity > 0) {
                                Atom add = new Atom(atm);
                                molecule.addAtom(add);
                                bnd = new Bond(prevatm, add, Order.SINGLE);
                                molecule.addBond(bnd);
                                multiplicity--;
                            }
                        }
                    } else if (bndorder == 2) {
                        Bond bnd = new Bond(atm, prevatm, Order.DOUBLE);
                        molecule.addBond(bnd);
                    } else if (bndorder == 3) {
                        Bond bnd = new Bond(atm, prevatm, Order.TRIPLE);
                        molecule.addBond(bnd);
                    }
                }
            }
        }
        Iterator<AtomGroupStringNode> connections = node.connections.iterator();
        Iterator<Integer> bondorders = node.bondorders.iterator();
        while (connections.hasNext()) {
            Integer border = bondorders.next();
            AtomGroupStringNode connection = connections.next();
            addNodeToMolecule(connection, molecule, node, atm,false,border.intValue());
        }
        if (node.mainConnection != null) {
            if (atminfo.bridgeToCycleAtom) {
                Integer cycle = new Integer(atminfo.cycleNumber);
                atm = ringConnections.get(cycle);
                previous = ringInfo.get(cycle);
                addNodeToMolecule(node.mainConnection, molecule, previous, atm,true,1);
            } else {
            addNodeToMolecule(node.mainConnection, molecule, node, atm,true,1);
            }
        }
    }

    private boolean singleAtom(String atm) {
        return atm.length() == 1;
    }
/** Detect the aromatic bond character
 * An aromatic bond is indicated by the ampersand.
 *
 * @return true if an aromatic bond
 */
    private boolean aromaticBond() {
        boolean ans = false;
        char c = currentCharacter();
        if (c == '&') {
            ans = true;
            nextCharacter();
        }

        return ans;
    }

    /** Detect the next bonded group.
     * A bonded group has the following elements
     * <ul>
     * <li> The first is the main connecting atom (top of the node)
     * <li> The next is what this atom is connected to. Could be several.
     * <ul>
     * <li> Could be a single atom
     * <li> Could be a subgroup
     * </ul>
     * <li> Radical Indicator
     * <li> Cycle Indicator
     * <li> Bonding to the next node.
     * </ul>
     *
     * This forms a node, meaning an atom (meta atom), and creates its immediate
     * connections of other nodes.
     */
    private AtomGroupStringNode atomGroup() throws SQLException {
        // Isolate the current node
        AtomGroupStringNode node = singleAtomMetaAtom();
        if (node != null) {
            node.atomElement.cycleNumber = cycleIndication(node.atomElement.cycleNumber);
            // Isolate the first connection
            if(radicalIndication())
               node.atomElement.radical = true;
            AtomGroupStringNode atm = singleAtomMetaAtom();
            /* Check if a subgroup is connected directly to the main node
             * Takes care of case like c(ch3)h2/ch3 for example.
             */
            if(radicalIndication())
               node.atomElement.radical = true;
            int bondorder = 1;
            if(atm == null && subgroup()) {
                char c1 = nextCharacter();
                bondorder = bondOrder();
                atm = processSubGroup();
                atm.atomElement.bondOrder = bondorder;
                
            }
            /* This loops over all connecting atoms, the end of the connecting atoms
             * can occur if the end of the string is encountered or if bonds are encountered.
            */
            char ccc = currentCharacter();
            while (atm != null) {
                // Extract the multiplicity from the string (if none, one is returned)
                //atm.atomElement.multiplicity = groupMultiplicity();
                int multiplicity = groupMultiplicity();
                atm.atomElement.multiplicity = 1;
                // add the current atom to the connection list of the main node.
                while(multiplicity > 0) {
                    node.addConnection(atm,bondorder);
                    multiplicity--;
                }
                atm = null;
                node.atomElement.cycleNumber = cycleIndication(node.atomElement.cycleNumber);
                if (node.atomElement.cycleNumber != 0) {
                    node.atomElement.radical = radicalIndication();
                } else if (radicalIndication()) {
                    node.atomElement.radical = true;
                    node.atomElement.cycleNumber = cycleIndication(node.atomElement.cycleNumber);
                }
                if (subgroup()) {
                    char c1 = nextCharacter();
                    node.atomElement.bondOrderForward = bondOrder();
                    atm = processSubGroup();
                } else {
                    atm = singleAtomMetaAtom();
                }
            }
        } else if (endOfCycle()) {
            node = processEndOfCycle();
        } else if (continuingCycle()) {
            node = processContinuingCycle();
        }
        if (node != null) {
            node.atomElement.cycleNumber = cycleIndication(node.atomElement.cycleNumber);
            node.atomElement.aromaticBond = aromaticBond();
            //if(node.atomElement.bondOrderForward > 1)
                //System.out.println("Current Character: " + currentCharacter());
            node.atomElement.bondOrderForward = bondOrder();

            //System.out.println("============= atomGroup =============");
            //System.out.println(node.toString());
            //System.out.println("-------------------------------------");
        }
        return node;
    }
/** The bond order is the number of back slashes encountered. Default is one.
 *
 * The number of back slashes is counted to give the bond order.
 * If none are found (probably because of the end of the string), then one is returned.
 * @return The detected bond order, default is one.
 */
    private int bondOrder() {
        int order = 1;
        char c = currentCharacter();
        if (c == '/') {
            order = 0;
            while (c == '/') {
                order++;
                c = nextCharacter();
            }
        }
        return order;
    }
/** Top level routine to convert the linked atom list, moleculeNodeForm,
 * derived from the Nancy linear form to a molecule
 *
 * This is the top level call to addNodeToMolecule
 *
 * @return the molecule
 */
    private Molecule convertToMolecule() throws NullPointerException {
        Molecule molecule = new Molecule();
        try {
            addNodeToMolecule(moleculeNodeForm, molecule, null, null, true, 1);
            MoleculeUtilities.assignIDs(molecule);
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
            detectAromaticity = CDKHueckelAromaticityDetector.detectAromaticity(molecule);
        } catch (CDKException ex) {
            Logger.getLogger(NancyLinearFormToMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return molecule;
    }
/** Decide the number of the last connection
 * This is called right after a connecting atom.
 * If the current character (checked by this routine) is a number,
 * then it is a multiplicity, meaing this is the number
 * connecting atoms of that type.
 *
 * The number can be multiple digits
 *
 * If the next character is not a multiplicity, then the default one is returned.
 *
 * @return The integer multiplicity
 */
    private int groupMultiplicity() {
        int mult = 1;
        char c = currentCharacter();
        if (c >= '0' && c <= '9') {
            String multS = new String("");
            while (c >= '0' && c <= '9') {
                multS += c;
                c = nextCharacter();
            }
            mult = Integer.parseInt(multS);
        }
        return mult;
    }
/** Isolate and convert the subgroup.
 *
 * The subgroup is isolated taking into account that
 * the subgroup itself could have subgroups. The set of
 * nested parentheses are looped through. The end of the subgroup
 * is indicated by a right paren and there are no additional left parens to
 * take care of (as indicated by the variable subs).
 *
 * Once the string is isolated, then it is treated as a complete
 * Nancy linear string. The resulting node from the convert
 * routine is given as the return
 *
 * @return The top node of the processed string
 */
    private AtomGroupStringNode processSubGroup() throws SQLException {
        int subs = 1;
        String substring = new String("");
        char c = currentCharacter();
        while (subs > 0) {
            if (c == ')') {
                subs--;
                if (subs > 0) {
                    substring += c;
                }
            } else if (c == '(') {
                subs++;
                substring += c;
            } else {
                substring += c;
            }
            c = nextCharacter();
        }
        //nextCharacter();
        NancyLinearFormToMolecule sublinear = new NancyLinearFormToMolecule(connection);
        sublinear.convert(substring);
        return sublinear.moleculeNodeForm;
    }
/** This detects a radical
 * A radical is indicated by a dot surrounded by parens.
 * If this set of three characters is found, then then
 * a radical is found and true is returned
 *
 * @return true if a radical is indicated.
 */
    private boolean radicalIndication() {
        boolean ans = false;
        char c = currentCharacter();
        if (c == '(') {
            if (currentCharacter(1) == '.' && currentCharacter(2) == ')') {
                ans = true;
                nextCharacter();
                nextCharacter();
                nextCharacter();
            }
        }
        return ans;
    }
/** Decides whether this is the beginning of a cycle
 *
 * A cycle is indicated by sharp (#) and then a number surrounded by parentheses.
 * For example (#1).
 * This procedure detects whether the next 2 characters are a left paren
 * followed by a sharp.
 * If so, then the next set of characters up to the right paren are isolated and converted to a number.
 *
 * @param current The integer to return if a cycle is not found
 * @return The cycle number (or the value of current is no cycle start is indicated).
 */
    private int cycleIndication(int current) {
        int ans = current;
        char c = currentCharacter();
        if (c == '(') {
            if (currentCharacter(1) == '#') {
                nextCharacter();
                char numC = nextCharacter();
                String numS = new String("");
                while (numC != ')') {
                    numS += numC;
                    numC = nextCharacter();
                }
                nextCharacter();
                ans = Integer.valueOf(numS);
            }
        }
        return ans;
    }
/** This isolates the next atom (single character) or meta atom (string of characters surrounded by quotes.
 *
 * Allowed single character atom names are upper and lower case letters
 * Any set of characters can be in quoted meta atom names (except for single quote itself).
 *
 * @return The AtomGroupStringNode initialized with the name of the group, null if not a legal atom or meta atom
 */
    private AtomGroupStringNode singleAtomMetaAtom() {
        String atom = null;
        char c = currentCharacter();
        AtomGroupStringNode node = null;
        // Signifies the start of a larger group name
        if (c == '\'') {
            atom = "";
            char cg = nextCharacter();
            while (cg != '\'') {
                atom += cg;
                cg = nextCharacter();
            }
            nextCharacter();
        } else if (c == '.') {
            nextCharacter();
            node = singleAtomMetaAtom();
            node.atomElement.radical = true;
        } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            atom = new String();
            atom += c;
            nextCharacter();
        }
        
        if (atom != null && node == null) {
            node = new AtomGroupStringNode(atom);
        }
        return node;
    }
    /** Decides whether the current string is parsed completely.
     * This is done by checking whether the currentCharPosition is beyond the length of the string.
     *
     * @return
     */
    private boolean doneParsing() {
        return currentCharPosition >= linearForm.length();
    }
/** This detects the beginning of a subgroup.
 * A subgroup is a string surrounded by parentheses, that is not a ring indication (meaning a sharp).
 * This routine just detects the proper left paren.
 *
 * @return true if the beginning of the subgroup is detected.
 */
    private boolean subgroup() {
        boolean ans = false;
        if (currentCharacter() == '(' && currentCharacter(1) != '#' && currentCharacter(1) != '.') {
            ans = true;
        }
        return ans;
    }

    private boolean endOfCycle() {
        boolean ans = false;
        char c = currentCharacter();
        if (c >= '0' && c <= '9') {
            ans = true;
        }
        return ans;
    }

    private AtomGroupStringNode processEndOfCycle() {
        char c = currentCharacter();
        nextCharacter();
        String cycle = new String();
        cycle += c;
        AtomGroupStringNode node = new AtomGroupStringNode(cycle);
        node.atomElement.endOfCycle = true;
        node.atomElement.cycleNumber = Integer.parseInt(cycle);
        return node;
    }

    private boolean continuingCycle() {
        char c = currentCharacter();
        return c == ',';
    }

    private AtomGroupStringNode processContinuingCycle() {
        // Skip over comma and get number
        char c = nextCharacter();
        // go past numbder
        nextCharacter();
        String cycle = new String();
        cycle += c;
        AtomGroupStringNode node = new AtomGroupStringNode(cycle);
        node.atomElement.bridgeToCycleAtom = true;
        node.atomElement.cycleNumber = Integer.parseInt(cycle);
        return node;
    }
}
