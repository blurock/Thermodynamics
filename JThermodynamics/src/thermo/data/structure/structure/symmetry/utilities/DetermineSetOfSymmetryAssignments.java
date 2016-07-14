/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry.utilities;

import thermo.data.structure.structure.symmetry.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.mcss.RMap;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.GetSubstructureMatches;
import thermo.data.structure.structure.matching.IsolateConnectedStructure;
import thermo.data.structure.structure.matching.NoStructureOverlap;
import thermo.data.structure.structure.symmetry.SymmetryMatch;
import thermo.data.structure.utilities.MoleculeUtilities;

/**
 * 
 * @author blurock
 */
public class DetermineSetOfSymmetryAssignments extends SymmetryDefinition {

    GetSubstructureMatches matches = new GetSubstructureMatches();
    NoStructureOverlap overlap = new NoStructureOverlap();
    DetermineSymmetryAssignmentsFromConnections matchassignments;
    SubstructuresFromSymmetry substructuresFromSymmetry;

    /**
     * 
     * @param symname
     * @param structure
     * @param pairlist
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public DetermineSetOfSymmetryAssignments(
            String symname,
            StructureAsCML structure,
            List<SymmetryPair> pairlist,
            DetermineSymmetryAssignmentsFromConnections matchassign) throws CDKException, ClassNotFoundException, IOException {
        super(symname, structure, pairlist);
        matchassignments = matchassign;
        substructuresFromSymmetry = new SubstructuresFromSymmetry(this);
    }

    public DetermineSetOfSymmetryAssignments(SymmetryDefinition definition,
            DetermineSymmetryAssignmentsFromConnections matchassign) {
        super(definition);
        matchassignments = matchassign;
        substructuresFromSymmetry = new SubstructuresFromSymmetry(this);
    }

    /**
     * 
     * @param structure
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     */
    public SetOfSymmetryMatches findIfMatchInStructures(AtomContainer structure) throws CDKException {
        List<SetOfSymmetryAssignments> setofassignsets = findAllSetsOfSymmetryAssignments(structure);
        return findIfMatchInStructures(setofassignsets);
    }

    public SetOfSymmetryMatches findIfMatchInStructures(List<SetOfSymmetryAssignments> setofassignsets) throws CDKException {
        SetOfSymmetryMatches setofmatches = new SetOfSymmetryMatches();
        Iterator<SetOfSymmetryAssignments> iset = setofassignsets.iterator();
        while (iset.hasNext()) {
            SetOfSymmetryAssignments assignments = iset.next();
            if (assignments != null) {
                //System.out.println("================ Symmetries =======================");
                //System.out.println(assignments.toString());
                //System.out.println("================ Symmetries =======================");
                boolean ans = table.sameSymmetry(assignments);
                if (ans) {
                    SymmetryMatch symmatch = new SymmetryMatch(assignments);
                    setofmatches.add(symmatch);
                }
            }
        }
        return setofmatches;
    }

    /**
     * 
     * @param structure
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     */
    public ArrayList<SetOfSymmetryAssignments> findAllSetsOfSymmetryAssignments(AtomContainer structure) throws CDKException {
        ArrayList<SetOfSymmetryAssignments> setofsets = new ArrayList<SetOfSymmetryAssignments>();
        if (structure.getAtomCount() >= this.getMolecule().getAtomCount()) {
            //System.out.println(MoleculeUtilities.toString(structure));
            List<List<RMap>> sets = getAtomMatches(structure);
            Iterator<List<RMap>> iset = sets.iterator();
            while (iset.hasNext()) {
                List<RMap> set = iset.next();
                //MoleculeUtilities.printCorrespondences(set, structure, this.getMolecule());
                SetOfSymmetryAssignments assignments = findSymmetryAssignments(structure, set);
                if (assignments != null) {
                    setofsets.add(assignments);
                }
            }
            //System.out.println(setofsets.toString());
            setofsets = reduceToUniqueSet(setofsets);
        }
        return setofsets;
    }

