/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.properties;

/**
 *
 * @author blurock
 */
public class ChemicalConstants extends SProperties {

    /**
     * 
     * @return
     */
    public static double getGasConstantInCalsMolesK() {
        String gasconstantS = getProperty("thermo.data.gasconstant.clasmolsk");
        if(gasconstantS == null) {
            gasconstantS = "1.98587755";
        }
        Double gasconstantD = new Double(gasconstantS.trim());
        return gasconstantD.doubleValue();
    }
}
