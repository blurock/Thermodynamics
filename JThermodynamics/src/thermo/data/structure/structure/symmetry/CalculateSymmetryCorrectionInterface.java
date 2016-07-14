/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
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

    public SetOfBensonThermodynamicBase calculate(Molecule mol) throws ThermodynamicException {
            SetOfBensonThermodynamicBase corrections = new SetOfBensonThermodynamicBase();
            calculate(mol, corrections);
            
        return corrections;
    }
    public void calculate(Molecule mol, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {

    }
}
