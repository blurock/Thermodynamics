/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson.CML;

import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xmlcml.cml.base.CMLElement;
import thermo.CML.*;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLPropertyList;
import org.xmlcml.cml.element.CMLScalar;
import thermo.data.benson.BensonConnectAtomStructure;
import thermo.data.benson.BensonGroupStructure;

/**
 *
 * @author blurock
 */
public class CMLBensonGroupStructure extends CMLAbstractThermo {

    @Override
    public void toCML() {
        BensonGroupStructure struct = (BensonGroupStructure) getStructure();
        this.setId("BensonGroupStructure");

        CMLScalar structureNameS = new CMLScalar();
        structureNameS.setId("StructureName");
        String name = struct.getStructureName();
        structureNameS.setValue(name);
        this.appendChild(structureNameS);
        System.out.println("toCML : " + name);
        CMLScalar centerS = new CMLScalar();
        centerS.setId("CenterAtom");
        centerS.setValue(struct.getCenterAtomS());
        this.appendChild(centerS);

        CMLProperty p3 = new CMLProperty();
        CMLPropertyList clist = new CMLPropertyList();
        for (int i = 0; i < struct.getBondedAtoms().size(); i++) {
            BensonConnectAtomStructure connect = struct.getBondedAtoms().elementAt(i);
            CMLBensonConnectAtomStructure cml = new CMLBensonConnectAtomStructure();
            cml.setStructure(connect);
            cml.toCML();
            CMLProperty prop = (CMLProperty) cml;
            //CMLProperty prop = new CMLProperty();
            //prop.setId("BensonConnectAtomStructure");
            //prop.appendChild(conlist);
            clist.addProperty(prop);
        }
        this.appendChild(clist);
    }

    @Override
    public void fromCML() {
        BensonGroupStructure grp = new BensonGroupStructure();


        List<CMLElement> proplist = this.getChildCMLElements();
        if (proplist.size() == 3) {
            CMLScalar structcml = (CMLScalar) proplist.get(0);
            String name1 = structcml.getId();
            String structureName = structcml.getStringContent();
            grp.setStructureName(structureName);

            CMLScalar centercml = (CMLScalar) proplist.get(1);
            String name2 = centercml.getId();
            String centerAtomS = centercml.getStringContent();
            grp.setCenterAtomS(centerAtomS);

            //Vector<BensonConnectAtomStructure> vec = new Vector<BensonConnectAtomStructure>();
            CMLPropertyList props = (CMLPropertyList) proplist.get(2);
            List<CMLProperty> connections = props.getPropertyDescendants();
            for (int i = 0; i < connections.size(); i++) {
                CMLProperty prop = connections.get(i);
                CMLBensonConnectAtomStructure connect = new CMLBensonConnectAtomStructure();
                connect.copyChildrenFrom(prop);
                connect.fromCML();
                BensonConnectAtomStructure c = (BensonConnectAtomStructure) connect.getStructure();
                grp.addBondedAtom(c);
            }


        } else {
            int s = proplist.size();
            Logger.getLogger(CMLBensonGroupStructure.class.getName()).log(Level.INFO,
                    "CMLBensonGroupStructure:  number of properties wrong:  " + s);
        }
        Logger.getLogger(CMLBensonGroupStructure.class.getName()).log(Level.INFO,
                    grp.toString());
        this.setStructure(grp);
    }
}
