/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import org.openscience.cdk.AtomContainer;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.exception.ThermodynamicException;

/**
 *
 * @author edwardblurock
 */
public class CalculateSymmetryCorrectionInterface {
        ThermoSQLConnection connect;

    public CalculateSymmetryCorrectionInterface(ThermoSQLConnection c) throws ThermodynamicException {
            connect = c;
    }

    public SetOfBensonThermodynamicBase calculate(AtomContainer mol) throws ThermodynamicException {
            SetOfBensonThermodynamicBase corrections = new SetOfBensonThermodynamicBase();
            calculate(mol, corrections);
            
        return corrections;
    }
    public void calculate(AtomContainer mol, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {

    }
}
