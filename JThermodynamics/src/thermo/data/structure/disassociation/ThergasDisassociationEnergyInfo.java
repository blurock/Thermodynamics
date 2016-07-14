/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.disassociation;

import org.openscience.cdk.AtomContainer;

/**
 *
 * @author edwardblurock
 */
public class ThergasDisassociationEnergyInfo extends DisassociationEnergy {
    String sourceLabel1;
    String sourceLabel2;

    public ThergasDisassociationEnergyInfo(AtomContainer substructure, String source, Double disassociationEnergy, Double errorInEnergy, String sourceLabel1, String sourceLabel2) {
        super(substructure, source, disassociationEnergy, errorInEnergy);
        this.sourceLabel1 = sourceLabel1;
        this.sourceLabel2 = sourceLabel2;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Thergas Diassociation Info " + sourceLabel1 + "\t" + sourceLabel2);
        buf.append("\n");
        buf.append(super.toString());
        return buf.toString();
    }


}
