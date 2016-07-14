/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational.CML;

import java.util.List;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLScalar;
import thermo.CML.CMLAbstractThermo;
import thermo.data.structure.structure.vibrational.VibrationalStructureInfo;

/**
 *
 * @author blurock
 */
public class CMLVibrationalStructureInfo   extends CMLAbstractThermo {

    @Override
    public void toCML() {
       VibrationalStructureInfo info = (VibrationalStructureInfo) getStructure();
        this.setId("MetaAtomInfo");

        CMLScalar elementNameS = new CMLScalar();
        elementNameS.setId("elementName");
        elementNameS.setValue(info.getElementName());
        this.appendChild(elementNameS);
        
        CMLScalar structureNameS = new CMLScalar();
        structureNameS.setId("structureName");
        structureNameS.setValue(info.getStructureName());
        this.appendChild(structureNameS);
       
        CMLScalar frequency = new CMLScalar();
        frequency.setId("frequency");
        frequency.setValue(info.getFrequency());
        this.appendChild(frequency);
       
        CMLScalar symmetry = new CMLScalar();
        symmetry.setId("symmetry");
        symmetry.setValue(info.getSymmetry());
        this.appendChild(symmetry);
    }

    @Override
    public void fromCML() {
        VibrationalStructureInfo info = new VibrationalStructureInfo();
        List<CMLElement> proplist = this.getChildCMLElements();
        if (proplist.size() == 4) {
            CMLScalar elecml = (CMLScalar) proplist.get(0);
            String elementName = elecml.getStringContent();
            info.setElementName(elementName);

            CMLScalar structcml = (CMLScalar) proplist.get(1);
            String structureNameS = structcml.getStringContent();
            info.setStructureName(structureNameS);

            CMLScalar freqcml = (CMLScalar) proplist.get(2);
            double frequency = freqcml.getDouble();
            info.setFrequency(frequency);

            CMLScalar symmcml = (CMLScalar) proplist.get(3);
            double symmetry = symmcml.getDouble();
            info.setSymmetry(symmetry);
            
            this.setStructure(info);
        }
    }

}
