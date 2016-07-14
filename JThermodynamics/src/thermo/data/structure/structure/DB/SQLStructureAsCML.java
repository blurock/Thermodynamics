/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author edwardblurock
 */
public class SQLStructureAsCML extends SQLStructureThermoAbstractInterface {

    public SQLStructureAsCML(ThermoSQLConnection connect) {
        super(connect);
    }

    @Override
    public String[] formulateInsert(Object structure) {
        StructureAsCML cmlstruct = (StructureAsCML) structure;
        String command = "INSERT INTO CMLStructures (ElementName CMLStructure,Source) "
                + "VALUES("
                + "\"" + cmlstruct.getNameOfStructure() + "\", "
                + "\"" + cmlstruct.getCmlStructureString() + "\","
                + "\"" + cmlstruct.getSource() + "\"";

        String commands[] = new String[1];
        commands[0] = command;
        return commands;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        StructureAsCML cmlstruct = (StructureAsCML) structure;
        return false;
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
        String command = "SELECT * FROM CMLStructures WHERE ElementName=\""
                + name + "\"";
        System.out.println(command);
        Statement statement = database.createStatement();
        ResultSet set = statement.executeQuery(command);
        boolean ans = set.first();
        HashSet<StructureAsCML> vec = new HashSet<StructureAsCML>();
        if(ans) {
            String elementName = set.getString("ElementName");
            String cml = set.getString("CMLStructure");
            String source = set.getString("Source");
            StructureAsCML cmlstruct = new StructureAsCML(elementName, cml,source);
            vec.add(cmlstruct);
        }
        return vec;
    }

    @Override
    public String keyFromStructure(Object structure) {
        StructureAsCML cmlstruct = (StructureAsCML) structure;
        return cmlstruct.getNameOfStructure();
    }

}
