/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.matching;

import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.Bond;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/** This class is used to find the connected substructures from a molecule
 * 
 * 
 *
 * @author blurock
 */
public class IsolateConnectedStructure {
    /**
     * 
     * @param mol The molecule to isolate connected structure from
     * @param connected  The atom with which the connected structure is bonded
     * @param firstinstructure The first atom in the connected structure 
     * @return A molecule which has the connected structure
     */
    public IAtomContainer IsolateConnectedStructure(IAtomContainer mol, IAtom connected, IAtom firstinstructure) {
        List<IAtom> directconnected = mol.getConnectedAtomsList(firstinstructure);
        boolean notfound = true;
        Iterator<IAtom> i = directconnected.iterator();
        while(i.hasNext() && notfound) {
            IAtom atm = i.next();
            if(atm == connected) {
                notfound = false;
                directconnected.remove(atm);
            }
        }
        Molecule substructure = new Molecule();
        substructure.addAtom(firstinstructure);
        boolean noloop = findConnected(mol,substructure, firstinstructure, directconnected, connected);
        if(!noloop) {
            substructure = null;
        }
        return substructure;
    }

    private void connect(IAtomContainer substructure, IAtom atm, List<IAtom> directconnected) {
        Iterator<IAtom> i = directconnected.iterator();
        while(i.hasNext()) {
            IAtom connect = i.next();
            Bond bnd = new Bond(atm,connect);
            substructure.addAtom(connect);
            substructure.addBond(bnd);
        }
    }

    private boolean findConnected(IAtomContainer mol, IAtomContainer substructure, IAtom atm, List<IAtom> directconnected, IAtom original) {
        boolean noloop = true;
        if(!directconnected.contains(original)) {
            findNewAtoms(substructure, directconnected);
            connect(substructure,atm,directconnected);
            Iterator<IAtom> i = directconnected.iterator();
            while(i.hasNext() && noloop) {
                IAtom next = i.next();
                List<IAtom> connected = mol.getConnectedAtomsList(next);
                noloop = findConnected(mol,substructure,next,connected,original);
            }
        } else {
            noloop = false;
        }
        return noloop;
    }

    private void findNewAtoms(IAtomContainer substructure, List<IAtom> connected) {
      
        Iterator<IAtom> i = substructure.atoms().iterator();
        while(i.hasNext()) {
            IAtom atm = i.next();
            if(connected.contains(atm)) {
                connected.remove(atm);
            }
        }
    }
}
