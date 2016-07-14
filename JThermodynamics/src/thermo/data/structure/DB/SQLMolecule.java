/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.AtomCounts;
import thermo.data.structure.structure.DB.SQLDatabaseMolecule;
import thermo.data.structure.structure.DatabaseMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.GetSubstructureMatches;

/**
 *
 * @author edwardblurock
 */
public class SQLMolecule {

    ThermoSQLConnection connect;

    public SQLMolecule(ThermoSQLConnection c) {
        connect = c;
    }
    public void addToDatabase(Object structure, String src) throws SQLException, CDKException {
        Molecule molecule = (Molecule) structure;
        String name = molecule.getID();
        deleteElement(name);
        AtomCounts counts = new AtomCounts(molecule);
        StructureAsCML cmlstruct = new StructureAsCML(molecule);
        DatabaseMolecule dmolecule =
                new DatabaseMolecule(molecule.getID(),
                                     cmlstruct.getCmlStructureString(),
                                     src);

        SQLAtomCounts sqlcounts = new SQLAtomCounts(connect);
        sqlcounts.addToDatabase(counts);

        SQLDatabaseMolecule sqldatamol = new SQLDatabaseMolecule(connect);
        sqldatamol.addToDatabase(dmolecule);
    }
    public void deleteElement(String name) throws SQLException {
        Statement statement = connect.createStatement();

        String atmcntS = "DELETE FROM AtomCounts WHERE Molecule=\"" + name + "\";";
        String databS  = "DELETE FROM DatabaseMolecule WHERE Molecule=\"" + name + "\";";

        statement.execute(atmcntS);
        statement.execute(databS);
    }

    StructureAsCML findMoleculeStructureInDatabase(String name) throws SQLException {
        StructureAsCML cmlstruct = null;
        SQLDatabaseMolecule sqldmol = new SQLDatabaseMolecule(connect);
        HashSet<DatabaseMolecule> mols = sqldmol.retrieveStructuresFromDatabase(name);
        Iterator<DatabaseMolecule> iter = mols.iterator();
        if (iter.hasNext()) {
            DatabaseMolecule dmol = iter.next();
            cmlstruct = new StructureAsCML(name,dmol.getCMLStructure());
        }
        return cmlstruct;
    }

    public String findInDatabase(Molecule molecule) throws SQLException, CDKException {
        SQLAtomCounts sqlcounts = new SQLAtomCounts(connect);

        AtomCounts counts = new AtomCounts(molecule);
        String[] names = sqlcounts.findIsomersInDatabase(counts);

        GetSubstructureMatches match = new GetSubstructureMatches();
        int i=-1;
        boolean notfound = true;
        while(notfound && i<names.length) {
            i++;
            StructureAsCML cmlstruct = findMoleculeStructureInDatabase(names[i]);
            if(cmlstruct != null) {
                Molecule dmolecule = cmlstruct.getMolecule();
                notfound = !match.equals(molecule,dmolecule);
            }
        }
        String name = null;
        if(!notfound) {
            name = names[i];
        }
        return name;
    }
    public String[] findMoleculesOfSource(String src) throws SQLException {
        Statement statement = connect.createStatement();
        String command = "SELECT Molecule FROM DatabaseMolecule WHERE Source=\""
                + src + "\";";
        ResultSet results = statement.executeQuery(command);
        HashSet<String> namesV = new HashSet<String>();
        while(results.next()) {
            namesV.add(results.getString("Molecule"));
        }
        String[] names = new String[namesV.size()];
        names = namesV.toArray(names);
        return names;
    }
    public void deleteFromSource(String src) throws SQLException {
        String[] molecules = findMoleculesOfSource(src);
        for(int i=0;i<molecules.length;i++) {
            deleteElement(molecules[i]);
        }
    }
}
