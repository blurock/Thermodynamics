/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.matching;

import org.openscience.cdk.Atom;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.isomorphism.matchers.IQueryBond;
import org.openscience.cdk.isomorphism.matchers.OrderQueryBond;

/**
 *
 *
 * @author edwardblurock
 */
public class QueryBondWithMetaAtoms extends OrderQueryBond {

    /**
     * The definition of equal is the same as from {@link OrderQueryBond} except
     * when it returns false. In this case, if the atoms being bonded are the same,
     * the bond is checked to see if it aromatic, if so, then true is returned.
     *
     * This is to compensate for an aromatic bond being defined either with alternating
     * single and double bonds or just with single bonds.
     *
     * @param ibond The bond to be compared
     * @return true if the bonds are 'equal'
     */
    @Override
    public boolean matches(IBond ibond) {
        boolean ans = super.matches(ibond);
        //System.out.println("(" + this.getOrder() + "," + ibond.getOrder() + ") = " + ans);
        if(!ans) {
            QueryAtomWithMetaAtoms atm1a = (QueryAtomWithMetaAtoms) ibond.getAtom(0);
            QueryAtomWithMetaAtoms atm2a = (QueryAtomWithMetaAtoms) ibond.getAtom(1);
            QueryAtomWithMetaAtoms atm1b = (QueryAtomWithMetaAtoms) this.getAtom(0);
            QueryAtomWithMetaAtoms atm2b = (QueryAtomWithMetaAtoms) this.getAtom(1);
        
            if(atm1a.matches(atm1b)) {
                ans = matchAromaticBond(ibond);
            } else if(atm1a.matches(atm2b)) {
                ans = matchAromaticBond(ibond);
            }
        }
        //System.out.println("Final=" + ans);
        return ans;
    }
    boolean matchAromaticBond(IBond ibond) {
        boolean ans = false;
        if(ibond.getFlag(CDKConstants.ISAROMATIC)) {
            if(this.getFlag(CDKConstants.ISAROMATIC)) {
                ans = true;
            }
        }
        
        
        return ans;
    }

}
