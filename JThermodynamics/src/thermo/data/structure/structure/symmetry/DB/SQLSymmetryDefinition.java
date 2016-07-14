/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.symmetry.DB;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.ListOfSymmetryPairs;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;
import thermo.data.structure.structure.symmetry.SymmetryPair;

/**
 *
 * @author blurock
 */
public class SQLSymmetryDefinition extends SQLStructureThermoAbstractInterface {

    public SQLSymmetryDefinition(ThermoSQLConnection connect) {
        super(connect);
        tableName = "SymmetryDefinition";
        tableKey = "SymmetryName";

    }

    @Override
    public String[] formulateInsert(Object structure) {
        SymmetryDefinition definition = (SymmetryDefinition) structure;

        //SQLMetaAtomInfo sqlmeta = new SQLMetaAtomInfo(database);
        //String[] metainserts = sqlmeta.formulateInsert(structure);

        List<SymmetryPair> pairs = definition.extractListOfSymmetryPairs();
        /*
        String[] inserts = new String[pairs.size() + metainserts.length];
        for(int i=0;i<metainserts.length;i++) {
        inserts[i] = metainserts[i];
        }
        int count = metainserts.length;
         */
        String command = "INSERT INTO SymmetryDefinition (SymmetryName, ElementName, SymmetryType, SymmetryFactor)"
                + "VALUES(" + "\""
                + definition.getMetaAtomName() + "\","
                + "\"" + definition.getElementName() + "\","
                + "\"" + definition.getMetaAtomType() + "\","
                + definition.getInternalSymmetryFactor().toString()
                + ");";

        String[] inserts = new String[pairs.size() + 1];
        inserts[0] = command;
        int count = 1;
        Iterator<SymmetryPair> ipair = pairs.iterator();
        while (ipair.hasNext()) {
            SymmetryPair pair = ipair.next();
            inserts[count] = "INSERT INTO SymmetryPairAssignments (SymmetryName, GroupName, AtomId, ConnectedSymmetry) "
                    + "VALUES(" + "\"" + definition.getMetaAtomName() +
                    "\"," + "\"" + pair.getGroupName()
                    + "\"," + "\"" + pair.getStructureName() + "\","
                    + "\"" + pair.getConnectedSymmetry() + "\");";

            //System.out.println(inserts[count]);
            count++;
        }
        return inserts;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
        HashSet vec = new HashSet();
        try {
            Statement statement = database.createStatement();
            ListOfSymmetryPairs pairs = getSymmetryPairs(name);

            String type = getSymmetryTypeFromName(name);
            StructureAsCML cmlstruct = getSymmetryElement(name);
            Double factor = getSymmetryFactorFromName(name);
            SymmetryDefinition definition = null;
            definition = new SymmetryDefinition(name, cmlstruct, pairs,factor);
            definition.setMetaAtomType(type);
            vec = new HashSet(1);
            vec.add(definition);
        } catch (CDKException ex) {
            throw new SQLException("CDK Error in creating Structure:\n" + ex.toString());
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Class Error in creating Structure:\n" + ex.toString());
        } catch (IOException ex) {
            throw new SQLException("IO Error in creating Structure:\n" + ex.toString());
        }
        return vec;
    }

    @Override
    public String keyFromStructure(Object structure) {
        SymmetryDefinition def = (SymmetryDefinition) structure;
        return def.getMetaAtomName();
    }

    @Override
    public void initializeStructureInDatabase() throws SQLException {
        Statement statement = database.createStatement();
        statement.execute("DELETE FROM SymmetryDefinition;");
        statement.execute("DELETE FROM SymmetryPairAssignments;");
    }

    @Override
    public void deleteByKey(String keyname) throws SQLException {
        Statement statement = database.createStatement();
        String sqlcommand1 = "DELETE FROM SymmetryDefinition WHERE SymmetryName = \"" + keyname + "\";";
        String sqlcommand2 = "DELETE FROM SymmetryPairAssignments WHERE SymmetryName = \"" + keyname + "\";";
        statement.execute(sqlcommand1);
        statement.execute(sqlcommand2);
    }
    /* Delete symmetry structure my the symmetry name.
     *
     * This is just a direct call to the overloaded operator 'deleteByKey'
     * since the key is the symmetry name.
     *
     */
    public void deleteBySymmetryName(String name) throws SQLException {
        deleteByKey(name);
    }
    public String[] retrieveSymmetryNamesOfTypeFromDatabase(String type) throws SQLException {
        Statement statement = database.createStatement();
        String sqlquery = "SELECT SymmetryName  FROM SymmetryDefinition WHERE SymmetryType=\"" + type + "\";";
        //System.out.println(sqlquery);
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean hasmore = elements.first();
        HashSet<String> lst = new HashSet<String>();
        while (hasmore) {
            String name = elements.getString("SymmetryName");
            lst.add(name);
            hasmore = elements.next();
        }
        String[] names = new String[lst.size()];
        lst.toArray(names);
        return names;
    }

