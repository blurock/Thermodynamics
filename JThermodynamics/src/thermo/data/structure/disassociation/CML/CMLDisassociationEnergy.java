/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.disassociation.CML;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLScalar;
import thermo.CML.CMLAbstractThermo;
import thermo.data.structure.disassociation.DisassociationEnergy;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author edwardblurock
 */
public class CMLDisassociationEnergy  extends CMLAbstractThermo  {

    @Override
    public void toCML() {
        try {
            DisassociationEnergy energy = (DisassociationEnergy) getStructure();
            this.setId("DisassociationEnergy");
            
            CMLScalar energyS = new CMLScalar();
            energyS.setId("disassociationEnergy");
            energyS.setValue(energy.getDisassociationEnergy());
            this.appendChild(energyS);
            
            CMLScalar errorS = new CMLScalar();
            errorS.setId("errorInEnergy");
            errorS.setValue(energy.getErrorInEnergy());
            this.appendChild(errorS);
            
            CMLScalar sourceS = new CMLScalar();
            errorS.setId("source");
            errorS.setValue(energy.getSourceOfStructure());
            this.appendChild(sourceS);
            
            StructureAsCML cmlstructure = new StructureAsCML((AtomContainer) energy.getSubstructure());
            String cmlStructureString = cmlstructure.getCmlStructureString();
            CMLScalar structureS = new CMLScalar();
            structureS.setId("structure");
            structureS.setValue(cmlStructureString);
            this.appendChild(structureS);
   
        } catch (CDKException ex) {
            Logger.getLogger(CMLDisassociationEnergy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void fromCML() {
        
        List<CMLElement> proplist = this.getChildCMLElements();
        if (proplist.size() == 4) {
            try {
                CMLScalar energycml = (CMLScalar) proplist.get(0);
                Double energyD = energycml.getDouble();
                
                CMLScalar errorcml = (CMLScalar) proplist.get(1);
                Double errorD = errorcml.getDouble();
                
                CMLScalar sourcecml = (CMLScalar) proplist.get(2);
                String sourceS = sourcecml.getString();
                
                CMLScalar structurecml = (CMLScalar) proplist.get(3);
                String structurecmlS = structurecml.getString();
                
                StructureAsCML cmlstructure = new StructureAsCML();
                cmlstructure.setCmlStructureString(structurecmlS);
                AtomContainer molecule = cmlstructure.getMolecule();
               
                DisassociationEnergy energy
                        = new DisassociationEnergy(molecule,sourceS,energyD, errorD);
                
                this.setStructure(energy);
            } catch (CDKException ex) {
                Logger.getLogger(CMLDisassociationEnergy.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
}
