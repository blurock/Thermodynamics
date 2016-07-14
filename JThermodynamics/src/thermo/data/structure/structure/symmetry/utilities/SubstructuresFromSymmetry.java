/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.isomorphism.mcss.RMap;
import thermo.data.structure.structure.AtomCount;
import thermo.data.structure.structure.AtomCounts;
import thermo.data.structure.structure.symmetry.SetOfSymmetryAssignments;
import thermo.data.structure.structure.symmetry.SymmetryAssignment;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;

/**
 *
 * @author edwardblurock
 */
public class SubstructuresFromSymmetry {
    String hydrogenS = "H";
    String carbonS = "C";
    String generatedS = "Generated";
    String symmetryS ="Symmetry";
    private ArrayList<AtomContainer> isolateMoleculesFromPairs(ArrayList<SymmetryPairedWithMolecule> pairs) {
        ArrayList<AtomContainer> molecules = new ArrayList<AtomContainer>();
        Iterator<SymmetryPairedWithMolecule> iter = pairs.iterator();
        while(iter.hasNext()) {
            SymmetryPairedWithMolecule pair = iter.next();
            molecules.add(pair.molecule);
            //printMoleculeIDs(carbonS, pair.molecule);
        }
        return molecules;
    }

    private boolean monoValent(AtomContainer molecule, String name) {
        boolean ans = false;
        if(name.equalsIgnoreCase(hydrogenS))
            ans = true;
        else if(name.equalsIgnoreCase(carbonS))
            ans = false;
        else {

        }
        return ans;
    }




    private class SymmetryPairedWithMolecule {
        public AtomContainer molecule;
        HashSet<String> atoms;

        public SymmetryPairedWithMolecule(AtomContainer molecule, HashSet<String> atoms) {
            this.molecule = molecule;
            this.atoms = atoms;
        }

    }


    SymmetryDefinition symmetryDefinition;
    Molecule symmetryMolecule;
    public SubstructuresFromSymmetry(SymmetryDefinition symmetryDefinition) {
        this.symmetryDefinition = symmetryDefinition;
    }

    public ArrayList<AtomContainer> generateStructures(AtomContainer molecule) {
        AtomCounts counts = new AtomCounts(molecule);
        SetOfSymmetryAssignments assignments = symmetryDefinition.getSymmetryAssignmentsTable();
        HashSet<String> atoms = counts.getAtomNames();
        symmetryMolecule = symmetryDefinition.getMolecule();
        return generateStructures(assignments,atoms,symmetryMolecule,counts);
    }

