/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

import java.util.StringTokenizer;
import java.util.Vector;
import thermo.properties.SProperties;

/** This is the thermodynamic type that is standard for the Thergas package,
 * meaning a fixed set of temperastures: 300, 400, 500, 600, 800, 1000, 1500K
 * 
 * The name of the type is:
 *
 * @author blurock
 */
public class StandardThergasBensonThermoType extends ThermoInfoType {

    public StandardThergasBensonThermoType() {
        this.setTypeName("Standard");
        //this.setTypeName(SProperties.getProperty("thermo.types.name.thergasbenson"));
        //Vector temps = new Vector();
        //String tempS = SProperties.getProperty("thermo.types.name.thergasbenson");
        String tempS = "300.0,400.0,500.0,600.0,800.0,1000.0,1500.0";
        
        StringTokenizer tok = new StringTokenizer(tempS,",");
        while(tok.hasMoreTokens()) {
            String tS = tok.nextToken();
            Double t = new Double(tS);
            this.add(t);
        }
    }


}
