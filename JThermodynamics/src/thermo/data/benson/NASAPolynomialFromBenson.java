/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

import Jama.Matrix;
import javax.vecmath.GMatrix;
import javax.vecmath.GVector;

import org.openscience.cdk.AtomContainer;

import thermo.exception.ThermodynamicComputeException;


/**
 *
 * @author blurock
 */
public class NASAPolynomialFromBenson extends NASAPolynomial {
    BensonThermodynamicBase benson;
    public double defaultLowTLimit = 300.0;
    public double defaultHighTLimit = 2000.0;
    double[] lowtemperatures = {300.0,500.0,700.0,900.0};
    double[] hightemperatures = {1100.0,1200.0,2000.0,2500.0};
    double commonTemperature = 1000.0;
    double deltaTemperature = 25.0;
    //! Gas Constant 1.987 	cal·K−1·mol−1
    double Rconstant = 1.987;
    double dHtoCalories = 1000.0;

    public boolean computeWithLeastSquares = false;
    public void fillInData(BensonThermodynamicBase b) throws ThermodynamicComputeException {
        benson = b;
        fillInParameters();
        this.name = benson.getID();
        lowerT = defaultLowTLimit;
        upperT = defaultHighTLimit;
        middleT = commonTemperature;
        this.name = benson.getID();
    }
    public NASAPolynomialFromBenson() {
    }
    public NASAPolynomialFromBenson(BensonThermodynamicBase b, AtomContainer molecule) throws ThermodynamicComputeException {
        fillInData(b);
        fillInMoleculeProperties(molecule);
    }
    public NASAPolynomialFromBenson(BensonThermodynamicBase b) throws ThermodynamicComputeException {
        fillInData(b);
    }

    private GMatrix BuildCpTemperatureMatrix(double[] temperatures) {
        GMatrix matrix = new GMatrix(5,5);
        for(int i=0;i<4;i++) {
            double mattemperature = 1.0;
            double temperature = temperatures[i];
            for(int j=0;j<5;j++) {
                matrix.setElement(i, j, mattemperature);
                mattemperature *= temperature;
            }
        }

        double t = commonTemperature;
        
        matrix.setElement(4,0,0.0);
        matrix.setElement(4,1,1.0);
        matrix.setElement(4,2,2.0*t);
        matrix.setElement(4,3,3.0*t*t);
        matrix.setElement(4,4,4.0*t*t*t);
        
        /*
        matrix.setElement(4,0,1.0);
        matrix.setElement(4,1,t);
        matrix.setElement(4,2,t*t);
        matrix.setElement(4,3,t*t*t);
        matrix.setElement(4,4,t*t*t*t);
        */
        return matrix;
    }
    private GVector buildCPValueVector(double[] temperatures) throws ThermodynamicComputeException {
        GVector vector = new GVector(5);
        vector.setElement(0, benson.getHeatCapacity(temperatures[0])/Rconstant);
        vector.setElement(1, benson.getHeatCapacity(temperatures[1])/Rconstant);
        vector.setElement(2, benson.getHeatCapacity(temperatures[2])/Rconstant);
        vector.setElement(3, benson.getHeatCapacity(temperatures[3])/Rconstant);
        //vector.setElement(4, benson.getHeatCapacity(commonTemperature)/Rconstant);
        
        double cpdiff = calculateCpSlopeAroundCommonTemperature();
        vector.setElement(4, cpdiff);
         
        return vector;
    }

    private double calculateA6(double[] c) throws ThermodynamicComputeException {
        double t = commonTemperature;
        double dH = benson.computeEnthalpy(t)*1000.0;
        double cpterm = c[0]*t +
                        c[1]*t*t/2.0 +
                        c[2]*t*t*t/3.0 +
                        c[3]*t*t*t*t/4.0 +
                        c[4]*t*t*t*t*t/5.0;
        double a6 = dH/Rconstant - cpterm;
        return a6;
    }
    /*!
     * S/R  = a1 lnT + a2 T + a3 T^2 /2 + a4 T^3 /3 + a5 T^4 /4 + a7
     */
    private double calculateA7(double[] c) throws ThermodynamicComputeException {
        double t = commonTemperature;
        double entropy = benson.computeEntropy(t);
        double cpterms = c[0]*Math.log(t) +
                    c[1]*t +
                    c[2]*t*t/2.0 +
                    c[3]*t*t*t/3.0 +
                    c[4]*t*t*t*t/4.0;
        double a7 = entropy/Rconstant -cpterms;
        return a7;
    }

    private double calculateCpSlopeAroundCommonTemperature() throws ThermodynamicComputeException {
        double tlow = commonTemperature - deltaTemperature;
        double thigh = commonTemperature + deltaTemperature;
        double t = commonTemperature;
        double cplow = benson.getHeatCapacity(tlow)/Rconstant;
        double cphigh = benson.getHeatCapacity(thigh)/Rconstant;
        double cpcommon = benson.getHeatCapacity(t)/Rconstant;
        double slopelow = (cplow-cpcommon)/(tlow-t);
        double slopehigh = (cphigh-cpcommon)/(thigh-t);
        double ave = (slopelow + slopehigh)/2.0;
        return ave;
    }
    protected void fillInParameters() throws ThermodynamicComputeException {
        if(computeWithLeastSquares)
            fillInParametersLeastSquares();
        else
            fillInParameters(benson);
        
    }

