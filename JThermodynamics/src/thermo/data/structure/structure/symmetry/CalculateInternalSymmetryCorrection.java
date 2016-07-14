/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.structure.symmetry.DB.SQLSetOfSymmetryDefinitions;
import thermo.exception.ThermodynamicException;
import thermo.properties.SProperties;

/**
 *
 * @author edwardblurock
 */
public class CalculateInternalSymmetryCorrection extends CalculateSymmetryCorrectionInterface  {

    String internalS = "InternalSymmetry";
    String referenceS = "Internal Symmetry Correction";
    SQLSetOfSymmetryDefinitions setOfDefinitions;
    DetermineSymmetryFromSingleDefinition fromSingleDefinition;
    DetermineInternalSymmetry determineTotal;
    double gasConstant;

    public CalculateInternalSymmetryCorrection(ThermoSQLConnection c) throws ThermodynamicException {
        super(c);
        try {
            setOfDefinitions = new SQLSetOfSymmetryDefinitions(connect, internalS);
            //System.out.println(setOfDefinitions.toString());
            fromSingleDefinition = new DetermineInternalSymmetryFromSingleDefinition();
            determineTotal = new DetermineInternalSymmetry(fromSingleDefinition, setOfDefinitions);
            String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
            gasConstant = Double.valueOf(gasconstantS).doubleValue();
        } catch (SQLException ex) {
            Logger.getLogger(CalculateInternalSymmetryCorrection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void calculate(Molecule mol, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
        try {
            int internalsymmetry = calculateInternalSymmetry(mol);
            if (internalsymmetry > 0.0 && internalsymmetry != 1.0) {
                double correction = -gasConstant * Math.log(internalsymmetry);
                String name = internalS + "(" + Integer.toString(internalsymmetry) + ")";
                BensonThermodynamicBase benson = new BensonThermodynamicBase(name, null, 0.0, correction);
                benson.setReference(name);
                corrections.add(benson);
            }
        } catch (CDKException ex) {
            Logger.getLogger(CalculateInternalSymmetryCorrection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int calculateInternalSymmetry(Molecule mol) throws CDKException {
        return determineTotal.determineSymmetry(mol);
    }
}
