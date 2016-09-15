/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import jThergas.data.read.ReadFileToString;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.linearform.NancyLinearFormToGeneralStructure;
import thermo.data.structure.structure.DB.SQLMetaAtomInfo;

/**
 *
 * @author edwardblurock
 */
public class BuildStructureLibrary {

    ThermoSQLConnection connect;
    NancyLinearFormToGeneralStructure linearStructure;
    SQLMetaAtomInfo sqlMetaAtomInfo;
    SQLStructureAsCML sqlStructureAsCML;

    String linearMetaAtomAtomS = "L";

    public BuildStructureLibrary(ThermoSQLConnection c) throws SQLException {
        connect = c;
        linearStructure = new NancyLinearFormToGeneralStructure(connect);
        sqlMetaAtomInfo = new SQLMetaAtomInfo(connect);
        sqlStructureAsCML = new SQLStructureAsCML(connect);
    }
    public void build(File fileF) throws FileNotFoundException, IOException, SQLException, CDKException, ClassNotFoundException {
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
        if(tok.countTokens() > 3) {
            String nancy           = tok.nextToken();
            String nameOfStructure = tok.nextToken();
            String metaAtomName    = tok.nextToken();
            String typeOfStructure = tok.nextToken();

            AtomContainer molecule = linearStructure.convert(nancy);
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
            boolean detectAromaticity = CDKHueckelAromaticityDetector.detectAromaticity(molecule);
            molecule.setID(nameOfStructure);
            StructureAsCML cmlstruct = new StructureAsCML(molecule);
            if(detectAromaticity) {
                System.out.println(cmlstruct.getCmlStructureString());
            }
            MetaAtomInfo info = new MetaAtomInfo();
            info.setElementName(nameOfStructure);
            info.setMetaAtomName(metaAtomName);
            info.setMetaAtomType(typeOfStructure);
            //MetaAtomDefinition metadef = new MetaAtomDefinition(info,cmlstruct);

            sqlMetaAtomInfo.deleteElement(info);
            sqlMetaAtomInfo.addToDatabase(info);
            sqlStructureAsCML.deleteElement(cmlstruct);
            sqlStructureAsCML.addToDatabase(cmlstruct);
        }

    }


}
