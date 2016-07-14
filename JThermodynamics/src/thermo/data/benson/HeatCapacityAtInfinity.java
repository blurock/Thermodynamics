/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson;

import java.io.IOException;
import java.sql.SQLException;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.DetectLinearStructure;
import thermo.properties.ChemicalConstants;

/**
 *
 * @author edwardblurock
 */
public class HeatCapacityAtInfinity {

    double rconstant = ChemicalConstants.getGasConstantInCalsMolesK();
    DetectLinearStructure detectLinear;

    public HeatCapacityAtInfinity(ThermoSQLConnection c) throws SQLException, CDKException, ClassNotFoundException, IOException {
        detectLinear = new DetectLinearStructure(c);
    }

    public double heatCapacityAtInfinity(Molecule mol) throws CDKException, IOException {
        double cp = 0.0;
        if (mol.getAtomCount() == 1) {
            cp = 2.5 * rconstant;
        } else {
            boolean islinear = detectLinear.isLinear(mol);
            if (islinear) {
                double N = (double) mol.getAtomCount();
                System.out.println("Linear with " + N + " Atoms");
                cp = (3.0 * N - 1.5) * rconstant;
            } else {
                double N = (double) mol.getAtomCount();
                cp = (3.0 * N - 2.0) * rconstant;
            }
        }
        return cp;
    }
}
