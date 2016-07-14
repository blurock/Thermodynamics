/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.CML;

import java.util.List;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLScalar;
import thermo.NameListSet;

/**
 *
 * @author blurock
 */
public class CMLNameListSet extends CMLAbstractThermo {
    
    public CMLNameListSet() {
        
    }

    @Override
    public void toCML() {
        NameListSet groups = (NameListSet) getStructure();
        this.appendChild(groups.getSetName());
        CMLNameList namelist = new CMLNameList(groups.toArray());
        namelist.toCML();
        CMLScalar name = new CMLScalar();
        name.setId("Name");
        name.setValue(groups.getSetName());
        this.appendChild(name);
        this.appendChild(namelist);
    }

    @Override
    public void fromCML() {
        List<CMLElement> proplist = this.getChildCMLElements();
        if(proplist.size() == 2) {
            CMLScalar cmlname = (CMLScalar) proplist.get(0);
            String setname = cmlname.getStringContent();
            CMLProperty cmllist = (CMLProperty) proplist.get(1);
            CMLNameList namelist = new CMLNameList();
            namelist.copyChildrenFrom(cmllist);
            namelist.fromCML();
            NameListSet groups = new NameListSet(setname,namelist.getNameList());
            this.setStructure(groups);
        }
        
    }
}
