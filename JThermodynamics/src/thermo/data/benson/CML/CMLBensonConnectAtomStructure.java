/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson.CML;

import java.util.List;
import javax.xml.namespace.QName;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLScalar;
import thermo.CML.CMLAbstractThermo;
import thermo.data.benson.BensonConnectAtomStructure;

/**
 *
 * @author blurock
 */
public class CMLBensonConnectAtomStructure extends CMLAbstractThermo {

    public CMLBensonConnectAtomStructure() {
    }

    public void toCML() {
        BensonConnectAtomStructure ben = (BensonConnectAtomStructure) getStructure();
        //System.out.println("ClassName: " + BensonConnectAtomStructure.class);
        //String name = ben.getClass().getName();
        this.setId("BensonCommectAtomStructure");
        //this.setDictRef("BensonCommectAtomStructure");

        //CMLProperty p1 = new CMLProperty();
        CMLScalar atomS = new CMLScalar();
        atomS.setId("AtomAsString");
        atomS.setValue(ben.getConnectedAtomS());
        this.appendChild(atomS);
        //p1.setId("AtomAsString");
        //p1.setProperty("AtomAsString", atomS);
        //this.addProperty(p1);


        //CMLProperty p2 = new CMLProperty();
        CMLScalar mult = new CMLScalar();
        mult.setId("BensonAtomMultiplicity");
        mult.setValue(ben.getMultiplicity());
        //mult.setDataType(CMLScalar.XSD_INTEGER);
        this.appendChild(mult);
        //this.addProperty(p2);

    }

    public void fromCML() {
        List<CMLElement> proplist = this.getChildCMLElements();
        if(proplist.size() == 2) {
            //CMLProperty cml2 = proplist.get(1);
            //CMLScalar mult = (CMLScalar) cml2.getChild();
            CMLScalar mult = (CMLScalar) proplist.get(1);
            String name = mult.getId();
            int multi = mult.getInt();
            
            //CMLProperty cml1 = proplist.get(0);
            //CMLScalar atomS = (CMLScalar) cml1.getChild();
            CMLScalar atomS = (CMLScalar) proplist.get(0);
            String atomId = atomS.getId();
            String a = atomS.getStringContent();
            
            this.setStructure(new BensonConnectAtomStructure(a,multi));
        }

    }
}
