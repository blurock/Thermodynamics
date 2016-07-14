/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.disassociation;

import java.sql.SQLException;
import org.openscience.cdk.AtomContainer;
import thermo.data.structure.substructure.SubStructure;
import thermo.data.structure.utilities.MoleculeUtilities;

/**
 *
 * @author edwardblurock
 */
public class DisassociationEnergyStructure extends SubStructure {

    public DisassociationEnergyStructure() throws SQLException {
    }
    public DisassociationEnergyStructure(AtomContainer substructure, String source) {
        this.setSubstructure(substructure);
        this.setSourceOfStructure(source);
    }

    @Override
    public void setSubstructure(AtomContainer substructure) {
        this.substructure = (AtomContainer) MoleculeUtilities.eliminateHydrogens(substructure);
    }

}
