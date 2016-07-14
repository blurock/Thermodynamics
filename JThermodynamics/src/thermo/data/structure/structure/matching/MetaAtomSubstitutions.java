/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.interfaces.IAtom;

/** Container for the list of atoms to be substituted.
 *
 * The list of atoms are the list of atoms to be substituted into the molecule and replaced with an
 * atom call {@link metaAtomName} in class.
 *
 * @author blurock
 */
public class MetaAtomSubstitutions implements Comparable {

    private List<IAtom> substitutionsInMolecule;
    private String metaAtomName;

    MetaAtomSubstitutions(String meta, List<IAtom> set) {
        //Collections.sort(substitionsInMolecule);
        metaAtomName = meta;
        substitutionsInMolecule = set;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("MetaAtomSubstitutions; ");
        Iterator<IAtom> i = getSubstitutionsInMolecule().iterator();
        while (i.hasNext()) {
            IAtom iI = i.next();
            buf.append(iI.toString() + "\t");
        }
        return buf.toString();
    }

    /** This determines the order of the substitutions (operation for {@link Comparable}
     *
     * This basically determines whether two substitutions are the same.
     * Substitutions are the same if the list of atoms are the same.
     * 
     * @param o1 First meta atom substitution
     * @param o2 Second meta atom substitution.
     * @return 0 if the same set of atoms, 1 otherwise
     */
    public int compare(Object o1, Object o2) {
        int comp = 0;
        MetaAtomSubstitutions sub1 = (MetaAtomSubstitutions) o1;
        MetaAtomSubstitutions sub2 = (MetaAtomSubstitutions) o2;

        Iterator<IAtom> i = sub1.getSubstitutionsInMolecule().iterator();
        Iterator<IAtom> j = sub2.getSubstitutionsInMolecule().iterator();
        boolean stillSame = true;
        while (stillSame && i.hasNext()) {
            IAtom atm = i.next();
            stillSame = sub2.getSubstitutionsInMolecule().contains(atm);
        }
        /*
        boolean stillSame = true;
        Integer iI = null;
        Integer jI = null;
        while (stillSame && i.hasNext()) {
            iI = i.next();
            Iterator<IAtom> j = sub2.getSubstitutionsInMolecule().iterator();
            boolean notfound = true;
            while (notfound && j.hasNext()) {
                jI = j.next();
                notfound = iI.intValue() != jI.intValue();
            }
            stillSame = !notfound;
         */ 
        //stillSame = sub2.getSubstitionsInMolecule().contains(iI);
        if (!stillSame) {
            comp = 1;
        }
        return comp;
    }

    /**
     * 
     * @return
     */
    public List<IAtom> getSubstitutionsInMolecule() {
        return substitutionsInMolecule;
    }

    /**
     * 
     * @return
     */
    public String getMetaAtomName() {
        return metaAtomName;
    }

    /**
     * 
     * @param metaAtomName
     */
    public void setMetaAtomName(String metaAtomName) {
        this.metaAtomName = metaAtomName;
    }

    /**
     *
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        return this.compare(this, o);
    }


}
