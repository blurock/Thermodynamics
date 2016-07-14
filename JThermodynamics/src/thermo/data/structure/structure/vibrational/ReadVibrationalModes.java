/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational;

import jThergas.data.read.ReadFileToString;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.vibrational.DB.SQLVibrationalStructureInfo;

/**
 *
 * @author edwardblurock
 */
public class ReadVibrationalModes {
    ThermoSQLConnection connect;
    SQLVibrationalStructureInfo sqlVibrationalStructure;

    public ReadVibrationalModes(ThermoSQLConnection c) {
        connect = c;
        sqlVibrationalStructure = new SQLVibrationalStructureInfo(connect);
    }

    public void build(File fileF, String referenceS) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException, CDKException {
        ReadFileToString read = new ReadFileToString();
        FileReader r = new FileReader(fileF);
        BufferedReader breader = new BufferedReader(r);

        read.read(breader);

        build(read.outputString);
    }

     void build(String data) throws SQLException, CDKException, ClassNotFoundException, IOException {
        StringTokenizer tok = new StringTokenizer(data,"\n");
        while(tok.hasMoreTokens()){
            String line = tok.nextToken();
            parseLine(line);
        }
    }

    public void parseLine(String line) throws SQLException, CDKException, ClassNotFoundException, IOException {
        StringTokenizer tok = new StringTokenizer(line);
        if(tok.countTokens() >= 4) {
            String elementName   = tok.nextToken();
            String structureName = tok.nextToken();
            String frequencyS     = tok.nextToken();
            String symmetryS      = tok.nextToken();

            Double frequencyD = new Double(frequencyS);
            Double symmetryD  = new Double(symmetryS);

            //ArrayList<String> types = new ArrayList<String>();

            VibrationalStructureInfo info = new VibrationalStructureInfo(elementName,structureName,
                    frequencyD.doubleValue(),symmetryD.doubleValue());
            sqlVibrationalStructure.deleteByKey(info.getElementName());
            sqlVibrationalStructure.addToDatabase(info);
        }

    }
}
