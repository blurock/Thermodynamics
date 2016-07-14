/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry.CML;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLPropertyList;
import thermo.CML.CMLAbstractThermo;
import thermo.data.structure.structure.symmetry.ListOfSymmetryPairs;
import thermo.data.structure.structure.symmetry.SymmetryPair;

/**
 *
 * @author blurock
 */
public class CMLListOfSymmetryPairs extends CMLAbstractThermo {

    @Override
    public void toCML() {
        ArrayList<SymmetryPair> pairs = (ArrayList<SymmetryPair>) getStructure();
        this.setId("ListOfSymmetryPairs");
        CMLPropertyList cmllist = new CMLPropertyList();
        
        Iterator<SymmetryPair> ipair = pairs.iterator();
        while(ipair.hasNext()) {
            SymmetryPair pair = ipair.next();
            CMLSymmetryPair cmlpair = new CMLSymmetryPair();
            cmlpair.setStructure(pair);
            cmlpair.toCML();
            cmllist.appendChild(cmlpair);
        }
        this.appendChild(cmllist);
    }

    @Override
    public void fromCML() {
        
         CMLPropertyList props = (CMLPropertyList) this.getChildCMLElements().get(0);
            List<CMLProperty> proplist = props.getPropertyDescendants();
        ListOfSymmetryPairs pairs = new ListOfSymmetryPairs();
        for(int i=0;i<proplist.size();i++) {
            CMLElement cmlpair = proplist.get(i);
            CMLSymmetryPair pair = new CMLSymmetryPair();
            pair.copyChildrenFrom(cmlpair);
            pair.fromCML();
            pairs.add((SymmetryPair) pair.getStructure());
        }
        this.setStructure(pairs);
    }

}