    private void fillInParameters(BensonThermodynamicBase b) throws ThermodynamicComputeException {
        if(computeWithLeastSquares) {
            benson = b;
            fillInParametersLeastSquares();
         } else {
            fillInParameters(lower,lowtemperatures,benson);
            fillInParameters(upper,hightemperatures,benson);
         }
    }

    protected void fillInParameters(double[] coefficients,double[] temperatures,BensonThermodynamicBase benson) throws ThermodynamicComputeException {
        GMatrix tmat = BuildCpTemperatureMatrix(temperatures);
        GVector cpvec = buildCPValueVector(temperatures);
        GMatrix inverse = new GMatrix(5,5); 
        inverse.invert(tmat);
        GVector coeffs = new GVector(5);
        coeffs.mul(inverse, cpvec);
        
        //GMatrix mtest = new GMatrix(5,5);
        //GVector mtest = new GVector(5);
        //mtest.mul(tmat,coeffs);
        coefficients[0] = coeffs.getElement(0);
        coefficients[1] = coeffs.getElement(1);
        coefficients[2] = coeffs.getElement(2);
        coefficients[3] = coeffs.getElement(3);
        coefficients[4] = coeffs.getElement(4);
        
        GVector test = new GVector(5);
        test.mul(tmat, coeffs);
        
        coefficients[5] = calculateA6(coefficients);
        coefficients[6] = calculateA7(coefficients);

    }
    void fillInParametersLeastSquares() throws ThermodynamicComputeException {
        int numpoints = 50;
        lower = fillInParametersLeastSquares(numpoints,lowerT,middleT);
        upper = fillInParametersLeastSquares(numpoints,middleT,upperT);
    }
    double[] fillInParametersLeastSquares(int numpoints, double lower, double upper) throws ThermodynamicComputeException {
        int n1 = numpoints;
        int n2 = numpoints/3;
        int n3 = numpoints/3;
        //n2 = 0;
        //n3 = 0;
        int n = n1+n2+n3;
        double interval1 = (upper-lower)/n1;
        double interval2 = (upper-lower)/n2;
        double interval3 = (upper-lower)/n3;
        Matrix tmat = new Matrix(n,5);
        Matrix ans = new Matrix(n,1);
        int cnt = -1;
        for(int i=0;i<n1;i++) {
            double t = lower + i*interval1;
            cnt++;
            double cp = benson.getHeatCapacity(t)/Rconstant;
            System.out.println(cnt + "\t" + t + "\t" + cp);
            ans.set(cnt, 0, cp);
            tmat.set(cnt, 0, 1.0);
            tmat.set(cnt, 1, t);
            tmat.set(cnt, 2, t*t);
            tmat.set(cnt, 3, t*t*t);
            tmat.set(cnt, 4, t*t*t*t);
            //tmat.set(cnt, 5, 0.0);
            //tmat.set(cnt, 6, 0.0);
}
        for(int i=0;i<n2;i++) {
            double t = lower+i*interval2;
            double h = benson.computeEnthalpy(t)*1000.0/(Rconstant*t);
            cnt++;
            ans.set(cnt, 0, h);
            tmat.set(cnt, 0, 1.0);
            tmat.set(cnt, 1, t/2.0);
            tmat.set(cnt, 2, t*t/3.0);
            tmat.set(cnt, 3, t*t*t/4.0);
            tmat.set(cnt, 4, t*t*t*t/5.0);
            //tmat.set(cnt, 5, 1.0/t);
            //tmat.set(cnt, 6, 0.0);
}
        for(int i=0;i<n3;i++) {
            double t = lower+i*interval3;
            double s = benson.computeEntropy(t)/Rconstant;
            cnt++;
            ans.set(cnt, 0, s);
            tmat.set(cnt, 0, Math.log(t));
            tmat.set(cnt, 1, t);
            tmat.set(cnt, 2, t*t/2.0);
            tmat.set(cnt, 3, t*t*t/3.0);
            tmat.set(cnt, 4, t*t*t*t/4.0);
            //tmat.set(cnt, 5, 0.0);
            //tmat.set(cnt, 6, 1.0);
        }
        Matrix mat = tmat.solve(ans);
        Matrix residule = tmat.times(mat).minus(ans);
        double rnorm = residule.normInf();
        System.out.println("Residule: " + rnorm);
        double[] coeffs = mat.getRowPackedCopy();

        double[] c = new double[7];
        for(int i=0;i<5;i++)
            c[i] = coeffs[i];
        c[5] = 0.0;
        c[6] = 0.0;
        return c;
    }
}
