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
public class DisassociationEnergy extends DisassociationEnergyStructure {
    Double disassociationEnergy;
    Double errorInEnergy;

    public DisassociationEnergy(AtomContainer substructure,
            String source,
            Double disassociationEnergy, Double errorInEnergy) {
        super(substructure, source);
        this.disassociationEnergy = disassociationEnergy;
        this.errorInEnergy = errorInEnergy;
    }

    public Double getDisassociationEnergy() {
        return disassociationEnergy;
    }

    public void setDisassociationEnergy(Double disassociationEnergy) {
        this.disassociationEnergy = disassociationEnergy;
    }

    public Double getErrorInEnergy() {
        return errorInEnergy;
    }

    public void setErrorInEnergy(Double errorInEnergy) {
        this.errorInEnergy = errorInEnergy;
    }
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Disassociaton Energy = ");
        buf.append(disassociationEnergy.toString() + " +/- " + errorInEnergy);
        buf.append("\n");
        buf.append(super.toString());
        return buf.toString();
    }
}
