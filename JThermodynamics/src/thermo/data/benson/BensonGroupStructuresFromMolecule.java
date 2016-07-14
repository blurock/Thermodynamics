/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtom;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.utilities.MoleculeUtilities;

/**
 *
 * @author blurock
 */
public class BensonGroupStructuresFromMolecule {
    private SetOfBensonGroupStructures bensonStructures;
    
    String hydrogen = "h";
    public BensonGroupStructuresFromMolecule() {
        
    }
    
    public SetOfBensonGroupStructures deriveBensonGroupStructures(AtomContainer molecule) {
        SetOfBensonGroupStructures lst = new SetOfBensonGroupStructures();
        System.out.println(MoleculeUtilities.toString(molecule));
        Iterator<IAtom> atoms = molecule.atoms().iterator();
        int ii=0;
        while(atoms.hasNext()) {
            IAtom atm = atoms.next();
            if(bensonCenterAtom(atm)) {
                List<IAtom> connected = molecule.getConnectedAtomsList(atm);
                BensonGroupStructure structure = formBensonGroupStructure(atm,connected);
                //System.out.println(structure.writeAsString());
                lst.add(structure);
            }
        }
        setBensonStructures(lst);
        return lst;
    }

    private boolean bensonCenterAtom(IAtom atm) {
        return !atm.getSymbol().equalsIgnoreCase(hydrogen);
    }

    private BensonGroupStructure formBensonGroupStructure(IAtom atm, List<IAtom> connected) {
        BensonGroupStructure structure = new BensonGroupStructure(atm.getSymbol());
        String atmS = atm.getSymbol();
        adjustConnections(atmS,connected);
        structure.setCenterAtomS(atm.getSymbol());
        while(!connected.isEmpty()) {
            BensonConnectAtomStructure connect = formNextConnection(atm,connected);
            structure.addBondedAtom(connect);
        }
        return structure;
    }

    @SuppressWarnings("empty-statement")
    private BensonConnectAtomStructure formNextConnection(IAtom atm, List<IAtom> connected) {
        Iterator<IAtom> i = connected.iterator();
        IAtom c = i.next();
        int count = 1;
        while(i.hasNext()) {
            IAtom n = i.next();
           if(n.getSymbol().equals(c.getSymbol())) count++;
        }
        removeIAtomFromList(c.getSymbol(),connected);
        BensonConnectAtomStructure structure = new BensonConnectAtomStructure(c.getSymbol(),count);
        return structure;
    }
    private void removeIAtomFromList(String name, List<IAtom> lst) {
        boolean notfound = true;
        if(lst.size() > 0) {
            Iterator<IAtom> i = lst.iterator();
            while(notfound && i.hasNext()) {
                IAtom atm = i.next();
                if(atm.getSymbol().equalsIgnoreCase(name)) {
                    notfound = false;
                    lst.remove(atm);
                    removeIAtomFromList(name,lst);
                }
            }
        } 
    }

    public SetOfBensonGroupStructures getBensonStructures() {
        return bensonStructures;
    }

    public void setBensonStructures(SetOfBensonGroupStructures bensonStructures) {
        this.bensonStructures = bensonStructures;
    }

    private void adjustConnections(String atmS, List<IAtom> connected) {
        if(centerAtomEliminatedInList(atmS)) {
            boolean notdone = true;
            Iterator<IAtom> iter = connected.iterator();
            while(iter.hasNext() && notdone) {
                IAtom ia = iter.next();
                if(ia.getSymbol().equalsIgnoreCase(atmS)) {
                    notdone = false;
                    connected.remove(ia);
                }
            }
        }
    }
    String centers[] = {new String("c/d"), new String("c/t")};
    private boolean centerAtomEliminatedInList(String atmS) {
        boolean ans = false;
        for(int i=0;!ans && i<centers.length;i++) {
            if(atmS.equalsIgnoreCase(centers[i])) {
                ans = true;
            }
        }
        return ans;
    }
    
    
}
