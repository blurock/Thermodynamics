/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson.CML;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLPropertyList;
import org.xmlcml.cml.element.CMLScalar;
import thermo.CML.CMLAbstractThermo;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.HeatCapacityTemperaturePair;

/**
 *
 * @author blurock
 */
public class CMLBensonThermodynamicBase extends CMLAbstractThermo {

    @Override
    public void toCML() {
        BensonThermodynamicBase thermo = (BensonThermodynamicBase) getStructure();

        this.setId("BensonThermodynamicBase");

        CMLScalar idS = new CMLScalar();
        idS.setId("ID");
        String id = thermo.getID();
        idS.setValue(id);
        this.appendChild(idS);

        CMLScalar referenceS = new CMLScalar();
        referenceS.setId("Reference");
        String ref = thermo.getReference();
        referenceS.setValue(ref);
        this.appendChild(referenceS);

        CMLScalar standardEnthalpyS = new CMLScalar();
        standardEnthalpyS.setId("StandardEnthalpy");
        Double enthalpy = thermo.getStandardEnthalpy();
        standardEnthalpyS.setValue(enthalpy.doubleValue());
        this.appendChild(standardEnthalpyS);

        CMLScalar standardEntropyS = new CMLScalar();
        standardEntropyS.setId("StandardEntropy");
        Double entropy = thermo.getStandardEntropy();
        standardEntropyS.setValue(entropy.doubleValue());
        this.appendChild(standardEntropyS);

        CMLPropertyList prop = new CMLPropertyList();
        Iterator<HeatCapacityTemperaturePair> iter = thermo.getSetOfHeatCapacities().iterator();
        while(iter.hasNext()) {
                 HeatCapacityTemperaturePair pair = iter.next();
                CMLHeatCapacityTemperaturePair cmlpair = new CMLHeatCapacityTemperaturePair();
                cmlpair.setStructure(pair);
                cmlpair.toCML();
                prop.addProperty(cmlpair);
           
        }
        this.appendChild(prop);
    }

    @Override
    public void fromCML() {
        BensonThermodynamicBase thermo = new BensonThermodynamicBase();


        List<CMLElement> proplist = this.getChildCMLElements();
        if (proplist.size() == 5) {
            CMLScalar idcml = (CMLScalar) proplist.get(0);
            String name0 = idcml.getId();
            String idS = idcml.getStringContent();
            thermo.setID(idS);

            CMLScalar refcml = (CMLScalar) proplist.get(1);
            String name1 = refcml.getId();
            String referenceS = refcml.getStringContent();
            thermo.setReference(referenceS);

            CMLScalar enthalpycml = (CMLScalar) proplist.get(2);
            String name2 = enthalpycml.getId();
            double enthalpy = enthalpycml.getDouble();
            thermo.setStandardEnthalpy(new Double(enthalpy));

            CMLScalar entropycml = (CMLScalar) proplist.get(3);
            String name3 = entropycml.getId();
            double entropy = entropycml.getDouble();
            thermo.setStandardEntropy(new Double(entropy));

            HashSet<HeatCapacityTemperaturePair> vec = new HashSet<HeatCapacityTemperaturePair>();
            CMLPropertyList props = (CMLPropertyList) proplist.get(4);
            List<CMLProperty> connections = props.getPropertyDescendants();
            for (int i = 0; i < connections.size(); i++) {
                CMLProperty prop = connections.get(i);
                CMLHeatCapacityTemperaturePair connect = new CMLHeatCapacityTemperaturePair();
                connect.copyChildrenFrom(prop);
                connect.fromCML();
                HeatCapacityTemperaturePair c = (HeatCapacityTemperaturePair) connect.getStructure();
                vec.add(c);
            }
            thermo.setSetOfHeatCapacities(vec);
        } else {
            int s = proplist.size();
            Logger.getLogger(CMLBensonThermodynamicBase.class.getName()).log(Level.INFO,
                    "CMLBensonThermodynamicBase:  number of properties wrong:  {0}", s);
        }
        Logger.getLogger(CMLBensonGroupStructure.class.getName()).log(Level.INFO,
                thermo.toString());
        this.setStructure(thermo);

    }
}
