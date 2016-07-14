/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.CML;

import java.util.List;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLPropertyList;
import org.xmlcml.cml.element.CMLScalar;
import thermo.data.structure.structure.DatabaseMolecule;

/**
 *
 * @author edwardblurock
 */
public class CMLDatabaseMolecule  extends thermo.CML.CMLAbstractThermo {

    @Override
    public void toCML() {
        DatabaseMolecule molecule = (DatabaseMolecule) getStructure();
        CMLScalar mol = new CMLScalar();
        mol.setValue(molecule.getMolecule());
        this.appendChild(mol);
 
        CMLScalar src = new CMLScalar();
        src.setValue(molecule.getSource());
        this.appendChild(src);
        
        CMLScalar struct = new CMLScalar();
        struct.setValue(molecule.getCMLStructure());
        this.appendChild(struct);

    }

    @Override
    public void fromCML() {
        List<CMLElement> proplist = this.getChildCMLElements();
       
        
        CMLScalar mol = (CMLScalar) proplist.get(0);
        String molS = mol.getString();
        
        CMLScalar src = (CMLScalar) proplist.get(0);
        String srcS = src.getString();
        
        CMLScalar struct = (CMLScalar) proplist.get(0);
        String structS = struct.getString();
        
        DatabaseMolecule molecule = new DatabaseMolecule(molS, structS, srcS);
        this.setStructure(molecule);
    }

}
