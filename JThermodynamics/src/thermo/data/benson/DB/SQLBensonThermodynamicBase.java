/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.HeatCapacityTemperaturePair;

/**
 *
 * @author blurock
 */
public class SQLBensonThermodynamicBase extends SQLStructureThermoAbstractInterface {

    public SQLBensonThermodynamicBase(ThermoSQLConnection connect) {
        super(connect);
    }

    @Override
    public void initializeStructureInDatabase() throws SQLException {
        Statement statement = database.createStatement();
        statement.execute("DELETE FROM GroupStandardEntropy;");
        statement.execute("DELETE FROM GroupStandardEnthalpy;");
        statement.execute("DELETE FROM HeatCapacityElement;");
    }
    public void initializeStructureInDatabase(String reference) throws SQLException {
        Statement statement = database.createStatement();
        statement.execute("DELETE FROM GroupStandardEntropy WHERE Reference = \"" + reference + "\";");
        statement.execute("DELETE FROM GroupStandardEnthalpy WHERE Reference = \"" + reference + "\";");
        statement.execute("DELETE FROM HeatCapacityElement WHERE Reference = \"" + reference + "\";");
    }

    @Override
    public String[] formulateInsert(Object structure) {
        BensonThermodynamicBase thermo = (BensonThermodynamicBase) structure;
        Double enthalpy = thermo.getStandardEnthalpy();
        Double entropy = thermo.getStandardEntropy();
        HashSet<HeatCapacityTemperaturePair> cps = thermo.getSetOfHeatCapacities();

        HashSet<String> commandsV = new HashSet<String>();
        String sqlenthalpy = "INSERT INTO GroupStandardEnthalpy (ElementName, StandardEnthalpy, Reference) " + "Values(" + "\"" + thermo.getID() + "\"," + enthalpy.toString() + "," + "\"" + thermo.getReference() + "\"" + ");";

        String sqlentropy = "INSERT INTO GroupStandardEntropy (ElementName, StandardEntropy, Reference) " + "Values(" + "\"" + thermo.getID() + "\"," + thermo.getStandardEntropy().toString() + "," + "\"" + thermo.getReference() + "\"" + ");";
        commandsV.add(sqlenthalpy);
        commandsV.add(sqlentropy);
        SQLHeatCapacityTemperaturePair sqlheat = new SQLHeatCapacityTemperaturePair(database);
        Iterator<HeatCapacityTemperaturePair> iter = cps.iterator();
        while(iter.hasNext()){
            HeatCapacityTemperaturePair pair = iter.next();
             String[] names = sqlheat.formulateInsert(pair);
            commandsV.addAll(Arrays.asList(names));
       }
         String[] commands = new String[commandsV.size()];
         Iterator<String> iterstr = commandsV.iterator();
         int i=0;
         while(iterstr.hasNext()){
             commands[i] = iterstr.next();
             
         }
        return commands;
    }

    @Override
    public void deleteElement(Object structure) throws SQLException {
        BensonThermodynamicBase thermo = (BensonThermodynamicBase) structure;
        Statement statement = database.createStatement();
        String elementname = thermo.getID();
        String entropyC = "DELETE FROM GroupStandardEntropy WHERE ElementName=\""
                + elementname + "\";";
        String ethalpyC = "DELETE FROM GroupStandardEnthalpy WHERE ElementName=\""
                + elementname + "\";";
        String cpC = "DELETE FROM HeatCapacityElement WHERE ElementName=\""
                + elementname + "\";";
        statement.execute(cpC);
        statement.execute(ethalpyC);
        statement.execute(entropyC);
    }
    @Override
    public boolean query(Object structure) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
        BensonThermodynamicBase grp = new BensonThermodynamicBase();
        grp.setID(name);

        Statement statement = database.createStatement();

        String sqlenthalpy = "SELECT ElementName, StandardEnthalpy, Reference From GroupStandardEnthalpy WHERE ElementName=\"" + name + "\";";
        System.out.println(sqlenthalpy);
        ResultSet enthalpyresults = statement.executeQuery(sqlenthalpy);
        boolean nextenthalpy = enthalpyresults.first();
        grp.setStandardEnthalpy(new Double(enthalpyresults.getDouble("StandardEnthalpy")));
        grp.setReference(enthalpyresults.getString("Reference"));
        
        String sqlentropy = "SELECT ElementName, StandardEntropy, Reference From GroupStandardEntropy WHERE ElementName=\"" + name + "\";";
        //System.out.println(sqlentropy);
        ResultSet entropyresults = statement.executeQuery(sqlentropy);
        boolean nextentropy = entropyresults.first();
        grp.setStandardEntropy(new Double(entropyresults.getDouble("StandardEntropy")));
        
        
        SQLHeatCapacityTemperaturePair cps = new SQLHeatCapacityTemperaturePair(database);
        HashSet vec = cps.retrieveStructuresFromDatabase(name);
        Object[] objs = vec.toArray();
        Arrays.sort(objs);
        HashSet sorted = new HashSet();
        sorted.addAll(Arrays.asList(objs));
        grp.setSetOfHeatCapacities(sorted);
        HashSet ans = new HashSet(1);
        ans.add(grp);
        return ans;

    }

    @Override
    public String keyFromStructure(Object structure) {
        BensonThermodynamicBase grp = new BensonThermodynamicBase();
        return grp.getID();
    }
    public HashSet<String> findElementsOfReference(String reference) throws SQLException {
        Statement statement = database.createStatement();
        String command = "Select ElementName FROM GroupStandardEntropy WHERE Reference=\""
                + reference + "\";";
        ResultSet results = statement.executeQuery(command);
        HashSet<String> names = new HashSet<String>();
        while(results.next()) {
            String name = results.getString("ElementName");
            names.add(name);
        }
        return names;
    }
    public void deleteSourceFromDatabase(String molecule) throws SQLException {
        Statement statement = database.createStatement();
        String entropyC = "DELETE FROM GroupStandardEntropy WHERE ElementName=\""
                + molecule + "\";";
        String ethalpyC = "DELETE FROM GroupStandardEnthalpy WHERE ElementName=\""
                + molecule + "\";";
        String cpC = "DELETE FROM HeatCapacityElement WHERE ElementName=\""
                + molecule + "\";";
        statement.execute(cpC);
        statement.execute(ethalpyC);
        statement.execute(entropyC);
    }

}
