/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.disassociation.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.AtomContainer;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.disassociation.DisassociationEnergy;
import thermo.data.structure.substructure.DB.SQLSubStructure;
import thermo.data.structure.substructure.SubStructure;

/**
 *
 * @author edwardblurock
 */
public class SQLDisassociationEnergy extends SQLStructureThermoAbstractInterface {
    SQLSubStructure sqlSubStructure;
    public SQLDisassociationEnergy(ThermoSQLConnection connect) {
        super(connect);
        sqlSubStructure = new SQLSubStructure(connect);
        tableName = "DisassociationEnergy";
        tableKey = "ElementName";
    }


   public void deleteElement(Object structure) throws SQLException {
       super.deleteElement(structure);
       sqlSubStructure.deleteElement(structure);
    }
    @Override
    public String[] formulateInsert(Object structure) {
        DisassociationEnergy energy = (DisassociationEnergy) structure;
        String[] subcommands = sqlSubStructure.formulateInsert(structure);

        String[] commands = new String[subcommands.length + 1];
        for(int i=0;i<subcommands.length;i++) {
            commands[i] = subcommands[i];
        }
        String command = "INSERT INTO DisassociationEnergy "
                + "(DisassociationEnergy, Error, ElementName, Source) "
                + "Values("
                + energy.getDisassociationEnergy() +","
                + energy.getErrorInEnergy() + ","
                + "\"" + energy.getSubstructure().getID() + "\","
                + "\"" + energy.getSourceOfStructure() + "\""
                + ")";
        commands[subcommands.length] = command;
        return commands;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
        Statement statement = database.createStatement();
        String sqlquery = "SELECT DisassociationEnergy, Error, ElementName, Source FROM DisassociationEnergy WHERE ElementName=\"" + name + "\"";
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean next = elements.first();
        HashSet vec = null;
        if (next) {
                vec = new HashSet(1);
                double disassociationE = elements.getDouble("DisassociationEnergy");
                double error = elements.getDouble("Error");
                String src = elements.getString("Source");
                HashSet structureV = sqlSubStructure.retrieveStructuresFromDatabase(name);
                if(structureV.size() != 1) {
                    throw new SQLException("For " + name + " Found " + structureV.size() + "structures expected one");
                }
                Iterator<SubStructure> iter = structureV.iterator();
                SubStructure sub = iter.next();
                DisassociationEnergy energy = new DisassociationEnergy((AtomContainer) sub.getSubstructure(),
                        src,disassociationE,error);
                vec.add(energy);
        } else {
            vec = new HashSet();
        }
        return vec;


    }

    @Override
    public String keyFromStructure(Object structure) {
        DisassociationEnergy energy = (DisassociationEnergy) structure;
        return energy.getSubstructure().getID();
    }

    public List<String> listOfDisassociationStructures() throws SQLException {
       Statement statement = database.createStatement();
       List<String> names = new ArrayList<String>();
       String sqlquery = "SELECT ElementName FROM DisassociationEnergy";
       ResultSet set = statement.executeQuery(sqlquery);
       while(set.next()) {
            String name = set.getString("ElementName");
            names.add(name);
       }
       return names;
    }
}