    public ArrayList<SetOfSymmetryAssignments> reduceToUniqueSet(ArrayList<SetOfSymmetryAssignments> set) {
        ArrayList<SetOfSymmetryAssignments> newset = new ArrayList<SetOfSymmetryAssignments>();
        Iterator<SetOfSymmetryAssignments> i = set.iterator();
        while (i.hasNext()) {
            SetOfSymmetryAssignments symset = i.next();
            //System.out.println("Reduce: " + symset.toString());
            Iterator<SetOfSymmetryAssignments> j = newset.iterator();
            boolean notfound = true;
            while (j.hasNext()) {
                SetOfSymmetryAssignments comp = j.next();
                if (comp.sameMatchings(symset) != null) {
                    notfound = false;
                }
            }
            if (notfound) {
                //System.out.println("Add to Set");
                newset.add(symset);
            } else {
                //System.out.println("Already there");
            }
        }
        return newset;
    }

    /**
     * 
     * @param assign
     * @return
     */
    public boolean addAssignment(SymmetryAssignment assign) {
        boolean ok = table.containsKey(assign.getGroupName());
        if (ok) {
            ok = noOverlap(assign);
            if (ok) {
                table.put(assign.getGroupName(), assign);
            }
        }
        return ok;
    }

    boolean noOverlap(SymmetryAssignment assignment) {
        boolean notfound = true;
        Iterator<String> a = assignment.getAssignmentsInMolecule().iterator();
        while (a.hasNext() && notfound) {
            String group = a.next();
            Iterator<SymmetryAssignment> i = table.values().iterator();
            while (i.hasNext() && notfound) {
                SymmetryAssignment assigns = i.next();
                notfound = !assigns.assigned(group);
            }
        }
        return notfound;
    }
    /*
     *   conncections: The set of substructures connected to the unspecified atoms in the symmetry structure
     *
     */
    protected SetOfSymmetryAssignments findSymmetryAssignments(IAtomContainer structure, List<RMap> set) throws CDKException {
        boolean ans = false;
        //System.out.println("========================================================================================");
        Hashtable<String, String> matched = null;
        SetOfSymmetryAssignments assignments = null;
        Hashtable<String, IAtomContainer> connections = findConnectedSubstructures(structure, set);
        if(connections != null) {
            //boolean uniquesubstructures = overlap.noOverlapInStructures(connections);
            boolean uniquesubstructures = true;
            if (uniquesubstructures) {
                matchassignments.determineSymmetryAssignments(connections);
                assignments = matchassignments.getAssignments();
                findCorrespondences(assignments, set, structure);
                //System.out.println("\nAssignments-----------------------------------\n" + assignments.toString());
                if(!ifProperSymmetry(assignments))
                    assignments = null;
            }
        } else {
            //System.out.println("Loop found");
        }
        return assignments;
    }
    /*
     * Loop through all the symmetry assignments of the molecule.
     * For each one, call {@link addCorrespondences}
     *
     *
     */

    protected void findCorrespondences(SetOfSymmetryAssignments assignments, List<RMap> set, IAtomContainer structure) {
        Iterator<String> ikeys = assignments.keySet().iterator();
        while (ikeys.hasNext()) {
            String key = ikeys.next();
            SymmetryAssignment assignment = assignments.get(key);
            addCorrespondences(assignment, set, structure);
        }
    }
    /* addCorrespondences
     inames:   The labels in the molecule
     * molname:  label from molecule
     * symname:  label from table structure
     * symnames: set of corresponding labels (will be added to symmetry assignments)
     *
     *
     */

