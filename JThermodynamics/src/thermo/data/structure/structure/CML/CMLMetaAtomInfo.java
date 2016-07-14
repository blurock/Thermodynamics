/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.CML;

import java.util.List;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLScalar;
import thermo.CML.CMLAbstractThermo;
import thermo.data.structure.structure.MetaAtomInfo;

/**
 *
 * @author blurock
 */
public class CMLMetaAtomInfo  extends CMLAbstractThermo {

    @Override
    public void toCML() {
       MetaAtomInfo info = (MetaAtomInfo) getStructure();
        this.setId("MetaAtomInfo");

        CMLScalar elementNameS = new CMLScalar();
        elementNameS.setId("elementName");
        elementNameS.setValue(info.getElementName());
        this.appendChild(elementNameS);
        
        CMLScalar metaAtomTypeS = new CMLScalar();
        metaAtomTypeS.setId("metaAtomType");
        metaAtomTypeS.setValue(info.getMetaAtomType());
        this.appendChild(metaAtomTypeS);
       
        CMLScalar metaAtomNameS = new CMLScalar();
        metaAtomNameS.setId("metaAtomName");
        metaAtomNameS.setValue(info.getMetaAtomName());
        this.appendChild(metaAtomNameS);
       
    }

    @Override
    public void fromCML() {
        MetaAtomInfo info = new MetaAtomInfo();
        List<CMLElement> proplist = this.getChildCMLElements();
        if (proplist.size() == 3) {
            CMLScalar elecml = (CMLScalar) proplist.get(0);
            String elementName = elecml.getStringContent();
            info.setElementName(elementName);

            CMLScalar typecml = (CMLScalar) proplist.get(1);
            String metaAtomTypeS = typecml.getStringContent();
            info.setMetaAtomType(metaAtomTypeS);

            CMLScalar namecml = (CMLScalar) proplist.get(2);
            String metaAtomNameS = namecml.getStringContent();
            info.setMetaAtomName(metaAtomNameS);
            
            this.setStructure(info);
        }
    }

}
