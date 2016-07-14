/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational.DB;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureType;
import thermo.data.structure.structure.StructureType;
import thermo.data.structure.structure.vibrational.SetOfVibrationalStructureInfo;
import thermo.properties.SProperties;

/**
 *
 * @author blurock
 */
public class SQLSetOfVibrationalStructureInfo {
    SQLVibrationalStructureInfo sqlinfo;
    SQLStructureType sqlStructureType;
    String nameOfVibrationalStructures;
    
    public SQLSetOfVibrationalStructureInfo(ThermoSQLConnection connect) {
        sqlinfo = new SQLVibrationalStructureInfo(connect);
        nameOfVibrationalStructures = SProperties.getProperty("thermo.database.vibrationalstructure");
    }
    
    public String[] getVibrationalStructures() throws SQLException {
        HashSet<String> namesV = new HashSet<String>();
        HashSet vibset = sqlStructureType.retrieveStructuresOfTypeFromDatabase(nameOfVibrationalStructures);
        Iterator vib = vibset.iterator();
        while(vib.hasNext()) {
            StructureType info = (StructureType) vib.next();
            namesV.add(info.getNameOfStructure());
        }
        return (String[]) namesV.toArray();
    }
    public SetOfVibrationalStructureInfo getSetOfVibrationalStructureInfo() throws SQLException {
        HashSet sqlset = sqlinfo.retrieveDatabase();
        SetOfVibrationalStructureInfo set = new SetOfVibrationalStructureInfo(sqlset);
        return set;
    }
}
