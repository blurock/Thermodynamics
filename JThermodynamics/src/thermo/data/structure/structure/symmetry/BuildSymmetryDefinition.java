/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry;

import jThergas.data.read.ReadFileToString;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.DB.SQLStructureAsCML;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.DB.SQLSymmetryDefinition;

/**
 *
 * @author edwardblurock
 */
public class BuildSymmetryDefinition {

    String symmetryKeyWord = "Symmetry";
    String symmetryS = "ExternalSymmetry";
    ThermoSQLConnection connect;
    SQLSymmetryDefinition sqlSymmetryDefinition;
    SQLStructureAsCML sqlcmlstruct;

    public BuildSymmetryDefinition(ThermoSQLConnection connect) {
        this.connect = connect;
        sqlSymmetryDefinition = new SQLSymmetryDefinition(connect);
        sqlcmlstruct = new SQLStructureAsCML(connect);
    }

    public void build(File fileF) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException, CDKException {
        ReadFileToString read = new ReadFileToString();
        FileReader r = new FileReader(fileF);
        BufferedReader breader = new BufferedReader(r);

        read.read(breader);

        build(read.outputString);
    }

    void build(String data) throws SQLException, CDKException, ClassNotFoundException, IOException {
        StringTokenizer tok = new StringTokenizer(data, "\n");
        while (tok.hasMoreTokens()) {
            if (tok.countTokens() >= 3) {
                parseLines(tok);
            }
        }
    }
/*
 * Line 1:   NameOfSymmetry  NameOfStructure SymmetryType
 *
 */
    private void parseLines(StringTokenizer tok) throws CDKException, SQLException, ClassNotFoundException, IOException {
        String symnumberS = null;
        String symmetryType;
        ListOfSymmetryPairs pairs = new ListOfSymmetryPairs();
        String nameOfSymmetry = null;
        StructureAsCML cmlstruct = null;
        //Line 1:   NameOfSymmetry  NameOfStructure SymmetryType
        String firstLine = tok.nextToken();
        StringTokenizer tokfirst = new StringTokenizer(firstLine);
        MetaAtomInfo metainfo = new MetaAtomInfo();
        if (tokfirst.countTokens() >= 3) {
            nameOfSymmetry = tokfirst.nextToken();
            String nameOfStructure = tokfirst.nextToken();
            symmetryType = tokfirst.nextToken();
            HashSet vec = sqlcmlstruct.retrieveStructuresFromDatabase(nameOfStructure);
            Iterator<StructureAsCML> iter = vec.iterator();
            cmlstruct = iter.next();

        } else {
            throw new CDKException("Expecting symmetry name and structure name but got '" + firstLine + "'");
        }
        System.out.println(tok.countTokens());
        //Each Line
        if (tok.countTokens() >= 2) {
            String group = tok.nextToken();
            System.out.println("Line: " + group);
            while (!group.startsWith(symmetryKeyWord)) {
                StringTokenizer grouptok = new StringTokenizer(group, ":");
                if (grouptok.countTokens() >= 3) {
                    String grpid = "Group" + grouptok.nextToken();
                    String atms = grouptok.nextToken();
                    String condition = grouptok.nextToken();
                    String[] grpids = findGroupIDs(atms);
                    for(int i=0;i<grpids.length;i++) {
                        SymmetryPair pair = new SymmetryPair(grpid, grpids[i],condition);
                        pairs.add(pair);
                    }
                } else {
                    throw new CDKException("Group assignments but got '" + group + "'");
                }
                group = tok.nextToken();
            }
            StringTokenizer symmtok = new StringTokenizer(group, ":");
            if(symmtok.countTokens() >= 2) {
                String symS = symmtok.nextToken();
                symnumberS = symmtok.nextToken();
            } else {
                throw new CDKException("Expecting 'Symmetry factor but got '" + group + "'");
            }
        }
        SymmetryDefinition symmdef = new SymmetryDefinition(nameOfSymmetry, cmlstruct, pairs, new Double(symnumberS));
        symmdef.setMetaAtomType(symmetryType);
        System.out.println(symmdef.toString());
        sqlSymmetryDefinition.deleteElement(symmdef);
        sqlSymmetryDefinition.addToDatabase(symmdef);
    }

    String[] findGroupIDs(String ids) {
        HashSet<String> IDs = new HashSet<String>();
        StringTokenizer tok = new StringTokenizer(ids);
        while(tok.hasMoreTokens()) {
            IDs.add(tok.nextToken());
        }
        String[] atmIDs = new String[IDs.size()];
        IDs.toArray(atmIDs);
        return atmIDs;
    }
}
