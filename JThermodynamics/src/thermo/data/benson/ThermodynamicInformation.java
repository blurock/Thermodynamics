/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

import thermo.exception.ThermodynamicComputeException;

/**
 *
 * @author blurock
 */
public interface ThermodynamicInformation {
    public String getThermodynamicType();
    public void setThermodynamicType(String type);
    public double getStandardEnthalpy298();
    public double getStandardEntropy298();
    public double computeEnthalpy(double temperature) throws ThermodynamicComputeException;
    public double computeEntropy(double temperature)  throws ThermodynamicComputeException;
    public double getHeatCapacity(double temperature)  throws ThermodynamicComputeException ;
    public String getName();
    public void setName(String name);
}