    private void addCorrespondences(SymmetryAssignment assignment, List<RMap> set, IAtomContainer structure) {
//        String str = "Structure";
//        printMoleculeIDs(str, structure);
//        String m = "SymmetryMolecule";
//        printMoleculeIDs(m, getMolecule());
//        printCorrespondences(set,structure,getMolecule());
        Iterator<String> inames = assignment.getAssignmentsInMolecule().iterator();
        //Iterator<String> inames = assignment.getAssignmentsInSymmetryStructure().iterator();
        Vector<String> symnames = new Vector<String>();
//        System.out.println("addCorrespondences");
//        System.out.println(assignment.toString());
        while (inames.hasNext()) {
            String molname = inames.next();
            String symname = findSymmetryNameMatch(molname, set, structure);
//            System.out.println(molname + ":-> " + symname);
            SymmetryAssignment symmetry = this.table.findSymmetryWithMolecleAtom(symname);
//            System.out.println(symmetry.toString());
            if (symmetry == null) {
                //String str = "Structure";
                //printMoleculeIDs(str, structure);
                //String m = "SymmetryMolecule";
                //printMoleculeIDs(m, getMolecule());
                //MoleculeUtilities.printCorrespondences(set, structure, getMolecule());
                /*
                System.out.println("-------------------------------------------");
                System.out.println("null: " + molname + "\t" + symname);
                System.out.println(this.table.toString());
                System.out.println(assignment.toString());
                System.out.println("Map");
                Iterator<RMap> iter = set.iterator();
                while (iter.hasNext()) {
                    RMap map = iter.next();
                    IAtom atm1 = getMolecule().getAtom(map.getId2());
                    IAtom atm2 = structure.getAtom(map.getId1());
                    System.out.println("(" + map.getId1() + "  " + atm1.getID() + "):(" + map.getId2() + " " + atm2.getID() + ")");
                }
                 *
                 */
                //System.out.println("-------------------------------------------");
            } else {
                assignment.setSymmetryConnection(symmetry.getSymmetryConnection());
            }
            symnames.add(symname);
        }
        assignment.setAssignmentsInSymmetryStructure(symnames);
    }

    public void printMoleculeIDs(String name, IAtomContainer atms) {
        StringBuffer buf = new StringBuffer();
        buf.append(name + ":");
        for (int i = 0; i < atms.getAtomCount(); i++) {
            buf.append("(" + i + " : " + atms.getAtom(i).getID() + ")\t");
        }
        buf.append("\n");
        System.out.println(buf.toString());
    }

    /** Find the symmetry match
     *
     * @param molname The connecting atom which refers to the symmetry element
     *                (with the connected molecule)
     * @param set The correspondences between symmetry and structure
     * @param structure The structure molecule
     *
     * <ul>
     * <li> molid is the atom number in the structure which corresponds to
     *      molname (translated by getAtomFromID)
     * <li> set will be used to find the atom in the symmetry structure which refers
     * to molid. Loop through until a match of Id1.
     * <li> Once a match has been found, Id2 refers to the atom in the
     *      symmetry structure (getMolecule())
     * </ul>
     *
     * The ID of the corresponding atom is returned
     *
     *
     *
     */
    private String findSymmetryNameMatch(String molname, List<RMap> set, IAtomContainer structure) {
        String symname = null;
        int molid = getAtomFromID(molname, structure);
        Iterator<RMap> imap = set.iterator();
        boolean notfound = true;
        while (imap.hasNext() && notfound) {
            RMap map = imap.next();
            if (map.getId1() == molid) {
                notfound = false;
                Molecule mol = getMolecule();
                if (mol != null) {
                    int i = map.getId2();
                    IAtom atm = mol.getAtom(i);
                    symname = atm.getID();
                }
            }
        }
        return symname;
    }

    private int getAtomFromID(String molname, IAtomContainer structure) {
        boolean notfound = true;
        int id = 0;
        while (notfound && id < structure.getAtomCount()) {
            IAtom atm = structure.getAtom(id);
            if (atm.getID().equals(molname)) {
                notfound = false;
            } else {
                id++;
            }
        }
        return id;
    }
/* @param structure The structure to match
 * @param set List of pairs: (structure index, symmetry structure index)
 * @return  The list of the connected substructures of the unspecified atoms
 *
 *   connections: Hashtable with
 *            String: The count of the substructure
 *            IAtomContainer:  The connected substructure
 * Loop through pairs
 *   match: The symmetry structure
 *     if match is an unspecified atom in symmetry structure:
 *          matchdeunspecified: corresponding atom in structure of the unspecified atom
 *          matchedspecified:   corresponding atom in structure of the connecting atom
 *          substructure:       The corresponding substructure starting at the connecting atom
 *
 */
    protected Hashtable<String, IAtomContainer> findConnectedSubstructures(IAtomContainer structure, List<RMap> set) {
        Hashtable<String, IAtomContainer> connections = new Hashtable<String, IAtomContainer>();
        int count = 0;
        Iterator<RMap> i = set.iterator();
        boolean noloop = true;
        while (i.hasNext() && noloop) {
            RMap pair = i.next();
            int match = pair.getId2();
            if (this.getUnspecifiedAtoms().contains(match)) {
                IAtom matchedunspecified = structure.getAtom(pair.getId1());
                IAtom matchedspecified = getConnectedSpecifiedAtom(structure, matchedunspecified, set);
                //IAtom matchedspecified = this.getConnectingAtom(matchedunspecified);
                IsolateConnectedStructure isolate = new IsolateConnectedStructure();
                IAtomContainer substructure =
                        isolate.IsolateConnectedStructure(structure, matchedspecified, matchedunspecified);
                if(substructure != null) {
                    Integer countI = new Integer(count);
                    //System.out.println("ID: " + matchedunspecified.getID() + "\t Symbol: " + matchedunspecified.getSymbol());
                    //System.out.println("Substructure: A=" + substructure.getAtomCount() + " B=" + substructure.getBondCount());
                    if (substructure == null || matchedunspecified.getID() == null) {
                        System.out.println(MoleculeUtilities.toString((AtomContainer) substructure));

                    }

                    connections.put(matchedunspecified.getID(), substructure);
                } else {
                    noloop = false;
                    connections = null;
                }
            }
        }
        return connections;
    }

