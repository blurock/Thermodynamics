/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.vibrational.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.vibrational.VibrationalStructureInfo;
import thermo.properties.SProperties;

/**
 *
 * @author blurock
 */
public class SQLVibrationalStructureInfo extends SQLStructureThermoAbstractInterface {

    public SQLVibrationalStructureInfo(ThermoSQLConnection connect) {
        super(connect);
        tableName = SProperties.getProperty("thermo.database.vibrationalstructure");
        tableKey = "ElementName";

    }

    @Override
    public String[] formulateInsert(Object structure) {
        VibrationalStructureInfo info = (VibrationalStructureInfo) structure;
        String[] commands = new String[1];

        String key = this.keyFromStructure(info);
        String command = "INSERT INTO VibrationalStructures (ElementName, StructureName, Frequency, Symmetry) "
                + "VALUES("
                + "\"" + info.getElementName() + "\", "
                + "\"" + info.getStructureName() + "\", "
                + info.getFrequency() + ", "
                + info.getSymmetry() + ");";
        commands[0] = command;

        return commands;

    }

    @Override
    public boolean query(Object structure) throws SQLException {
        VibrationalStructureInfo info = (VibrationalStructureInfo) structure;
        String sqlquery = "SELECT ElementName, StructureName, Frequency, Symmetry FROM VibrationalStructureInfo ";
        if (info.getElementName() != null) {
            sqlquery += " WHERE " + "ElementName=\"" + info.getElementName() + "\"" + ";";
        }
        //System.out.println(sqlquery);
        Statement statement = database.createStatement();
        ResultSet elements = statement.executeQuery(sqlquery);
        return elements.getFetchSize() > 0;
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
        Statement statement = database.createStatement();
        String sqlquery = "SELECT ElementName, StructureName, Frequency, Symmetry"
                + " FROM "
                + tableName 
                + " WHERE ElementName=" 
                + "\""  + name + "\";";
        ResultSet elements = statement.executeQuery(sqlquery);
        HashSet vec = transferAllElements(elements);
        return vec;
    }

    public HashSet transferAllElements(ResultSet elements) throws SQLException {
        HashSet vec = new HashSet();
        boolean next = elements.first();
        while (next) {
            VibrationalStructureInfo info = new VibrationalStructureInfo();
            info.setElementName(elements.getString("ElementName"));
            info.setStructureName(elements.getString("StructureName"));
            info.setFrequency(elements.getDouble("Frequency"));
            info.setSymmetry(elements.getDouble("Symmetry"));
            vec.add(info);
            next = elements.next();
        }
        return vec;
    }

    @Override
    public String keyFromStructure(Object structure) {
        VibrationalStructureInfo info = (VibrationalStructureInfo) structure;
        return info.getElementName();
    }
}
