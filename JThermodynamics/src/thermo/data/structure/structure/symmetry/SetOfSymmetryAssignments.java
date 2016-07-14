/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author blurock
 */
public class SetOfSymmetryAssignments extends Hashtable<String, SymmetryAssignment> {

    public SetOfSymmetryAssignments() {
    }
    public SetOfSymmetryAssignments(SetOfSymmetryAssignments set) {
        super(set);
    }
    
    public SetOfSymmetryAssignments(List<SymmetryPair> pairlist, IAtomContainer structure) {
        Iterator<SymmetryPair> i = pairlist.iterator();
        Set<String> keys = this.keySet();
        while (i.hasNext()) {
            SymmetryPair pair = i.next();
            this.add(pair,structure);
        }
    }
   public boolean add(SymmetryPair symmetry,IAtomContainer structure) {
        SymmetryAssignment assign = this.get(symmetry.getGroupName());
        if (assign == null) {
            assign = new SymmetryAssignment(symmetry.getGroupName(),symmetry.getConnectedSymmetry(),structure);
            this.put(symmetry.getGroupName(), assign);
        }
        return assign.addAssignment(symmetry.getStructureName());
    }

    /** Find the correspondences between two sets of symmetry assignments
     * 
     * Each symmetry assignment is stored in a Hashtable:
     * <ol>
     * <li> The key is the name of the particular symmetry element
     * <li> The Symmetry element stored in SymmetryAssignment
     * </ol>
     *
     * 
     * The first criteria for a match is that both sets have not only the same number of elements, but the 
     * individual symmmetry assignments have the same number.
     * 
     * For the matching to be successful, all assignments from both have to be paired (null is returned otherwise).
     * 
     * The matching between two assignments is determined from the SymmetryAssignment class.
     * 
     * The name of the key is not used for the symmetry matching.
     * 
     * A SymmetryAssignment match is in that class.
     * 
     * 
     * @param assign1 the first set of symmetry assignments
     * @return A Hashtable using the name of the first assignment as a key to reference the second assignment. If the two sets do not correspond exactly, then null is returned.
     */
    public Hashtable<String, String> sameMatchings(SetOfSymmetryAssignments assign1) {
        boolean ans = numberOfAssignmentsMatch(assign1);
        Hashtable<String, String> matched = new Hashtable<String, String>();
        if (ans) {
            Set<String> set1 = assign1.keySet();
            Iterator<String> ikeys = set1.iterator();
            while (ikeys.hasNext() && ans) {
                String key1 = ikeys.next();
                SymmetryAssignment symmetry1 = assign1.get(key1);
                String key2 = matchingAssignment(symmetry1);
                if (key2 != null) {
                    matched.put(key1, key2);
                } else {
                    ans = false;
                }
            }
        }
        if (!ans) {
            matched = null;
        }
        return matched;
    }
    public SymmetryAssignment findSymmetryWithMolecleAtom(String atm) {
        SymmetryAssignment symmetry = null;
        Iterator<String> groups = this.keySet().iterator();
        boolean notfound = true;
        while(groups.hasNext() && notfound) {
            SymmetryAssignment assign = this.get(groups.next());
            if(assign.IsAtomInMolecule(atm)) {
                symmetry = assign;
                notfound = false;
            }
        }
        return symmetry;
    }
      public boolean sameSymmetry(SetOfSymmetryAssignments assign1) {
        boolean ans = numberOfAssignmentsMatch(assign1);
        if (ans) {
            Set<String> set1 = assign1.keySet();
            Iterator<String> ikeys = set1.iterator();
            while (ikeys.hasNext() && ans) {
                String key1 = ikeys.next();
                SymmetryAssignment symmetry1 = assign1.get(key1);
                int count1 = symmetry1.getAssignmentsInMolecule().size();
                
                Set<String> set2 = this.keySet();
                Iterator<String> jkeys = set2.iterator();
                boolean notthesame = true;
                while(jkeys.hasNext() && notthesame) {
                    String key2 = jkeys.next();
                    SymmetryAssignment symmetry2 = this.get(key2);
                    int count2 = symmetry2.getAssignmentsInMolecule().size();
                    notthesame = count1 != count2;
                }
                if(notthesame)
                    ans = false;
            }
        }
        return ans;
    }

    /** Determine which symmetry element matches from a set of symmetry elements
     * 
     * @param symmetry A particular Symmetry element
     * @return The name of the symmetry element which matches, if no match is found, null is returned
     * 
     * A match between two symmetry elements is determined by the matches function in SymmetryAssignment
     */
    public String matchingAssignment(SymmetryAssignment symmetry) {
        String key = null;
        Set<String> set = this.keySet();
        boolean notfound = true;
        Iterator<String> ikeys = set.iterator();
        while (ikeys.hasNext() && notfound) {
            key = ikeys.next();
            SymmetryAssignment next = this.get(key);
            notfound = !next.matches(symmetry);
        }
        if(notfound)
            key = null;
        return key;
    }

    /**
     * 
     * @param assign1
     * @return
     */
    public boolean numberOfAssignmentsMatch(Hashtable<String, SymmetryAssignment> assign1) {
        Set<String> set1 = assign1.keySet();
        Set<String> set2 = this.keySet();
        int iset1 = set1.size();
        int iset2 = set2.size();
        return iset1 == iset2;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Set of Symmetry Assignments: \n");
        Set<String> keys = this.keySet();

        Iterator<String> ikeys = keys.iterator();
        while (ikeys.hasNext()) {
            String key = ikeys.next();
            SymmetryAssignment sym = this.get(key);
            buf.append(sym.toString());
            buf.append("\n");
        }
        return buf.toString();
    }
    public List<SymmetryPair> extractListOfSymmetryPairs() {
        ArrayList<SymmetryPair> pairs = new ArrayList<SymmetryPair>(); 
        Iterator<String> ikeys = this.keySet().iterator();
        while(ikeys.hasNext()) {
            String key = ikeys.next();
            SymmetryAssignment assignment = this.get(key);
            Iterator<String> iatm = assignment.getAssignmentsInMolecule().iterator();
            String group = assignment.getGroupName();
            while(iatm.hasNext()) {
                String atm = iatm.next();
                SymmetryPair pair = new SymmetryPair(group,atm,assignment.getSymmetryConnection());
                pairs.add(pair);
            }
        }
        return pairs;
    }
}
