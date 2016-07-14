/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.linearform;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.DB.SQLMetaAtomInfo;
import thermo.data.structure.structure.MetaAtomInfo;

/**
 *
 * @author blurock
 */
public class CorrectLinearForm {

    SQLMetaAtomInfo sqlMetaInfo;
    private ThermoSQLConnection connect;
    String nancyLinearFormS = "NancyLinearForm";
    HashSet<String> groupNames = null;
    private int maxNumberOfCharactersForGroupName = 7;

    /** The constructor sets up the set of meta atom names used in the Nancy Linear Form
     * 
     * The {@link SQLMetaAtomInfo} class is used to get the set of meta atoms
     * that are used in the Nancy Linear Form.
     * These are then put into a vector of strings {@link groupNames}
     * 
     * @param c
     * @throws java.sql.SQLException
     */
    public CorrectLinearForm(ThermoSQLConnection c) throws SQLException {
        connect = c;
        sqlMetaInfo = new SQLMetaAtomInfo(connect);

        HashSet ans = sqlMetaInfo.retrieveMetaAtomTypesFromDatabase(nancyLinearFormS);
        groupNames = new HashSet<String>();
        for (int sze = maxNumberOfCharactersForGroupName; sze > 0 ; sze--) {
            Iterator info = ans.iterator();
            while (info.hasNext()) {
                MetaAtomInfo metainfo = (MetaAtomInfo) info.next();
                String name = metainfo.getMetaAtomName();
                if (name.length() == sze) {
                    if (name.startsWith("'")) {
                        //System.out.println("Quoted: " + name);
                        name = name.substring(1, name.length() - 1);
                        //System.out.println("Result: " + name);
                    }
                    groupNames.add(name);
                }
            }
        }
    }

    public String correctNancyLinearForm(String original) {
        boolean parens = false;
        int i = 0;
        while (i < original.length()) {
            //System.out.println("-->" + original + "<--\t-->" + original.substring(i) + "<--");
            if (parens) {
                if (original.startsWith("'")) {
                    parens = false;
                }
                i++;
            } else if (original.startsWith("'")) {
                parens = true;
                i++;
            } else {
                Iterator<String> info = groupNames.iterator();
                boolean notfound = true;
                while (info.hasNext() && notfound) {
                    String name = info.next();
                    if (original.startsWith(name, i)) {
                        String front = original.substring(0,i) + "'" + name + "'";
                        String back = original.substring(i+name.length());
                        System.out.println("front:" + front + " back:" + back + " name:" + name + "'");
                        i = front.length();
                        original = front + back;
                        notfound = false;
                    }
                }
                i++;
            }
        }
        return original;
    }
    public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator<String> info = groupNames.iterator();
        buf.append("Linear Form Group Names: ");
        while(info.hasNext()) {
            buf.append(info.next());
            buf.append("\t");
        }
        return buf.toString();
    }
}
