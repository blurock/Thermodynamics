/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.compute;

import jThergas.data.read.ReadFileToString;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.NASAPolynomialFromBenson;
import thermo.data.benson.SetOfThermodynamicInformation;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;

/**
 *
 * @author edwardblurock
 */
public class ComputeThermoFromNancyStringFile {

    ThermoSQLConnection connect;
    ComputeThermodynamicsFromMolecule compute;

    public ComputeThermoFromNancyStringFile() throws ThermodynamicComputeException {
        connect = new ThermoSQLConnection();
        connect.connect();
        compute = new ComputeThermodynamicsFromMolecule(connect);
    }

    public SetOfThermodynamicInformation computeFromFile(File fileF) throws FileNotFoundException, ThermodynamicComputeException {
        
        System.out.println("Compute Thermodynamics of molecule: " + fileF.toString());
        
        SetOfThermodynamicInformation set = new SetOfThermodynamicInformation("Benson");
        StringBuilder buf = new StringBuilder();

        ReadFileToString readfile = new ReadFileToString();
        readfile.read(fileF);
        StringTokenizer tok = new StringTokenizer(readfile.outputString, "\n");

        buf.append("THERM");
        buf.append("   300.000  1000.000  5000.000");
        while (tok.hasMoreTokens()) {
            String line = tok.nextToken();
            NASAPolynomialFromBenson nasa = computeBenson(line);
            buf.append(nasa.toString());
            set.add(nasa);
        }
        buf.append("END");
        return set;
    }

    private NASAPolynomialFromBenson computeBenson(String line) throws ThermodynamicComputeException {
        StringTokenizer linetok = new StringTokenizer(line);
        String name = linetok.nextToken();
        String nancy = linetok.nextToken();
        ThermodynamicInformation thermo = compute.computeThermodynamics(nancy);
        NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) thermo);
        nasa.name = name;

        return nasa;
    }
}
