/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.substructure.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.substructure.SubStructure;

/**
 *
 * @author edwardblurock
 */
public class SQLSubStructure extends SQLStructureThermoAbstractInterface {

    public SQLSubStructure(ThermoSQLConnection connect) {
        super(connect);
        tableName = "CMLStructures";
        tableKey = "ElementName";

    }

    @Override
    public String[] formulateInsert(Object structure) {
        SubStructure substructure = (SubStructure) structure;
        String[] command = null;
        try {
            command = new String[1];
            StructureAsCML cmlstruct = new StructureAsCML((AtomContainer) substructure.getSubstructure());
        
        SQLStructureAsCML sqlcml = new SQLStructureAsCML(null);
        String cmlmolecule = sqlcml.cmlStructureStringToSQLString(cmlstruct.getCmlStructureString());
        command[0] = "INSERT INTO CMLStructures (ElementName, CMLStructure, Source) "
                + "VALUES("
                + "\"" + substructure.getSubstructure().getID() + "\", "
                + "\"" + cmlmolecule + "\", "
                + "\"" + substructure.getSourceOfStructure() + "\");";

        } catch (CDKException ex) {
            Logger.getLogger(SQLSubStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return command;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
        Statement statement = database.createStatement();
        String sqlquery = "SELECT ElementName, CMLStructure, Source FROM CMLStructures WHERE ElementName=\"" + name + "\"";
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean next = elements.first();
        HashSet vec = null;
        if (next) {
            try {
                vec = new HashSet(1);
                String cmlstructS = elements.getString("CMLStructure");
                String molname = elements.getString("ElementName");
                String src = elements.getString("Source");
                StructureAsCML cmlstruct = new StructureAsCML(molname, cmlstructS);
                AtomContainer molecule = cmlstruct.getMolecule();
                molecule.setID(molname);
                SubStructure substructure = new SubStructure(molecule, src);
                vec.add(substructure);
            } catch (CDKException ex) {
                Logger.getLogger(SQLSubStructure.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            vec = new HashSet();
        }
        return vec;

    }

    @Override
    public String keyFromStructure(Object structure) {
        SubStructure substructure = (SubStructure) structure;
        return substructure.getSubstructure().getID();
    }

}
