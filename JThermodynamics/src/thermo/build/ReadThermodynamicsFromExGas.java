/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.build;

import jThergas.data.read.ReadFileToString;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import thermo.data.benson.NASAPolynomial;
import thermo.data.benson.SetOfThermodynamicInformation;

/**
 *
 * @author edwardblurock
 */
public class ReadThermodynamicsFromExGas {

	String thermoS = "THERMO";
	String endS = "END";

	public ReadThermodynamicsFromExGas() {
	}

	public SetOfThermodynamicInformation read(File fileF) throws IOException {
		ReadFileToString read = new ReadFileToString();
		read.read(fileF);
		StringTokenizer tok = new StringTokenizer(read.outputString, "\n");
		boolean notdone = true;
		while (notdone) {
			String line1 = tok.nextToken();
			if (line1.startsWith(thermoS)) {
				notdone = false;
			} else {
				notdone = tok.hasMoreTokens();
			}
		}

		SetOfThermodynamicInformation set = new SetOfThermodynamicInformation(fileF.toString());
		String temp = tok.nextToken();
		System.out.println("Thermodynamic temperature: '" + temp + "'");
		notdone = true;
		while (notdone) {
			String line1 = tok.nextToken();
			if (line1 != null) {
				line1 = line1.trim();
				if (!line1.startsWith(endS)) {
					String line2 = tok.nextToken();
					String line3 = tok.nextToken();
					String line4 = tok.nextToken();
					NASAPolynomial nasa = new NASAPolynomial();
					nasa.parse(line1, line2, line3, line4);
					set.add(nasa);
				} else {
					notdone = false;
				}
			} else {
				System.out.println("No END found");
			}
		}
		return set;
	}
}
