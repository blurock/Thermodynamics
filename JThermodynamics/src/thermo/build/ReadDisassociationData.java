/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.build;

import jThergas.data.read.ReadFileToString;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLAtomCounts;
import thermo.data.structure.disassociation.DB.SQLDisassociationEnergy;
import thermo.data.structure.disassociation.ThergasDisassociationEnergyInfo;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.AtomCounts;
import thermo.data.structure.structure.DB.SQLSubstituteBackMetaAtomIntoMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.substructure.SetOfSubStructures;
import thermo.data.structure.substructure.SubStructure;
import thermo.exception.ThermodynamicException;

/**
 *
 * @author edwardblurock
 */
public class ReadDisassociationData {
    ThermoSQLConnection connection;
    ReadFileToString readFileToString;
    SQLSubstituteBackMetaAtomIntoMolecule substitute;
    String commentPrefixS = "c";
    String secondLineStartS = "l";
    String nancyLinearFormType = "NancyLinearForm";

    String referenceSourceS;
    SQLDisassociationEnergy sqlDisassocationEnergy;
    SQLAtomCounts sqlAtomCounts;

    public ReadDisassociationData(ThermoSQLConnection connection) throws SQLException, CDKException, ClassNotFoundException, IOException {
        this.connection = connection;
        readFileToString = new ReadFileToString();
        sqlDisassocationEnergy = new SQLDisassociationEnergy(connection);
        sqlAtomCounts = new SQLAtomCounts(connection);
        substitute = new SQLSubstituteBackMetaAtomIntoMolecule(nancyLinearFormType, connection);
    }

    public void build(File f, String source) throws ThermodynamicException, SQLException {
        SetOfSubStructures set = this.read(f, source);
        System.out.println(set.toString());
        Iterator<SubStructure> iter =  set.iterator();
        while(iter.hasNext()){
            ThergasDisassociationEnergyInfo info = (ThergasDisassociationEnergyInfo) iter.next();
            storDissassociationEnergy(info);
        }
    }
    public SetOfSubStructures read(File f, String source) {
        SetOfSubStructures set = new SetOfSubStructures();
        referenceSourceS = source;
        readFileToString.read(f);
        StringTokenizer lines = new StringTokenizer(readFileToString.outputString,"\n");
        while(lines.hasMoreTokens()) {
            try {
            ThergasDisassociationEnergyInfo info = parseBlock(lines);
            set.add(info);
            } catch(SQLException ex) {
                System.err.println(ex.toString());
            } catch(ThermodynamicException ex) {
                System.err.println(ex.toString());
            }
        }
        return set;
    }

    private ThergasDisassociationEnergyInfo parseBlock(StringTokenizer lines) throws ThermodynamicException, SQLException {
        ThergasDisassociationEnergyInfo info = null;
        String[] block = getNextBlock(lines);
        StringTokenizer tok1 = new StringTokenizer(block[0]);
        if(tok1.countTokens() == 5) {
            StringTokenizer tok2 = new StringTokenizer(block[1]);
            if(tok2.countTokens() == 2) {
                String label1  = tok1.nextToken();
                String label2  = tok1.nextToken();
                String energyS = tok1.nextToken();
                String errorS  = tok1.nextToken();
                String nameS   = tok1.nextToken();
                
                
                String startS  = tok2.nextToken();
                String nancy   = tok2.nextToken();
                System.out.println("Form Structure: " + nameS + "\t" + nancy );

                NancyLinearFormToMolecule nancyform = new NancyLinearFormToMolecule(connection);
                AtomContainer molecule = nancyform.convert(nancy);
                substitute.substitute(molecule);
                molecule.setID(nameS);
                Double energyD = new Double(energyS);
                Double errorD = new Double(errorS);
                try {
                    StructureAsCML cml = new StructureAsCML(molecule);
                    System.out.println(cml.getCmlStructureString());
                } catch (CDKException ex) {
                    Logger.getLogger(ReadDisassociationData.class.getName()).log(Level.SEVERE, null, ex);
                }
                info = new ThergasDisassociationEnergyInfo(molecule,referenceSourceS,energyD,errorD,label1,label2);
                } else {
                throw new ThermodynamicException("Second line of block ill formed: " + block[1]);
            }
        } else {
            throw new ThermodynamicException("First line of block ill formed: " + block[0]);
        }
        return info;
    }

    private String[] getNextBlock(StringTokenizer lines) throws ThermodynamicException {
        String[] block = new String[2];
        block[0] = getNextNonComment(lines);
        block[1] = getNextNonComment(lines);
        if(!block[1].startsWith(secondLineStartS)) {
            throw new ThermodynamicException("Error in reading file: '" + block[1] + "'");
        }
        return block;
    }

    private String getNextNonComment(StringTokenizer lines) throws ThermodynamicException {
        String line = null;
        try {
            line = lines.nextToken();
            while(line.startsWith(commentPrefixS)) {
                line = lines.nextToken();
            }
        } catch(NoSuchElementException ex) {
            throw new ThermodynamicException("Incomplete Block at end of file");
        }
        return line;
    }

    private void storDissassociationEnergy(ThergasDisassociationEnergyInfo info) throws SQLException {
        AtomContainer substructure = (AtomContainer) info.getSubstructure();
        AtomCounts counts = new AtomCounts(substructure);
        sqlDisassocationEnergy.deleteElement(info);
        sqlDisassocationEnergy.addToDatabase(info);
        sqlAtomCounts.deleteElement(counts.moleculeID);
        sqlAtomCounts.addToDatabase(counts);
    }
}