    public ListOfSymmetryPairs getSymmetryPairs(String name) throws SQLException {
        Statement statement = database.createStatement();
        String sqlquery = "SELECT GroupName, AtomId, ConnectedSymmetry FROM SymmetryPairAssignments WHERE SymmetryName=\"" + name + "\";";
        //System.out.println(sqlquery);
        ResultSet elements = statement.executeQuery(sqlquery);
        ListOfSymmetryPairs pairs = new ListOfSymmetryPairs();
        boolean haselements = elements.first();
        while (haselements) {
            String group = elements.getString("GroupName");
            String atomid = elements.getString("AtomId");
            String connected = elements.getString("ConnectedSymmetry");
            SymmetryPair pair = new SymmetryPair(group, atomid,connected);
            pairs.add(pair);
            haselements = elements.next();
        }
        return pairs;
    }

    public String getSymmetryTypeFromName(String name) throws SQLException {
        Statement statement = database.createStatement();
        String mainquery = "SELECT SymmetryType FROM SymmetryDefinition WHERE SymmetryName=" + "\"" + name + "\";";
        ResultSet elements = statement.executeQuery(mainquery);
        elements.first();
        return elements.getString("SymmetryType");
    }
    public Double getSymmetryFactorFromName(String name) throws SQLException {
        Statement statement = database.createStatement();
        String mainquery = "SELECT SymmetryFactor FROM SymmetryDefinition WHERE SymmetryName=" + "\"" + name + "\";";
        ResultSet elements = statement.executeQuery(mainquery);
        elements.first();
        return new Double(elements.getString("SymmetryFactor"));
    }

    public String getSymmetryElementName(String name) throws SQLException {
        Statement statement = database.createStatement();
        String mainquery = "SELECT ElementName FROM SymmetryDefinition WHERE SymmetryName=" + "\"" + name + "\";";
        ResultSet elements = statement.executeQuery(mainquery);
         elements.first();
       return elements.getString("ElementName");
    }

    public StructureAsCML getSymmetryElement(String name) throws SQLException {
        String elementname = getSymmetryElementName(name);
        SQLStructureAsCML sqlstructure = new SQLStructureAsCML(database);
        Iterator<StructureAsCML> iter = sqlstructure.retrieveStructuresFromDatabase(elementname).iterator();
        StructureAsCML cmlstruct = iter.next();
        return cmlstruct;
    }
    public void insertSymmetryDefintion(String symmetryname, String elementname, String type,
            String connectedSymmetry, ListOfSymmetryPairs pairs) {
        try {
            Statement statement = database.createStatement();
            String command = "INSERT INTO SymmetryDefinition (SymmetryName, ElementName, SymmetryType ConnectedSymmery)"
                    + "VALUES("
                    + "\"" + symmetryname + "\","
                    + "\"" + elementname + "\","
                    + "\"" + type + "\","
                    + "\"" + connectedSymmetry + ");";
            statement.execute(command);
            insertSymmetryPairs(symmetryname, pairs);

        } catch (SQLException ex) {
            Logger.getLogger(SQLSymmetryDefinition.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void insertSymmetryDefintion(String symmetryname, String elementname, String type, ListOfSymmetryPairs pairs, Double factor) {
        try {
            Statement statement = database.createStatement();
            String command = "INSERT INTO SymmetryDefinition (SymmetryName, ElementName, SymmetryType, SymmetryFactor)"
                    + "VALUES("
                    + "\"" + symmetryname + "\","
                    + "\"" + elementname + "\","
                    + "\"" + type + "\","
                    + factor.toString()
                    + ");";
            statement.execute(command);
            insertSymmetryPairs(symmetryname, pairs);

        } catch (SQLException ex) {
            Logger.getLogger(SQLSymmetryDefinition.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected void insertSymmetryPairs(String symmetryname, ListOfSymmetryPairs pairs) throws SQLException {
        Statement statement = database.createStatement();
       Iterator<SymmetryPair> ipair = pairs.iterator();
        while (ipair.hasNext()) {
            SymmetryPair pair = ipair.next();
            String command = "INSERT INTO SymmetryPairAssignments (SymmetryName, GroupName, AtomId, ConnectedSymmetry) " + "VALUES(" 
                + "\"" + symmetryname + "\"," 
                    + "\"" + pair.getGroupName() + "\"," 
                    + "\"" + pair.getStructureName() + "\""
                    + "\"" + pair.getConnectedSymmetry() + "\""+ ");";
            statement.execute(command);
        }
    }
}
