/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.ISingleElectron;
import org.openscience.cdk.isomorphism.mcss.RMap;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author edwardblurock
 */
public class MoleculeUtilities {

    static String atomprefix = "a";

    /** Find the atom with the given ID
     *
     * @param ID The ID of the atom to search for
     * @param molecule The molecule to search for atom
     * @return The atom with the proper ID, null if not found
     */
    public static IAtom findAtomInMolecule(String ID, IAtomContainer molecule) {
        IAtom atm = null;
        boolean notfound = true;
        Iterator<IAtom> iter = molecule.atoms().iterator();
        while (iter.hasNext() && notfound) {
            IAtom a = iter.next();
            if (a.getID().equals(ID)) {
                atm = a;
                notfound = false;
            }
        }
        return atm;
    }

    /** Reassign the ids for all the molecule
     * This routine is used to make sure that the atom ids are
     * assigned and are unique.
     *
     * @param molecule The molecule to reassign
     */
    public static void assignIDs(IAtomContainer molecule) {
        Iterator<IAtom> iter = molecule.atoms().iterator();
        int count = 0;
        while (iter.hasNext()) {
            IAtom a = iter.next();
            String name = atomprefix + Integer.toString(count++);
            a.setID(name);
        }
    }

    public static IAtomContainer eliminateHydrogens(IAtomContainer molecule) {
        AtomContainer nohydrogens = new AtomContainer(molecule);
        List<IAtom> hydrogens = findHydrogens(nohydrogens);
        Iterator<IAtom> iter = hydrogens.iterator();
        while (iter.hasNext()) {
            IAtom atm = iter.next();
            List<IAtom> connected = nohydrogens.getConnectedAtomsList(atm);
            if (connected.size() == 1) {
                nohydrogens.removeBond(atm, connected.get(0));
            } else {
                System.err.println("Expected Hydrogen to have one bond, found " + connected.size());
            }
            nohydrogens.removeAtom(atm);
        }
        nohydrogens.setID(molecule.getID());
        return nohydrogens;
    }

    public static List<IAtom> findHydrogens(IAtomContainer molecule) {
        List<IAtom> lst = new ArrayList<IAtom>();
        Iterator<IAtom> iter = molecule.atoms().iterator();
        while (iter.hasNext()) {
            IAtom atm = iter.next();
            if (isHydrogen(atm)) {
                lst.add(atm);
            }
        }
        return lst;
    }


    private static String hydrogenS = "H";

    private static boolean isHydrogen(IAtom atm) {
        String aS = atm.getSymbol();
        return aS.equals(hydrogenS);
    }
    public static List<IAtom> findSingleElectrons(IAtomContainer molecule) {
        List<IAtom> atoms = new ArrayList<IAtom>();
             for(int i=0;i<molecule.getSingleElectronCount();i++) {
                ISingleElectron sing = molecule.getSingleElectron(i);
                IAtom atm = sing.getAtom();
                atoms.add(atm);
            }
       return atoms;
    }
    public static void printSetOfCorrespondences(List< List<RMap> > match, IAtomContainer mol1, IAtomContainer mol2) {
        Iterator<List<RMap>> set = match.iterator();
        int count = 1;
        while(set.hasNext()) {
            System.out.print(count++ + ":");
            printCorrespondences(set.next(),mol1,mol2);
        }
    }

    public static void printCorrespondences(List<RMap> set, IAtomContainer mol1, IAtomContainer mol2) {
         StringBuffer buf = new StringBuffer();
         Iterator<RMap> iter = set.iterator();
         while(iter.hasNext()) {
             RMap m = iter.next();
             buf.append("\t(");
             buf.append(mol1.getAtom(m.getId1()).getID());
             buf.append(":");
             buf.append(mol2.getAtom(m.getId2()).getID());
             buf.append(")");
         }
        System.out.println(buf.toString());

    }
    public static String toString(AtomContainer mol) {
        StructureAsCML struct = null;
        try {
            struct = new StructureAsCML(mol);
        } catch (CDKException ex) {
            Logger.getLogger(MoleculeUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return struct.getCmlStructureString();
    }
    public static void normalizeAtomNames(AtomContainer mol) throws IOException {
        Iterator<IAtom> iter = mol.atoms().iterator();
        while(iter.hasNext()) {
            IAtom atm = iter.next();
            String symbol = atm.getSymbol();
            if(symbol.length() == 1) {
                symbol = symbol.toUpperCase();
            } if(symbol.length() == 2) {

            } else {
                symbol = symbol.toUpperCase();
            }
            atm.setSymbol(symbol);
        }
    }
    public static void normalizeMolecule(AtomContainer mol) throws IOException {
        normalizeAtomNames(mol);
        assignIDs(mol);
    }
}
