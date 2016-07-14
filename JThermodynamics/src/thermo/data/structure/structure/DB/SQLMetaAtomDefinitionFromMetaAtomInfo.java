/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.DB;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.SubstituteBackMetaAtomsIntoMolecule;

/**
 *
 * @author blurock
 */
public class SQLMetaAtomDefinitionFromMetaAtomInfo  {
    
    ThermoSQLConnection SQLConnection;
    SQLMetaAtomInfo sqlMetaAtomInfo;
    SQLStructureAsCML sqlStructureAsCML;
    
    public SQLMetaAtomDefinitionFromMetaAtomInfo(ThermoSQLConnection connect) {
        SQLConnection = connect;
        sqlMetaAtomInfo = new SQLMetaAtomInfo(connect);
        sqlStructureAsCML = new SQLStructureAsCML(connect);
    }
    
    public MetaAtomDefinition createMetaAtomDefinition(MetaAtomInfo info) throws SQLException, CDKException, ClassNotFoundException, IOException {
        HashSet vecstruct = sqlStructureAsCML.retrieveStructuresFromDatabase(info.getElementName());
        Iterator<StructureAsCML> iter = vecstruct.iterator();
        StructureAsCML cmlstruct = iter.next();
        
        MetaAtomDefinition meta = new MetaAtomDefinition(info, cmlstruct);
        return meta;
    }
    
    public SetOfMetaAtomsForSubstitution createSubstitutionSets(String metaAtomType) throws SQLException, CDKException, ClassNotFoundException, IOException {
        SetOfMetaAtomsForSubstitution subs = new SetOfMetaAtomsForSubstitution();
        HashSet vecmetaatoms = sqlMetaAtomInfo.retrieveMetaAtomTypesFromDatabase(metaAtomType);
        Iterator i = vecmetaatoms.iterator();
        while(i.hasNext()) {
            MetaAtomInfo info = (MetaAtomInfo) i.next();
            MetaAtomDefinition def = createMetaAtomDefinition(info);
            subs.addDefinition(def);
        }
        return subs;
    }
    public SubstituteBackMetaAtomsIntoMolecule createSubstitutionBackSets(String metaAtomType) throws SQLException, CDKException, ClassNotFoundException, IOException {
        SubstituteBackMetaAtomsIntoMolecule subs = new SubstituteBackMetaAtomsIntoMolecule();
        createSubstitutionBackSets(metaAtomType,subs);
        return subs;
    }
     public void  createSubstitutionBackSets(String metaAtomType, SubstituteBackMetaAtomsIntoMolecule subs) throws SQLException, CDKException, ClassNotFoundException, IOException {
         HashSet vecmetaatoms = sqlMetaAtomInfo.retrieveMetaAtomTypesFromDatabase(metaAtomType);
        Iterator i = vecmetaatoms.iterator();
        while(i.hasNext()) {
            MetaAtomInfo info = (MetaAtomInfo) i.next();
            MetaAtomDefinition def = createMetaAtomDefinition(info);
            subs.addDefinition(def);
        }

     }




}
