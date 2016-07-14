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
import org.openscience.cdk.exception.CDKException;

/** Main class for Thermodynamic Calculations
 *
 * The arguments on the line are one of two forms:
 * 1. MOLECULE linear name
 * 2. FILE root
 *
 * where:
 * InChI: The molecule in InChI form
 * name: The name of the molecule
 * root: The root name of a file which has on each line: inchi name
 *
 * @author amritjalan
 */
public class ComputeThermoFromInChIString {
    static String fileType = "FILE";
    static String moleculeType = "InChI";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CDKException {
        if(args.length < 2) {
            System.out.println("Two arguments are needed:");
            System.out.println("Type of input: FILE InChI");
            System.out.println("If FILE, then followed by name of file");
            System.out.println("If InChI, then the molecule in InChI Form");
            System.out.println("If InChI, the next argument is the name of the molecule");
        } else {
            String type = args[0];
            String name = args[1];
            if(type.startsWith(fileType)) {
                try {
                    computeFromFile(name);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
                }
            } if(type.startsWith(moleculeType)) {
                try {
                    if(args.length >= 3) {
                        String inchi = name;
                        name = new String(args[2]);
                        computeFromInChI(inchi,name);
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
    private static void computeFromFile(String fileroot) throws FileNotFoundException, CDKException {
        System.out.println("Compute Thermodynamics of molecule: " + fileroot);
        ThermoSQLConnection c = new ThermoSQLConnection();
        c.connect();
        ComputeThermodynamicsFromMolecule compute = null;
        try {
            compute = new ComputeThermodynamicsFromMolecule(c);
        } catch (ThermodynamicComputeException ex) {
            Logger.getLogger(ComputeThermoFromInChIString.class.getName()).log(Level.SEVERE, null, ex);
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
                String inchi = linetok.nextToken();
                String name = linetok.nextToken();
                ThermodynamicInformation thermo = compute.computeThermodynamicsFromInChI(inchi);
                NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) thermo);
                nasa.name = name;
                prt.print(nasa.toString());
            } catch (ThermodynamicComputeException ex) {
                Logger.getLogger(ComputeThermoFromInChIString.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
        prt.println("END");
         prt.close();
    }
    private static void computeFromInChI(String inchi,String name) throws ThermodynamicComputeException, CDKException {
        System.out.println("Compute Thermodynamics of molecule: '" + name + "'='" + inchi + "'");
        ThermoSQLConnection c = new ThermoSQLConnection();
        c.connect();
        ComputeThermodynamicsFromMolecule compute = new ComputeThermodynamicsFromMolecule(c);
        ThermodynamicInformation thermo = compute.computeThermodynamicsFromInChI(inchi);
        NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) thermo);
        nasa.name = name;
        System.out.println(nasa.toString());
    }
}
