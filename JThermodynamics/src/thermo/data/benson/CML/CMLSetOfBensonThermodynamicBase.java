/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson.CML;

import java.util.Iterator;
import java.util.List;
import org.xmlcml.cml.base.CMLElement;
import thermo.CML.CMLAbstractThermo;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;

/**
 *
 * @author edwardblurock
 */
public class CMLSetOfBensonThermodynamicBase   extends CMLAbstractThermo {

    @Override
    public void toCML() {
        SetOfBensonThermodynamicBase set = (SetOfBensonThermodynamicBase) getStructure();
        
        this.setId("SetOfBensonThermodynamicBase");
        Iterator<BensonThermodynamicBase> iter = set.iterator();
        while(iter.hasNext()) {
            CMLBensonThermodynamicBase cmlbenson = new CMLBensonThermodynamicBase();
            BensonThermodynamicBase thermo = iter.next();
            cmlbenson.setStructure(thermo);
            cmlbenson.toCML();
            this.appendChild(cmlbenson);
        }
    }

    @Override
    public void fromCML() {
        SetOfBensonThermodynamicBase set = new SetOfBensonThermodynamicBase();
        List<CMLElement> top = this.getChildCMLElements();
        for (int i = 0; i < top.size(); i++) {
           CMLBensonThermodynamicBase cmlbenson = new CMLBensonThermodynamicBase();
           CMLElement prop = (CMLElement) top.get(i);
           cmlbenson.copyChildrenFrom(prop);
           cmlbenson.fromCML();
           BensonThermodynamicBase benson = (BensonThermodynamicBase) cmlbenson.getStructure();
           set.add(benson);
        }
        this.setStructure(set);
    }

}
