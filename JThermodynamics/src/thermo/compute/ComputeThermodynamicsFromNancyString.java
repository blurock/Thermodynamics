/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compute;

import jThergas.data.read.ReadFileToString;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.NASAPolynomialFromBenson;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;

/** Main class for Thermodyanamic Calculations
 *
 * The arguments on the line are one of two forms:
 * 1. MOLECULE linear name
 * 2. FILE root
 *
 * where:
 * linear: The molecule in nancy linear form
 * name: The name of the molecule
 * root: The root name of a file which has on each line: linear name
 *
 * @author edwardblurock
 */
public class ComputeThermodynamicsFromNancyString {
    static String fileType = "FILE";
    static String moleculeType = "MOLECULE";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Two arguments are needed:");
            System.out.println("Type of input: FILE MOLECULE");
            System.out.println("If FILE, then followed by name of file");
            System.out.println("If MOLECULE, then the molecule in Nancy Linear Form");
            System.out.println("If MOLECULE, the next argument is the name of the molecule");
        } else {
            String type = new String(args[0]);
            String name = new String(args[1]);
            if(type.startsWith(fileType)) {
                try {
                    computeFromFile(name);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
                }
            } if(type.startsWith(moleculeType)) {
                try {
                    if(args.length >= 3) {
                        String nancy = name;
                        name = new String(args[2]);
                        computeFromNancyLinearForm(nancy,name);
                        } else {
                            System.out.println("Expecting name of molecule as third argument");
                        }

                } catch (ThermodynamicComputeException ex) {
                    Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Expecting type: " + fileType + " or " + moleculeType);
            }
        }

    }
    private static void computeFromFile(String fileroot) throws FileNotFoundException {
        System.out.println("Compute Thermodynamics of molecule: " + fileroot);
        ThermoSQLConnection c = new ThermoSQLConnection();
        c.connect();
        ComputeThermodynamicsFromMolecule compute = null;
        try {
            compute = new ComputeThermodynamicsFromMolecule(c);
        } catch (ThermodynamicComputeException ex) {
            Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
        }
        String fileS = new String(fileroot + ".dat");
        File fileF = new File(fileS);
        ReadFileToString readfile = new ReadFileToString();
        readfile.read(fileF);
        StringTokenizer tok = new StringTokenizer(readfile.outputString,"\n");
        String outputS = fileroot + ".thm";
        PrintWriter  prt = new PrintWriter(new FileOutputStream(outputS));
        prt.println("THERM");
        prt.println("   300.000  1000.000  5000.000");
        while(tok.hasMoreTokens()) {
            String line = tok.nextToken();
            System.out.println(line);
            try {
                StringTokenizer linetok = new StringTokenizer(line);
                String nancy = linetok.nextToken();
                String name = linetok.nextToken();
                ThermodynamicInformation thermo = compute.computeThermodynamics(nancy);
                NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) thermo);
                nasa.name = name;
                prt.print(nasa.toString());
            } catch (ThermodynamicComputeException ex) {
                Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
        prt.println("END");
         prt.close();
    }
    private static void computeFromNancyLinearForm(String nancy,String name) throws ThermodynamicComputeException {
        System.out.println("Compute Thermodynamics of molecule: '" + name + "'='" + nancy + "'");
        ThermoSQLConnection c = new ThermoSQLConnection();
        c.connect();
        ComputeThermodynamicsFromMolecule compute = new ComputeThermodynamicsFromMolecule(c);
        ThermodynamicInformation thermo = compute.computeThermodynamics(nancy);
        NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) thermo, compute.getMolecule());
        nasa.name = name;
        System.out.println(nasa.toString());
    }
}
