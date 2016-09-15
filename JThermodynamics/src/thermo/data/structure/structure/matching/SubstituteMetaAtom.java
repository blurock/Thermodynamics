/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.matching;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.isomorphism.mcss.RMap;
import thermo.data.structure.structure.MetaAtomDefinition;

/** Substitute a meta atom into a molecule.
 *
 * The meta atom definition {@link MetaAtomDefinition} within the class
 * is substituted into the molecule.
 *
 *
 * The connections are made to the meta atom as a whole.
 * The connections are not ordered.
 * This means that the basic assumption is that the meta atom connections
 * are with one atom within the meta atom (although in the substitution
 * operations, this assumption is not made).
 *
 * @author blurock
 */
public class SubstituteMetaAtom {
    
    MetaAtomDefinition metaAtom;
    AtomContainer molecule;
    GetSubstructureMatches matches;
    private int priority;
    
    /** Constructor from {@link MetaAtomDefinition}
     * 
     * @param meta The meta atom definition
     *
     * The priority is set by the number of specified atoms.
     *
     */
    public SubstituteMetaAtom(MetaAtomDefinition meta) {
        metaAtom = meta;
        matches = new GetSubstructureMatches();
        priority = meta.getSpecifiedAtoms().size();
    }
    
    /** Substitute the meta atom in this molecule.
     * 
     * @param molecule The molecule to substitute into.
     * @throws org.openscience.cdk.exception.CDKException
     *
     * First the atom matches are found with {@link GetSubstructureMatches}.
     * From this a list of {@link MetaAtomSubstitutions} are derived ({@link determineSubstitionsFromAtomMaps})
     * Each {@link MetaAtomSubstitutions} is then substituted within the molecule in {@lnk substituteMetaAtoms}.
     *
     */
    public void substitute(AtomContainer molecule) throws CDKException {
        this.molecule = molecule;
        //System.out.println("Number of Atoms:" + molecule.getAtomCount());
        List< List<RMap> > bondmap = matches.getAtomMatches(molecule, metaAtom.getMolecule()); 
        List< MetaAtomSubstitutions> substitutions = determineSubstitionsFromAtomMaps(bondmap);
        substituteMetaAtoms(substitutions);
        
    }

