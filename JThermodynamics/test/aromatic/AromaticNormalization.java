/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aromatic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.isomorphism.mcss.RMap;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.CML.CMLMetaAtomInfo;
import thermo.data.structure.structure.DB.SQLMetaAtomInfo;
import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.GetSubstructureMatches;
import thermo.data.structure.structure.matching.SubstituteMetaAtom;

/**
 *
 * @author edwardblurock
 */
public class AromaticNormalization {

    public AromaticNormalization() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    //@Test
    public void aromatic() {
        try {
            System.out.println("______________________________________________________");
            String aromaticS = "InChI=1S/C10H10/c1-2-6-10-8-4-3-7-9(10)5-1/h1-3,5-7H,4,8H2";
            InChIGeneratorFactory inchifactory = InChIGeneratorFactory.getInstance();
            InChIToStructure istruct = inchifactory.getInChIToStructure(aromaticS, DefaultChemObjectBuilder.getInstance());
            AtomContainer molecule = new AtomContainer(istruct.getAtomContainer());
            StructureAsCML cmlorig = new StructureAsCML(molecule);
            System.out.println("Original Aromatic Molecule:\n" + cmlorig.getCmlStructureString());

            CDKHueckelAromaticityDetector aromatic = new CDKHueckelAromaticityDetector();
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
            boolean sromaticB = CDKHueckelAromaticityDetector.detectAromaticity(molecule);
            StructureAsCML cmlstruct = new StructureAsCML(molecule);
            System.out.println("Molecule after Aromatic Detection: \n" + cmlstruct.getCmlStructureString());

            CMLMetaAtomInfo metaatominfo = new CMLMetaAtomInfo();
            ThermoSQLConnection connect = new ThermoSQLConnection();
            boolean connected = connect.connect();
            if(connected) {
                SQLMetaAtomInfo infoSQL = new SQLMetaAtomInfo(connect);
                
                SQLStructureAsCML structSQL = new SQLStructureAsCML(connect);
                String carbonS = "AromaticCarbon";
                HashSet set = structSQL.retrieveStructuresFromDatabase(carbonS);
                Iterator<StructureAsCML> iter = set.iterator();
                if(iter.hasNext()) {
                    StructureAsCML info = iter.next();
                    System.out.println("AromaticCarbon\n" + info.getCmlStructureString());

                    //Molecule aromaticCarbon = info.getMolecule();
                    AtomContainer mol = info.getMolecule();
                    MetaAtomDefinition def = new MetaAtomDefinition("c/b", mol);
                    SubstituteMetaAtom sub = new SubstituteMetaAtom(def);

                    GetSubstructureMatches matches = new GetSubstructureMatches();
                    List<List<RMap>> atomMatches = matches.getAtomMatches(molecule, mol);
                    System.out.println("Number of atom matches: " + atomMatches.size());

                    sub.substitute(molecule);

                    StructureAsCML cmlmol = new StructureAsCML(molecule);
                    System.out.println(cmlmol.getCmlStructureString());
                    System.out.println("______________________________________________________");
                    /*
                    GetSubstructureMatches matches = new GetSubstructureMatches();
                    List<List<RMap>> atomMatches = matches.getAtomMatches(molecule, aromaticCarbon);
                    System.out.println("Number of atom matches: " + atomMatches.size());
                    List<List<RMap>> bondMatches = matches.getBondMatches(molecule, aromaticCarbon);
                    System.out.println("Number of bond matches: " + bondMatches.size());
                     */
                } else {
                    System.err.println("No Structure found");
                }

                } else {
                System.err.println("Not connected to database");
                }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AromaticNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AromaticNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AromaticNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(AromaticNormalization.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Test
    public void cyclic() {
        try {
            System.out.println("______________________________________________________");
            String nancy = "ch3/ch(#1)/ch2/ch2/ch2/ch2/ch2/1";
            ThermoSQLConnection connect = new ThermoSQLConnection();
            boolean connected = connect.connect();
            if(connected) {

            NancyLinearFormToMolecule nancyFormToMolecule = new NancyLinearFormToMolecule(connect);
            AtomContainer molecule = nancyFormToMolecule.convert(nancy);

            CDKHueckelAromaticityDetector aromatic = new CDKHueckelAromaticityDetector();
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
            boolean sromaticB = CDKHueckelAromaticityDetector.detectAromaticity(molecule);
            StructureAsCML cmlstruct = new StructureAsCML(molecule);
            System.out.println("Molecule after Aromatic Detection: \n" + cmlstruct.getCmlStructureString());
                String carbonS = "AromaticCarbon";
                SQLStructureAsCML structSQL = new SQLStructureAsCML(connect);
                HashSet set = structSQL.retrieveStructuresFromDatabase(carbonS);
                Iterator<StructureAsCML> iter = set.iterator();
                if(iter.hasNext()) {
                    StructureAsCML info = iter.next();
                    System.out.println("AromaticCarbon\n" + info.getCmlStructureString());

                    //Molecule aromaticCarbon = info.getMolecule();
                    AtomContainer mol = info.getMolecule();
                    MetaAtomDefinition def = new MetaAtomDefinition("c/b", mol);
                    SubstituteMetaAtom sub = new SubstituteMetaAtom(def);

                    GetSubstructureMatches matches = new GetSubstructureMatches();
                    List<List<RMap>> atomMatches = matches.getAtomMatches(molecule, mol);
                    System.out.println("Number of atom matches: " + atomMatches.size());
                    System.out.println("______________________________________________________");
            }
                /*
            String bensonAtomType = "BensonAtom";
            SQLMetaAtomDefinitionFromMetaAtomInfo sqlMetaAtom = new SQLMetaAtomDefinitionFromMetaAtomInfo(connect);
            SetOfMetaAtomsForSubstitution metaAtomSubstitutions = sqlMetaAtom.createSubstitutionSets(bensonAtomType);
            Molecule substituted = metaAtomSubstitutions.substitute(molecule);
            StructureAsCML cmlstructsub = new StructureAsCML(molecule);
            System.out.println("Molecule after BensonAtom Detection: \n" + cmlstructsub.getCmlStructureString());
            System.out.println("______________________________________________________");
                 * 
                 */
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AromaticNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AromaticNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(AromaticNormalization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AromaticNormalization.class.getName()).log(Level.SEVERE, null, ex);
        }

        }
}