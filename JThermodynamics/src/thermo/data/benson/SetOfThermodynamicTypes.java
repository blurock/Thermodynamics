/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

import java.util.TreeMap;
import thermo.properties.SProperties;

/**
 *
 * @author blurock
 */
public class SetOfThermodynamicTypes {
    /**
     * 
     */
    protected TreeMap<String,ThermoInfoType> setOfTypes = null;
    
    public SetOfThermodynamicTypes() {
        initialize();
    }
    void initialize() {
        setOfTypes = new TreeMap<String,ThermoInfoType>();
        StandardThergasBensonThermoType type = new StandardThergasBensonThermoType();
        setOfTypes.put(type.getTypeName(), type);
    }
    public ThermoInfoType getDefaultType() {
        return setOfTypes.get(SProperties.getProperty("thermo.types.name.thergasbenson"));
    }
    public ThermoInfoType getType(String typename) {
        if(setOfTypes == null) {
            initialize();
        }
        return setOfTypes.get(typename);
    }
    public void addThermodynamicType(ThermoInfoType type) {
        setOfTypes.put(type.getTypeName(), type);
    }
}
