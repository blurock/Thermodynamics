/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.matching;

import org.openscience.cdk.Atom;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.isomorphism.matchers.IQueryAtom;

/**
 *
 * @author blurock
 */
public class QueryAtomWithMetaAtoms extends Atom implements IQueryAtom {

    String matchAny = "R";
    private Object aromaticMatchAny = "R.Aromatic";
    boolean radical = false;

    /**
     * 
     * @param atm
     */
    public QueryAtomWithMetaAtoms(Atom atm) {
        super(atm);
    }

    /**
     *
     * @param atm
     */
    public QueryAtomWithMetaAtoms(QueryAtomWithMetaAtoms atm) {
        super(atm);
        radical = atm.radical;
    }

    /**
     * 
     * @param arg0
     * @return
     */
    public boolean matches(IAtom arg0) {
        QueryAtomWithMetaAtoms atm = (QueryAtomWithMetaAtoms) arg0;
        boolean ans = false;
        /*
        System.out.print("matches: (" + this.getSymbol()
                + ", " + this.getID()
                + "," + this.getFlag(CDKConstants.ISAROMATIC)
                + ")");
        
        if (this.radical) {
            System.out.print(" (.) ");
        }
        System.out.print("\t(" + arg0.getSymbol()
                + ", " + arg0.getID()
                + "," + arg0.getFlag(CDKConstants.ISAROMATIC)
                + ")");
        
        if (atm.radical) {
            System.out.print(" (.) ");
        } else {
            System.out.print("     ");
        }
        */
        if (formalChargeMatch(atm) && radicalMatch(atm) && aromaticMatch(atm)) {
            if (symbolMatch(atm)) {
                ans = true;
            } else if (this.matchesAll(atm) && aromaticMatch(atm)) {
                    ans = true;
            } else if (this.matchesAll(this) && aromaticMatch(atm)) {
                    ans = true;
            }
            }
        /*
        if (ans) {
            System.out.println("   Match: " + atm.getSymbol() + "  = " + this.getSymbol());
        } else {
            System.out.println("No Match: " + atm.getSymbol() + " != " + this.getSymbol());
        }
         */
        return ans;
    }

    /**
     * 
     * @param atm
     * @return
     */
    public boolean isAromatic(IAtom atm) {
        return atm.getFlag(CDKConstants.ISAROMATIC);
    }
    public boolean aromaticMatch(IAtom atm) {
        boolean ans=false;
        if(this.isAromatic(this) && this.isAromatic(atm)) {
            ans=true;
        } if(!this.isAromatic(this) && !this.isAromatic(atm)) {
            ans = true;
        }
        return ans;
       }
    /**
     * 
     * @param atm
     * @return
     */
    public boolean matchesAll(Atom atm) {
        return atm.getSymbol().equals(matchAny);
    }

    /**
     * 
     * @param atm
     * @return
     */
    public boolean aromaticMatchAny(Atom atm) {
        return atm.getSymbol().contains((CharSequence) aromaticMatchAny);
    }

    private boolean symbolMatch(Atom atm) {
        return this.getSymbol().equalsIgnoreCase(atm.getSymbol());
    }

    /**
     * 
     * @param atm
     * @return
     */
    public boolean formalChargeMatch(Atom atm) {
        return (this.getFormalCharge() == atm.getFormalCharge());
    }

    public boolean radicalMatch(QueryAtomWithMetaAtoms atm) {
        boolean ans = false;
        if (matchesAll(atm) || matchesAll(this)) {
            ans = true;
        } else {
            if (this.radical && atm.radical) {
                ans = true;
            } else if (!this.radical && !atm.radical) {
                ans = true;
            }

        }
        return ans;

    }

    /**
     * 
     * @param arg0
     */
    public void setOperator(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void setToRadical() {
        radical = true;
    }
}