    private List<MetaAtomSubstitutions> determineSubstitionsFromAtomMaps(List<List<RMap>> bondmap) {
        ArrayList<MetaAtomSubstitutions> defs = new ArrayList<MetaAtomSubstitutions>();
        Iterator<List<RMap>> i = bondmap.iterator();
        while(i.hasNext()) {
            List<RMap> map = i.next();
            MetaAtomSubstitutions def = determineSubstitutionFromSingleAtomMap(map);
            if(isNotInSet(defs,def)) {
                //System.out.println("Add Meta Definition: \n" + def.toString());
                defs.add(def);
            }
        }
        return defs;
    }
    /** Use the comparator to determine if a substitution is in a list of substitutions
     * 
     * @param defs The list of substitutions ({@link MetaAtomSubstitutions}
     * @param def The substitution to be tested.
     * @return true if not in the set.
     */
    public boolean isNotInSet(ArrayList<MetaAtomSubstitutions> defs, MetaAtomSubstitutions def) {
        //boolean ans = defs.contains(def);
        Iterator<MetaAtomSubstitutions> i = defs.iterator();
        boolean notinset = true;
        while(i.hasNext() && notinset) {
            MetaAtomSubstitutions meta = i.next();
            if(meta.compareTo(def) == 0) {
                notinset = false;
            }
        }
        
        return notinset;
    }
    List<Integer> getMoleculeMatchesOfSpecifiedAtoms(List<RMap> map) {
        List<Integer> speclist =  metaAtom.getSpecifiedAtoms();
        List<Integer> molmatches = new ArrayList<Integer>();
        Iterator<RMap> i = map.iterator();
        while(i.hasNext()) {
            RMap pair = i.next();
            Integer i2I = pair.getId2();
            if(speclist.contains(i2I)) {
                molmatches.add(new Integer(pair.getId1()));
            }
        }
        return molmatches;
    }
    /** Convert a set of matches (each in {@RMap} to a {@link MetaAtomSubstitutions}
     *
     * @param map The matched atoms
     * @return The substitutions within a class.
     *
     */
    private MetaAtomSubstitutions determineSubstitutionFromSingleAtomMap(List<RMap> map) {
        List<Integer> molmatches = getMoleculeMatchesOfSpecifiedAtoms(map);
        List<IAtom> atmmatches = SubstructureIsolation.listOfAtomsFromIndicies(molecule, molmatches);
        MetaAtomSubstitutions def = new MetaAtomSubstitutions(metaAtom.getMetaAtomName(), atmmatches);
        //System.out.println("-----------------------------Substitution:  \n"+def.toString());
        return def;
    }
    /** Eliminate the list of atoms from the molecule
     *
     * @param molecule The molecule
     * @param atoms The list of atoms
     *
     * Simply loop through the atoms and eliminate them from the molecule
     */
    private void eliminateAtomsFromMolecule(AtomContainer molecule, List<IAtom> atoms) {
        Iterator<IAtom> i = atoms.iterator();
        while(i.hasNext()) {
            IAtom atm = i.next();
            molecule.removeAtom(atm);
        }
    }
    /** Eliminate the bonds in the list from the molecule (not the atoms)
     *
     * @param molecule The molecule
     * @param subbonds The bonds
     *
     * simply loop through the bonds and remove it from the molecule bond list
     */
    private void eliminateBondsFromMolecule(AtomContainer molecule, List<IBond> subbonds) {
        Iterator<IBond> i = subbonds.iterator();
        while(i.hasNext()) {
            IBond bond = i.next();
            molecule.removeBond(bond);
        }
    }
    /** Change the bond to connect to the meta atom
     *
     * @param molecule The molecule
     * @param matm The new meta atom in the molecule
     * @param atoms The list of original atoms making up the meta atom
     * @param connections The list of original bonds connecting the meta atom with other atoms in the molecule
     *
     * Examine each bond in turn.
     * Determine which atom in the bond is to the original set of atoms making up
     * the meta atom and replace it with the new meta atom.
     */
    private void modifyConnectionBonds(AtomContainer molecule, IAtom matm, List<IAtom> atoms, List<IBond> connections) {
        Iterator<IBond> i = connections.iterator();
        while(i.hasNext()) {
            IBond bond = i.next();
            IAtom atm1 = bond.getAtom(0);
            IAtom atm2 = bond.getAtom(1);
            if(atoms.contains(atm1)) {
                bond.setAtom(matm, 0);
            } else {
                bond.setAtom(matm, 1);
            }
        }
    }
    /** Substitute a single meta atom into the molecule.
     *
     * @param sub Meta atom substitution information.
     *
     * Two lists of bonds are determined:
     * <ul>
     * <li> Bonds within the meta atom (subbonds)
     * <li> Bonds connecting the meta atom with the rest of the molecule (connections).
     * </ul>
     *
     * First a meta atom with the meta atom name is created and added to the molecule
     * Then eliminate the bonds within the meta atom.
     * Then modify the connecting bonds (connections) to connect to the meta atom.
     * Then eliminate the original atoms making up the meta atom.
     */
    private void substituteMetaAtom(MetaAtomSubstitutions sub) {
        List<IBond> connections = SubstructureIsolation.listOfBondsConnectedToAtomSet(molecule, sub.getSubstitutionsInMolecule());
        List<IBond> subbonds    = SubstructureIsolation.listOfBondsWithAtomSet(molecule, sub.getSubstitutionsInMolecule());
        IAtom matm = metaAtom.createMetaAtom();
         molecule.addAtom(matm);
        eliminateBondsFromMolecule(molecule,subbonds);
       modifyConnectionBonds(molecule,matm,sub.getSubstitutionsInMolecule(),connections);
        eliminateAtomsFromMolecule(molecule,sub.getSubstitutionsInMolecule());
    }
    /** Substitute a set of meta atom substitutions
     *
     * @param substitutions The set of substitutions.
     *
     * Call {@substituteMetaAom} for each substitution.
     */
    private void substituteMetaAtoms(List<MetaAtomSubstitutions> substitutions) {
        Iterator<MetaAtomSubstitutions> i = substitutions.iterator();
        while(i.hasNext()) {
            MetaAtomSubstitutions sub = i.next();
            substituteMetaAtom(sub);
            /*
            StructureAsCML cml;
            try {
                cml = new StructureAsCML(molecule);
                System.out.println(cml.getCmlStructureString());
            } catch (CDKException ex) {
                Logger.getLogger(SubstituteMetaAtom.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
        }
        
        
                
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(metaAtom.toString());
        return buf.toString();
    }

    /**
     * 
     * @return
     */
    public int getPriority() {
        return priority;
    }
    public String getID() {
        return metaAtom.getMetaAtomName();
    }
}
