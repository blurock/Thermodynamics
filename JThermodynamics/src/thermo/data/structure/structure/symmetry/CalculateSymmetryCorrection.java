/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import thermo.data.benson.DB.ThermoSQLConnection;

import org.openscience.cdk.AtomContainer;

import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.exception.ThermodynamicException;

/**
 *
 * @author edwardblurock
 */
public class CalculateSymmetryCorrection extends CalculateSymmetryCorrectionInterface {
    CalculateOpticalSymmetryCorrection optical;
    CalculateInternalSymmetryCorrection internal;
    CalculateExternalSymmetryCorrection external;

    public CalculateSymmetryCorrection(ThermoSQLConnection c) throws ThermodynamicException {
        super(c);
            optical = new CalculateOpticalSymmetryCorrection(connect);
            internal = new CalculateInternalSymmetryCorrection(connect);
            external = new CalculateExternalSymmetryCorrection(connect);
    }
    public void calculate(AtomContainer mol, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
            optical.calculate(mol, corrections);
            internal.calculate(mol, corrections);
            external.calculate(mol, corrections);
    }
    void calculate(AtomContainer R, AtomContainer RH, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
        SetOfBensonThermodynamicBase correctionsR = new SetOfBensonThermodynamicBase();
        SetOfBensonThermodynamicBase correctionsRH = new SetOfBensonThermodynamicBase();
        this.calculate(RH, correctionsRH);
        this.calculate(R, correctionsR);
    }

}