    public IAtom getConnectedSpecifiedAtom(IAtomContainer structure, IAtom unspecified, List<RMap> set) {
        List<IAtom> connected = structure.getConnectedAtomsList(unspecified);
        Iterator<RMap> iter = set.iterator();
        IAtom atm = null;
        boolean notfound = true;
        while (iter.hasNext() && notfound) {
            RMap m = iter.next();
            atm = structure.getAtom(m.getId1());
            if (connected.contains(atm)) {
                notfound = false;
            }
        }
        return atm;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(super.toString());
        buf.append("\n");
        buf.append(table.toString());
        return buf.toString();
    }

    private List<List<RMap>> getAtomMatches(AtomContainer structure) throws CDKException {

//        List<List<RMap>> matchedatoms = new ArrayList<List<RMap>>();
//        List<List<RMap>> sets = matches.getAtomMatches(structure, this.getMolecule());
//        matchedatoms.addAll(sets);

//        ArrayList<AtomContainer> molecules = substructuresFromSymmetry.generateStructures(structure);
//        Iterator<AtomContainer> iter = molecules.iterator();
//        while(iter.hasNext()) {
//            AtomContainer mol = iter.next();
//            List<List<RMap>> sets = matches.getAtomMatches(structure, mol);
//            matchedatoms.addAll(sets);
//        }

        List<List<RMap>> matchedatoms = matches.getAtomMatches(structure, this.getMolecule());
        return matchedatoms;
    }
/*
 *  Loop through symmetries ({@link SetOfSymmetryAssignments})
 *    key: the name of the atom signifying the assignment
 *    assignment: The SymmetryAssignment from structure
 *    Loop through each assignments
 *        akey: The key of the assignment of the table in this assignment
 *        assign0: assignment in the structure
 *        if the same size, then do comparison of labels
 *            (copy -> remove those that are the same
                    -> see if there are any left
 *                  -> none left, the same list
 *
 */
    private boolean ifProperSymmetry(SetOfSymmetryAssignments assignments) {
//        System.out.println(assignments.toString());
//        System.out.println(this.table.toString());
        Iterator<String> iter = assignments.keySet().iterator();
        boolean okay = true;
        while(iter.hasNext() && okay) {
            String key = iter.next();
            SymmetryAssignment assignment = assignments.get(key);
            Vector<String> assign0 = assignment.getAssignmentsInSymmetryStructure();
            Iterator<String> aiter = this.table.keySet().iterator();
            boolean notdone = true;
            while(aiter.hasNext() && notdone) {
                String akey = aiter.next();
                SymmetryAssignment assign1 = this.table.get(akey);
                if(assign1.getAssignmentsInMolecule().size() == assign0.size()) {
                    Vector<String> cpy = new Vector<String>(assign0);
                    cpy.removeAll(assign1.getAssignmentsInMolecule());
                    if(cpy.size() == 0) {
                        notdone = false;
                    } else if(cpy.size() != assign0.size()) {
                        notdone = false;
                        okay = false;
                    }
                }
            }
        }
//        if(okay)
//            System.out.println("The sets are the same");
//        else
//            System.out.println("The sets are not the same");
        return okay;
    }
}
