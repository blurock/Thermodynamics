/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.DB;

import java.io.IOException;
import java.sql.SQLException;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.SubstituteBackMetaAtomsIntoMolecule;
import thermo.data.benson.DB.ThermoSQLConnection;

/** SubstituteBackMetaAtomsIntoMolecule with SQL source of meta atoms
 *
 * In the constructor, the meta atoms, from a given type,
 * are read in are the basis of the substitutions.
 *
 * @author reaction
 */

public class SQLSubstituteBackMetaAtomIntoMolecule extends SubstituteBackMetaAtomsIntoMolecule {

    /** Constructor
     *
     * This constructor sets up the meta atoms from the SQL database
     *
     * @param metaAtomType The name of the meta atoms to substitute
     * @param connect The connection to the SQL database
     * @throws java.sql.SQLException
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public SQLSubstituteBackMetaAtomIntoMolecule(String metaAtomType,ThermoSQLConnection connect) throws SQLException, CDKException, ClassNotFoundException, IOException {
        SQLMetaAtomDefinitionFromMetaAtomInfo sqlsource = new SQLMetaAtomDefinitionFromMetaAtomInfo(connect);
        sqlsource.createSubstitutionBackSets(metaAtomType,this);
    }

}
