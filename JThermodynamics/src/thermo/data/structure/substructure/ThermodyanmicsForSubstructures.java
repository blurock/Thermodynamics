/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.substructure;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.SQLBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.structure.DB.SQLStructureType;
import thermo.data.structure.structure.StructureType;

/** ThermodyanmicsForSubstructures
 *
 *
 *
 * @author edwardblurock
 */
public class ThermodyanmicsForSubstructures {
    protected ThermoSQLConnection connection;
    private SQLStructureType sqlStructureType;
    protected SQLBensonThermodynamicBase thermodynamics;
    String elementName;
    protected List<String> namesOfType;

    /**
     *
     * @param type
     * @param connect
     * @throws SQLException
     * @throws CDKException
     */
    public ThermodyanmicsForSubstructures(String type, ThermoSQLConnection connect) throws SQLException, CDKException {
        connection = connect;
        sqlStructureType = new SQLStructureType(connection);
        thermodynamics = new SQLBensonThermodynamicBase(connection);
        HashSet<StructureType> names = sqlStructureType.retrieveStructuresOfTypeFromDatabase(type);
        namesOfType = new ArrayList<String>();
        Iterator<StructureType> iter = names.iterator();
        while(iter.hasNext()) {
            StructureType structuretype = iter.next();
            namesOfType.add(structuretype.getNameOfStructure());
        }
    }
    /**
     *
     * @param molecule The
     * @return
     * @throws SQLException
     * @throws CDKException
     */
    public ThermodynamicInformation FindLargestStructureThermodynamics(AtomContainer molecule) throws SQLException, CDKException {
        FindSubstructure findSubstructure = new FindSubstructure(molecule,connection);
        elementName = findSubstructure.findLargestSubstructure(namesOfType);
        ThermodynamicInformation thermo = null;
        if(elementName != null) {
            HashSet vec = thermodynamics.retrieveStructuresFromDatabase(elementName);
            Iterator<ThermodynamicInformation> iter = vec.iterator();
            thermo = iter.next();
        }
        return thermo;
    }
}
