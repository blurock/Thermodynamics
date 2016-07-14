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
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.AtomCount;
import thermo.data.structure.structure.AtomCounts;

/** Manage AtomCounts in SQL database
 *
 * {@link AtomCounts} is represented in the database as a vector
 * of {@link AtomCount} classes. This class manages this connection.
 *
 * @author edwardblurock
 */
public class SQLAtomCounts {
    ThermoSQLConnection connect;
    /**
     *
     * @param c
     */
    public SQLAtomCounts(ThermoSQLConnection c) {
        connect = c;
    }

    /** Add AtomCounts to database
     *
     * The structure information is added by repeated calls to
     * the {@link SQLAtomCount} class.
     *
     * @param counts The structure
     * @throws SQLException
     */
    public void addToDatabase(AtomCounts counts) throws SQLException {
       HashSet<AtomCount> vec = counts.vectorOfAtomCounts();
       Iterator<AtomCount> iter = vec.iterator();
       SQLAtomCount sqlcount = new SQLAtomCount(connect);
       while(iter.hasNext()) {
           sqlcount.addToDatabase(iter.next());
       }
   }
    public AtomCounts getAtomCounts(String name) throws SQLException {
        SQLAtomCount sqlcount = new SQLAtomCount(connect);
        HashSet vec = sqlcount.retrieveStructuresFromDatabase(name);
        AtomCounts counts = new AtomCounts();
        counts.setMoleculeID(name);
        Iterator<AtomCount> iter = vec.iterator();
        while(iter.hasNext()){
            AtomCount count = iter.next();
            Integer cntI = count.getAtomCount();
            counts.put(count.getSymbolName(),cntI);
        }
        return counts;
    }
    /** This is the heart of finding a matching {@link AtomCounts} from the AtomCounts database
     *
     * A query is set up for an element that matches all the atom symbol and corresponding counts of
     * {@link AtomCounts}. This match returns the first match, i.e.
     * assuming a unique match. {@link SQLGroupElement#queryNameSet(thermo.data.benson.BensonGroupStructure) }
     * is the routine that finds multiple matches.
     *
     * @param counts
     * @return The set of molecules (isomers) having
     * @throws java.sql.SQLException
     */
    public String[] findIsomersInDatabase(AtomCounts counts) throws SQLException {
        String[] names = findMoleculeWithCounts(counts);
        String[] filter = filterExactMatches(names,counts);
        return filter;
    }
    /** All structures in database with formula given in AtomCounts
     *
     * This search is for all structures that have atom counts
     * of matching those given in the input {@link AtomCounts}.
     * However, molecules having additional atoms are included.
     * If just isomers are wanted, and extra filtering
     * ({@link AtomCounts::filterExactMatches})
     *
     * @param counts The atom counts that should match
     * @return An array of molecule names matching atom counts
     * @throws SQLException
     */
    public String[] findMoleculeWithCounts(AtomCounts counts) throws SQLException {
        String[] isomers = null;
        boolean success = false;
        StringBuilder sqlqueryB = new StringBuilder();
        sqlqueryB.append("SELECT DISTINCT p1.Molecule");
        HashSet<AtomCount> vec = counts.vectorOfAtomCounts();
        sqlqueryB.append(" From ");
            for (int i = 0; i < vec.size(); i++) {
                if (i != 0) {
                    sqlqueryB.append(",");
                }
                String iS = String.valueOf(i + 1);
                sqlqueryB.append("AtomCounts as p" + iS + " ");
            }
            sqlqueryB.append(" WHERE ");
            Iterator<AtomCount> aciter = vec.iterator();
            int i= 0;
            while(aciter.hasNext()) {
            String iS = String.valueOf(i + 1);
            if(i>0) {
                sqlqueryB.append(" AND p1.Molecule=p" + iS + ".Molecule AND ");
            }
            AtomCount atomcount = aciter.next();
            sqlqueryB.append(" p" + iS + ".AtomCount = " + atomcount.getAtomCount());
            sqlqueryB.append(" AND p" + iS + ".AtomSymbol = \"" + atomcount.getSymbolName() + "\" ");
            i++;
        }
        sqlqueryB.append(";");
        System.out.println(sqlqueryB.toString());
        Statement statement = connect.createStatement();
        ResultSet set = statement.executeQuery(sqlqueryB.toString());
        HashSet<String> namesV = new HashSet();
        boolean next = set.first();
        while(next) {
            String name = set.getString("Molecule");
            System.out.println("Query name: '" + name + "'");
            namesV.add(name);
            next = set.next();
        }
        String[] names = new String[namesV.size()];
        Iterator<String> iter = namesV.iterator();
        i= 0;
        while(iter.hasNext()) {
            names[i++] = iter.next();
        }
        return names;
    }

    private String[] filterExactMatches(String[] names, AtomCounts counts) throws SQLException {
        Statement statement = connect.createStatement();
        HashSet<String> filtered = new HashSet<String>();
        HashSet<AtomCount> vec = counts.vectorOfAtomCounts();
        int sze = vec.size();
        for(int i=0;i<names.length;i++) {
            String sqlquery = "SELECT Molecule FROM AtomCounts WHERE Molecule=\"" + names[i] + "\";";
            ResultSet elements = statement.executeQuery(sqlquery);
            //elements.first();
            int count = 0;
            while(elements.next()) count++;
            if(count == sze) {
                filtered.add(names[i]);
            }
        }
        String[] f = new String[filtered.size()];
        f= filtered.toArray(f);
        return f;
    }
    /** Delete the structure from the database
     *
     * Corresponding elements of {@link AtomCount} through
     * {@link SQLAtomCount} are deleted.
     *
     * @param counts The AtomCounts to be deleted
     * @throws SQLException
     */
    public void deleteElement(AtomCounts counts) throws SQLException {
        Statement statement = connect.createStatement();
        HashSet<AtomCount> vec = counts.vectorOfAtomCounts();
        SQLAtomCount sqlcount = new SQLAtomCount(connect);
        Iterator<AtomCount> iter = vec.iterator();
        while(iter.hasNext()) {
           sqlcount.deleteElement(iter.next());
        }
    }
    public void deleteElement(String name) throws SQLException {
        Statement statement = connect.createStatement();
        String command = "DELETE FROM AtomCounts WHERE Molecule=\""
                + name +"\";";
        statement.execute(command);
    }
}
