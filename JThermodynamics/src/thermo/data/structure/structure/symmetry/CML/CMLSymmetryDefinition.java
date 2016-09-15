/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry.CML;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLPropertyList;
import org.xmlcml.cml.element.CMLScalar;
import thermo.CML.CMLAbstractThermo;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.ListOfSymmetryPairs;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;
import thermo.data.structure.structure.symmetry.SymmetryPair;

/**
 *
 * @author edwardblurock
 */
public class CMLSymmetryDefinition extends CMLAbstractThermo {

    @Override
    public void toCML() {
        try {
            SymmetryDefinition symdef = (SymmetryDefinition) getStructure();
            AtomContainer molecule = symdef.getMolecule();
            StructureAsCML cmlstructure = new StructureAsCML(molecule);
            String cmlStructureS = cmlstructure.getCmlStructureString();

            Double internalSymmetryFactor = symdef.getInternalSymmetryFactor();

            String metaAtomName = symdef.getMetaAtomName();
            String metaAtomType = symdef.getMetaAtomType();
            
            List<SymmetryPair> extractListOfSymmetryPairs = symdef.extractListOfSymmetryPairs();

            CMLScalar metaAtomNameS = new CMLScalar();
            metaAtomNameS.setId("MetaAtomName");
            metaAtomNameS.setValue(metaAtomName);
            this.appendChild(metaAtomNameS);

            CMLScalar metaAtomTypeS = new CMLScalar();
            metaAtomTypeS.setId("MetaAtomType");
            metaAtomTypeS.setValue(metaAtomType);
            this.appendChild(metaAtomTypeS);
            

            CMLScalar structureS = new CMLScalar();
            structureS.setId("CMLStructure");
            structureS.setValue(cmlStructureS);
            this.appendChild(structureS);

            CMLScalar internalSymmetryFactorS = new CMLScalar();
            internalSymmetryFactorS.setId("symmetryFactor");
            internalSymmetryFactorS.setValue(internalSymmetryFactor);
            this.appendChild(internalSymmetryFactorS);

            CMLListOfSymmetryPairs cmlsympairs = new CMLListOfSymmetryPairs();
            cmlsympairs.setStructure(extractListOfSymmetryPairs);
            cmlsympairs.toCML();

            CMLPropertyList cmllist = new CMLPropertyList();
            cmllist.appendChild(cmlsympairs);
            this.appendChild(cmllist);
        } catch (CDKException ex) {
            Logger.getLogger(CMLSymmetryDefinition.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void fromCML() {
        List<CMLElement> proplist = this.getChildCMLElements();
        if (proplist.size() == 5) {
            try {
                CMLScalar metaAtomNamecml = (CMLScalar) proplist.get(0);
                String metaAtomNameS = metaAtomNamecml.getStringContent();

                CMLScalar metaAtomTypecml = (CMLScalar) proplist.get(1);
                String metaAtomTypeS = metaAtomTypecml.getStringContent();

                CMLScalar structure = (CMLScalar) proplist.get(2);
                String cmlstructureS = structure.getStringContent();
                StructureAsCML cmlstructure = new StructureAsCML(metaAtomNameS, cmlstructureS);


                CMLScalar internalSymmetryFactorcml = (CMLScalar) proplist.get(3);
                Double internalSymmetryFactor = internalSymmetryFactorcml.getDouble();

                CMLPropertyList props = (CMLPropertyList) proplist.get(4);
                List<CMLProperty> plist = props.getPropertyDescendants();

                CMLProperty prop0 = plist.get(0);
                CMLListOfSymmetryPairs cmlsympairs = new CMLListOfSymmetryPairs();

                cmlsympairs.copyChildrenFrom(prop0);
                cmlsympairs.fromCML();
                ListOfSymmetryPairs pairs = (ListOfSymmetryPairs) cmlsympairs.getStructure();

                SymmetryDefinition definition = new SymmetryDefinition(metaAtomNameS, cmlstructure, pairs, internalSymmetryFactor);
                definition.setMetaAtomType(metaAtomTypeS);
                this.setStructure(definition);
            } catch (CDKException ex) {
                Logger.getLogger(CMLSymmetryDefinition.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CMLSymmetryDefinition.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CMLSymmetryDefinition.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
