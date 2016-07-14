/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.matching;

import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.Bond;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.isomorphism.matchers.smarts.OrderQueryBond;
import thermo.data.structure.utilities.MoleculeUtilities;
/**
 *
 * @author blurock
 */
public class QueryMoleculeWithMetaAtoms extends AtomContainer {
    
    QueryMoleculeWithMetaAtoms(AtomContainer mol) {
        List<IAtom> radicals = MoleculeUtilities.findSingleElectrons(mol);

        for(int i=0;i<mol.getAtomCount();i++) {
            Atom atm = (Atom) mol.getAtom(i);
            QueryAtomWithMetaAtoms qatm = new QueryAtomWithMetaAtoms(atm);
            Iterator<IAtom> iter = radicals.iterator();
            while(iter.hasNext()) {
                IAtom singatm = iter.next();
                if(atm == singatm)
                    qatm.setToRadical();
            }
            this.addAtom(qatm);
        }
        for(int i=0;i<mol.getBondCount();i++) {
            Bond bnd = (Bond) mol.getBond(i);
            
            int i1 = mol.getAtomNumber(bnd.getAtom(0));
            int i2 = mol.getAtomNumber(bnd.getAtom(1));
            QueryAtomWithMetaAtoms qatm1 = (QueryAtomWithMetaAtoms) this.getAtom(i1);
            QueryAtomWithMetaAtoms qatm2 = (QueryAtomWithMetaAtoms) this.getAtom(i2);
            QueryAtomWithMetaAtoms[] vec = new QueryAtomWithMetaAtoms[2];
            vec[0] = qatm1;
            vec[1] = qatm2;
            QueryBondWithMetaAtoms qbnd = new QueryBondWithMetaAtoms();
            qbnd.setOrder(bnd.getOrder());
            if(bnd.getFlag(CDKConstants.ISAROMATIC)) {
                qbnd.setFlag(CDKConstants.ISAROMATIC, true);
                qatm1.setFlag(CDKConstants.ISAROMATIC, true);
                qatm1.setFlag(CDKConstants.ISAROMATIC, true);
            }
            qbnd.setAtoms(vec);
            this.addBond((IBond) qbnd);
        }
    }

}
