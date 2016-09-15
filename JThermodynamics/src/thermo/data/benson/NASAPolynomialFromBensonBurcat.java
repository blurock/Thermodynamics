/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.GMatrix;
import javax.vecmath.GVector;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.DetectLinearStructure;
import thermo.exception.ThermodynamicComputeException;
import thermo.properties.ChemicalConstants;

/**
 *
 * @author edwardblurock
 */
public class NASAPolynomialFromBensonBurcat extends NASAPolynomialFromBenson {

    double rconstant = ChemicalConstants.getGasConstantInCalsMolesK();
    DetectLinearStructure detectLinear;
    double cpinfinity;
    double cpzero;
    double[] burcatcoeffs;
    ThermoSQLConnection connection;
    AtomContainer molecule;
    double[] temperatures = {1000.0,1100.0,1200.0,1500.0};
    double B;
    public NASAPolynomialFromBensonBurcat(BensonThermodynamicBase b, AtomContainer molecule, ThermoSQLConnection c) throws SQLException, CDKException, ClassNotFoundException, IOException, ThermodynamicComputeException {
        connection = c;
        detectLinear = new DetectLinearStructure(connection);
        heatCapacityAtInfinityAndZero(molecule);
        fillInData(b);
        fillInMoleculeProperties(molecule);

    }

    @Override
    protected void fillInParameters() throws ThermodynamicComputeException {
        try {
            calculateBurcatCoefficients();
            super.fillInParameters(lower,lowtemperatures,benson);
        } catch (CDKException ex) {
            Logger.getLogger(NASAPolynomialFromBensonBurcat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NASAPolynomialFromBensonBurcat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    void calculateBurcatCoefficients() throws CDKException, IOException, ThermodynamicComputeException {
        burcatcoeffs = new double[temperatures.length];
        double numtemperatures = temperatures.length;
        heatCapacityAtInfinityAndZero(molecule);
        B = 335.0 - numtemperatures;
        int n = temperatures.length;
        GMatrix mat = new GMatrix(n,n);
        GVector cps = new GVector(n);
        for(int i=0;i<n;i++) {
            double t = temperatures[i];
            double y = t/(t+B);
            double cpt = benson.getHeatCapacity(t) - cpzero;
            cps.setElement(i,cpt);
            double yp = 1.0;
            for(int j=0;j<n;j++) {
                double value = y*y*(cpinfinity - cpzero)*(y-1)*yp;
                mat.setElement(i, j, value);
                yp *= y;
            }
        }
        GMatrix inverse = new GMatrix(n,n);
        inverse.invert(mat);
        GVector coeffs = new GVector(5);
        coeffs.mul(inverse, cps);
        burcatcoeffs = new double[n];
        for(int i=0;i<n;i++) {
            burcatcoeffs[i] = coeffs.getElement(i);
        }
    }
    public double calculateFromBurcat(double t) {
        int n = molecule.getAtomCount();
        double y = t/(t+B);
            double yp = 1.0;
            double value = 0.0;
            for(int j=0;j<n;j++) {
                double a = y*y*(cpinfinity - cpzero)*(y-1)*yp;
                value *= a * burcatcoeffs[j];
                yp *= y;
            }
        return value + cpzero;
    }
    public void heatCapacityAtInfinityAndZero(AtomContainer mol) throws CDKException, IOException {
        double cp = 0.0;
        if (mol.getAtomCount() == 1) {
            cpinfinity = 2.5 * rconstant;
            cpzero = 0.0;
        } else {
            boolean islinear = detectLinear.isLinear(mol);
            if (islinear) {
                double N = (double) mol.getAtomCount();
                System.out.println("Linear with " + N + " Atoms");
                cpinfinity = (3.0 * N - 1.5) * rconstant;
                cpzero = 3.5*rconstant;
            } else {
                double N = (double) mol.getAtomCount();
                cpinfinity = (3.0 * N - 2.0) * rconstant;
                cpzero = 4.0*rconstant;
            }
        }
    }

}
