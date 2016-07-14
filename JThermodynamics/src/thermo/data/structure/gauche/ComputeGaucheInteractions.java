/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.gauche;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.substructure.FindSubstructure;
import thermo.data.structure.substructure.ListOfStructureMatches;
import thermo.data.structure.substructure.StructureMatch;
import thermo.data.structure.substructure.ThermodyanmicsForSubstructures;

/**
 *
 * @author edwardblurock
 */
public class ComputeGaucheInteractions extends ThermodyanmicsForSubstructures {

    public ComputeGaucheInteractions(ThermoSQLConnection connection) throws SQLException, CDKException {
        super("StericCorrections", connection);
    }

    public void compute(AtomContainer molecule, SetOfBensonThermodynamicBase thermo) throws SQLException, CDKException {
        FindSubstructure findSubstructure = new FindSubstructure(molecule, connection);
        ListOfStructureMatches matches = findSubstructure.findNonoverlappingSubstructures(namesOfType);
        Iterator<StructureMatch> iter = matches.iterator();
        while (iter.hasNext()) {
            StructureMatch match = iter.next();
            String name = match.getNameOfStructure();
            HashSet<BensonThermodynamicBase> vec = thermodynamics.retrieveStructuresFromDatabase(name);
            Iterator<BensonThermodynamicBase> biter = vec.iterator();
            BensonThermodynamicBase data = biter.next();
            thermo.add(data);
        }
    }
}