    private ArrayList<AtomContainer> generateStructures(
            SetOfSymmetryAssignments assignments,
            HashSet<String> atoms,
            AtomContainer molecule,
            AtomCounts counts) {
        ArrayList<SymmetryPairedWithMolecule> structures = new ArrayList<SymmetryPairedWithMolecule>();
        SymmetryPairedWithMolecule pair = new SymmetryPairedWithMolecule(molecule,atoms);
        structures.add(pair);
        Iterator<String> names = assignments.keySet().iterator();
        while(names.hasNext()) {
            String name = names.next();
            SymmetryAssignment assignment = assignments.get(name);
            structures = generateStructures(assignment,structures,counts);
            //printMoleculeIDs(symmetryS,assignment.getStructure());
        }
        return isolateMoleculesFromPairs(structures);
    }
    /**
     *
     * @param assignment
     * @param structures
     * @param counts
     * @return
     */
    private ArrayList<SymmetryPairedWithMolecule> generateStructures(
            SymmetryAssignment assignment,
            ArrayList<SymmetryPairedWithMolecule> structures,
            AtomCounts counts) {

        ArrayList<SymmetryPairedWithMolecule> newstructures = new ArrayList<SymmetryPairedWithMolecule>();
        Iterator<SymmetryPairedWithMolecule> iter = structures.iterator();
        boolean notdone = true;
        while(iter.hasNext()) {
            SymmetryPairedWithMolecule pair = iter.next();
            Vector<String> names = assignment.getAssignmentsInMolecule();
            Vector<String> candidatenames = getCandidateNames(counts,pair.atoms,names.size());
            if(candidatenames.size() > 0) {
                generateStructures(assignment,pair,newstructures,candidatenames);
            }
        }
        return newstructures;
    }
    /** Get atoms types with sufficient number of atoms
     * {@link AtomCounts} is used to determine the number of
     * instances of a particular atom there is in the reference
     * molecule.
     * If this number is greater than or equal to the number of
     * atoms required for the symmetry (given by sze),
     * then it is listed as a candidate.
     *
     * @param counts The counts of all atom types in the molecule
     * @param atoms The list of atom type names to check
     * @param sze The minimum number of instance required for symmetry
     *
     */
    private Vector<String> getCandidateNames(AtomCounts counts, HashSet<String> atoms, int size) {
        Vector<String> candidates = new Vector<String>();
        Iterator<String> iter = atoms.iterator();
        while(iter.hasNext()) {
            String name = iter.next();
            Integer count = counts.get(name);
            if(count.intValue() >= size) {
                candidates.add(name);
            }
        }
        return candidates;
    }
    /** Substitute the atoms of the symmetry with all the new symbols, one per new molecule
     *
     * A new set of molecule, symbol set pairs is produced, each one has one of the
     * candidate molecules replaced.
     *
     * The new pair has the specified symbol replaced and the symbol
     * removed from the atoms HashSet.
     *
     * @param assignment The symmetry assignment to get the atom to be subsituted
     * @param pair The current molecule and symbol list
     * @param newstructures The set of new structures to be added to
     * @param candidatenames The symbols which satified the criteria
     */
    private void generateStructures(
            SymmetryAssignment assignment,
            SymmetryPairedWithMolecule pair,
            ArrayList<SymmetryPairedWithMolecule> newstructures,
            Vector<String> candidatenames) {
        AtomContainer molecule = pair.molecule;
        HashSet<String> symbols = pair.atoms;
        Vector<String> tosubstitute = assignment.getAssignmentsInMolecule();
        Iterator<String> iter = candidatenames.iterator();
        while(iter.hasNext()) {
            String name = iter.next();
            AtomContainer newmolecule = generateStructure(molecule, tosubstitute,name);
            HashSet<String> newset = new HashSet<String>(symbols);
            if(monoValent(molecule,name))
                newset.remove(name);
            SymmetryPairedWithMolecule newpair = new SymmetryPairedWithMolecule(newmolecule,newset);
            newstructures.add(newpair);
        }
    }
    /** Replace all atoms in the tosubstitute list with the new symbol
     *
     * @param molecule The molecule to substitute in
     * @param tosubstitute The list of IDs to substitute
     * @param newname The new symbol to substitute in.
     * @return A copy of the input molecule with substitutions
     */
    private AtomContainer generateStructure(AtomContainer molecule, Vector<String> tosubstitute, String newname) {
        AtomContainer newmolecule = new AtomContainer(molecule);
        Iterator<String> iter = tosubstitute.iterator();
        while(iter.hasNext()) {
            String atmname = iter.next();
            substituteAtomInMolecule(newmolecule,atmname,newname);
        }
        return newmolecule;
    }
    /** Replace the atom symbol of the specified atom in the molecule
     *
     * @param newmolecule
     * @param atmname The atom ID to replace with new symbol
     * @param newname The new symbol to replace with
     */
    private void substituteAtomInMolecule(AtomContainer newmolecule, String atmname, String newname) {
        IAtom atm = findAtomInMolecule(newmolecule,atmname);
        atm.setSymbol(newname);
        String id = atm.getID();
        List<IAtom> connected = newmolecule.getConnectedAtomsList(atm);

        Iterator<IAtom> iter = connected.iterator();
        int i=0;
        ArrayList<Order> ordlist = new ArrayList<Order>();
        while(iter.hasNext()) {
            IAtom batm = iter.next();
            IBond bond = newmolecule.getBond(atm, batm);
            Order ord = bond.getOrder();
            ordlist.add(i++,ord);
            newmolecule.removeBond(bond);
        }
        Atom subatm = new Atom(newname);
        subatm.setID(id);
        int idI = findAtomNumber(newmolecule,atmname);
        newmolecule.setAtom(idI, subatm);
        iter = connected.iterator();
        i=0;
        while(iter.hasNext()) {
            IAtom batm = iter.next();
            Order ord = ordlist.get(i++);
            Bond newbond = new Bond(subatm,batm,ord);
            newmolecule.addBond(newbond);
        }
    }
    private int findAtomNumber(AtomContainer newmolecule, String atmname) {
        int id = 0;
        boolean notfound = true;
        while(id<newmolecule.getAtomCount() && notfound) {
            if(newmolecule.getAtom(id).getID().equals(atmname)) {
                notfound = false;
            } else {
                id++;
            }
        }
        return id;
    }
    /** Find the atom with the specified ID
     *
     * @param newmolecule The molecule to search in
     * @param atmname The atom ID to search for
     * @return The atom with the proper ID (null if not found)
     */
    private IAtom findAtomInMolecule(AtomContainer newmolecule, String atmname) {
        IAtom found = null;
        Iterator<IAtom> iter = newmolecule.atoms().iterator();
        while(iter.hasNext() && found == null) {
            IAtom atm = iter.next();
            if(atm.getID().equals(atmname)) {
                found = atm;
            }
        }
        return found;
    }
    public void printMoleculeIDs(String name, IAtomContainer atms) {
        StringBuffer buf = new StringBuffer();
        buf.append(name + ":");
        for(int i=0;i<atms.getAtomCount();i++) {
            buf.append("(" + i + " : " + atms.getAtom(i).getID() + ")\t");
        }
        buf.append("\n");
        System.out.println(buf.toString());
    }
}
