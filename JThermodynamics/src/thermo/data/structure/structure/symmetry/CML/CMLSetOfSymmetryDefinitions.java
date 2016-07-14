/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry.CML;

import java.util.Iterator;
import java.util.List;
import org.xmlcml.cml.base.CMLElement;
import thermo.CML.CMLAbstractThermo;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;
import thermo.data.structure.structure.symmetry.CML.CMLSymmetryDefinition;
import thermo.data.structure.structure.symmetry.SetOfSymmetryDefinitions;

/**
 *
 * @author edwardblurock
 */
public class CMLSetOfSymmetryDefinitions   extends CMLAbstractThermo {
        @Override
    public void toCML() {
        SetOfSymmetryDefinitions set = (SetOfSymmetryDefinitions) getStructure();
        
        this.setId("SetOfSymmetryDefinitions");
        Iterator<SymmetryDefinition> iter = set.iterator();
        while(iter.hasNext()) {
            CMLSymmetryDefinition cmlsymdef = new CMLSymmetryDefinition();
            SymmetryDefinition thermo = iter.next();
            cmlsymdef.setStructure(thermo);
            cmlsymdef.toCML();
            this.appendChild(cmlsymdef);
        }
    }

    @Override
    public void fromCML() {
        SetOfSymmetryDefinitions set = new SetOfSymmetryDefinitions();
        List<CMLElement> top = this.getChildCMLElements();
        for (int i = 0; i < top.size(); i++) {
           CMLSymmetryDefinition cmlsymdef = new CMLSymmetryDefinition();
           CMLElement prop = (CMLElement) top.get(i);
           cmlsymdef.copyChildrenFrom(prop);
           cmlsymdef.fromCML();
           SymmetryDefinition benson = (SymmetryDefinition) cmlsymdef.getStructure();
           set.add(benson);
        }
        this.setStructure(set);
    }

}
