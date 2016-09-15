/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import org.openscience.cdk.AtomContainer;
/**
 *
 * @author edwardblurock
 */
public class HydrocarbonCompleteCombustion {
    String hydrogenS = "H";
    String carbonS = "C";
    String oxygenS = "O";
    AtomCounts atomCounts;
    double co2Count = 0;
    double h2oCount = 0;
    double oCount = 0;

    protected double numberCO2 = 0;
    protected double numberH2O = 0;
    protected double oxidizer = 0;
    
    boolean combustInOxygen = false;
    double percentO2InAir = 0.21;
    double percentN2InAir = 0.79;

    public double getNumberCO2() {
        return numberCO2;
    }

    public double getNumberH2O() {
        return numberH2O;
    }

    public double getOxidizer() {
        return oxidizer;
    }

    public HydrocarbonCompleteCombustion(AtomContainer mol) {
        atomCounts = new AtomCounts(mol);
        findMoleculeCounts();
        }
    public void findMoleculeCounts() {
        int co2 = atomCounts.findCountOfAtom(carbonS);
        int h2o = atomCounts.findCountOfAtom(hydrogenS);
        int o = atomCounts.findCountOfAtom(oxygenS);
        co2Count = (double) co2;
        h2oCount = (double) h2o;
        oCount = (double) o;
    }
    public void findCompleteCombustionInOxygen(){
        numberCO2 = co2Count;
        numberH2O = h2oCount/2.0;
        double numberO = 2.0 * numberCO2 + numberH2O - oCount;
        oxidizer = numberO/2;
        combustInOxygen = true;
    }
    public void findCombustionInAir() {
        numberCO2 = co2Count;
        numberH2O = h2oCount/2.0;
        double numberO = 2.0 * co2Count + h2oCount - oCount;
        oxidizer = numberO/percentO2InAir;
    }
}
