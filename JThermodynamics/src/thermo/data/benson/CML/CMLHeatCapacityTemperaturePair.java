/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson.CML;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLScalar;
import thermo.CML.CMLAbstractThermo;
import thermo.data.benson.HeatCapacityTemperaturePair;

/**
 *
 * @author blurock
 */
public class CMLHeatCapacityTemperaturePair   extends CMLAbstractThermo {

    @Override
    public void toCML() {
         HeatCapacityTemperaturePair pair = (HeatCapacityTemperaturePair) getStructure();
        
        this.setId("HeatCapacityTemperaturePair");
        
        CMLScalar tempS = new CMLScalar();
        tempS.setId("Temperature");
        double t = pair.getTemperatureValue();
        tempS.setValue(t);
        this.appendChild(tempS);
        
        CMLScalar hpS = new CMLScalar();
        hpS.setId("HeatCapacity");
        double cp = pair.getHeatCapacityValue();
        hpS.setValue(cp);
        this.appendChild(hpS);

    }

    @Override
    public void fromCML() {
        HeatCapacityTemperaturePair pair = new HeatCapacityTemperaturePair();
        List<CMLElement> proplist = this.getChildCMLElements();
        if (proplist.size() == 2) {
            CMLScalar structcml = (CMLScalar) proplist.get(0);
            String name1 = structcml.getId();
            double temperature = structcml.getDouble();
            pair.setTemperatureValue(temperature);

            CMLScalar cpcml = (CMLScalar) proplist.get(1);
            String name2 = cpcml.getId();
            double cp = cpcml.getDouble();
            pair.setHeatCapacityValue(cp);

         } else {
            int s = proplist.size();
            Logger.getLogger(CMLBensonGroupStructure.class.getName()).log(Level.INFO,
                    "HeatCapacityTemperaturePair:  number of properties wrong:  " + s);
        }
        Logger.getLogger(CMLHeatCapacityTemperaturePair.class.getName()).log(Level.INFO,
                    pair.toString());
        this.setStructure(pair);

    }

}
