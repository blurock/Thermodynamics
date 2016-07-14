/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.properties;

import thermo.data.benson.SetOfThermodynamicTypes;
import thermo.data.benson.ThermoInfoType;

/**
 *
 * @author blurock
 */
public class ThermodynamicProperties extends SProperties {

        protected static SetOfThermodynamicTypes types = new SetOfThermodynamicTypes();
        
        public static void addThermodynamicType(ThermoInfoType type) {
            types.addThermodynamicType(type);
        }
        public static ThermoInfoType getTherodyInfoType(String type) {
            return types.getType(type);
        }
}
