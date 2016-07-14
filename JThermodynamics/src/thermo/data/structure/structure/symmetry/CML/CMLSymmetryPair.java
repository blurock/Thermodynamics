/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry.CML;

import java.util.List;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLScalar;
import thermo.CML.CMLAbstractThermo;
import thermo.data.structure.structure.symmetry.SymmetryPair;

/**
 *
 * @author blurock
 */
public class CMLSymmetryPair  extends CMLAbstractThermo  {

    @Override
    public void toCML() {
       SymmetryPair info = (SymmetryPair) getStructure();
       this.setId("SymmetryPair");
       CMLScalar groupNameS = new CMLScalar();
        groupNameS.setId("groupName");
        groupNameS.setValue(info.getGroupName());
        this.appendChild(groupNameS);
       
        CMLScalar structureNameS = new CMLScalar();
        structureNameS.setId("structureName");
        structureNameS.setValue(info.getStructureName());
        this.appendChild(structureNameS);

    }

    @Override
    public void fromCML() {
        List<CMLElement> proplist = this.getChildCMLElements();
        if (proplist.size() == 2) {
            CMLScalar groupcml = (CMLScalar) proplist.get(0);
            String groupName = groupcml.getStringContent();

            CMLScalar structcml = (CMLScalar) proplist.get(1);
            String structureNameS = structcml.getStringContent();

        SymmetryPair pair = new SymmetryPair(groupName,structureNameS);
            this.setStructure(pair);
        }
    }

}
