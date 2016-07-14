/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.CML;

import org.xmlcml.cml.element.CMLScalar;
import thermo.CML.CMLAbstractThermo;
import thermo.data.structure.disassociation.DisassociationEnergy;
import thermo.data.structure.structure.StructureType;

/**
 *
 * @author edwardblurock
 */
public class CMLStructureType  extends CMLAbstractThermo  {

    @Override
    public void toCML() {
           StructureType stype = (StructureType) getStructure();
            this.setId("DisassociationEnergy");
             
             CMLScalar nameS = new CMLScalar();
            nameS.setId("nameOfStructure");
            nameS.setValue(stype.getNameOfStructure());
            this.appendChild(nameS);
            
             CMLScalar stypeS = new CMLScalar();
            stypeS.setId("typeOfStructure");
            stypeS.setValue(stype.getTypeOfStructure());
            this.appendChild(stypeS);
            
    }

    @Override
    public void fromCML() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
