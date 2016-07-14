/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compute;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import thermo.data.benson.BensonGroupStructuresFromMolecule;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.SQLSetOfBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonGroupStructures;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.StandardThergasBensonThermoType;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.structure.disassociation.CalculateDisassociationEnergy;
import thermo.data.structure.gauche.ComputeGaucheInteractions;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.AddHydrogenToSingleRadical;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;
import thermo.data.structure.structure.DB.SQLSubstituteBackMetaAtomIntoMolecule;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.CalculateSymmetryCorrection;
import thermo.data.structure.structure.vibrational.CalculateVibrationalCorrectionForRadical;
import thermo.data.structure.substructure.ThermodyanmicsForSubstructures;
import thermo.data.structure.utilities.MoleculeUtilities;
import thermo.exception.NotARadicalException;
import thermo.exception.ThermodynamicComputeException;
import thermo.exception.ThermodynamicException;
import thermo.properties.SProperties;

/**
 *
 * @author blurock
 */
public class ComputeThermodynamicsFromMolecule {
    String bensonAtomType = "BensonAtom";
    String nancyLinearFormType = "NancyLinearForm";
    ThermoSQLConnection connect;
    SQLMetaAtomDefinitionFromMetaAtomInfo sqlMetaAtom;
    SetOfMetaAtomsForSubstitution metaAtomSubstitutions;
    BensonGroupStructuresFromMolecule bensonGroups;
    SQLSetOfBensonThermodynamicBase sqlthermodynamics;
    SetOfBensonThermodynamicBase bensonBase;
    StandardThergasBensonThermoType standardbenson;
    NancyLinearFormToMolecule nancyFormToMolecule;
    SQLSubstituteBackMetaAtomIntoMolecule substitute;
    CalculateDisassociationEnergy disassociation;
    AddHydrogenToSingleRadical formRH;
    CalculateSymmetryCorrection symmetryCorrections;
    String cycleTypesS = "Cycles";
    ThermodyanmicsForSubstructures findCyclesThermo;
    String torsionTypesS = "StericCorrections";
    ThermodyanmicsForSubstructures findTorsionsThermo;
    ComputeGaucheInteractions gauche;
    InChIGeneratorFactory inchifactory;

    private double[] temperatures;
    double gasConstant;
    Molecule molecule;
    /** Constructor
     *
     * This is the basic constructor which sets up the computation of thermodynamic quantities.
     *
     * Several exceptions are caught within the code (see code) and are 'translated' to a general
     * 'ThermodyanmicComputeException'.
     *
     * @param c The mySQL connector
     * @throws ThermodynamicComputeException thrown if an error occurs
     */
    public ComputeThermodynamicsFromMolecule(ThermoSQLConnection c) throws ThermodynamicComputeException {
        try {
            connect = c;
            sqlMetaAtom = new SQLMetaAtomDefinitionFromMetaAtomInfo(connect);
            metaAtomSubstitutions = sqlMetaAtom.createSubstitutionSets(bensonAtomType);
            bensonGroups = new BensonGroupStructuresFromMolecule();
            bensonBase = new SetOfBensonThermodynamicBase();
            sqlthermodynamics = new SQLSetOfBensonThermodynamicBase(bensonBase, connect);
            standardbenson = new StandardThergasBensonThermoType();
            nancyFormToMolecule = new NancyLinearFormToMolecule(c);
            inchifactory = null;
            substitute = new SQLSubstituteBackMetaAtomIntoMolecule(nancyLinearFormType, connect);
            disassociation = new CalculateDisassociationEnergy(connect);
            temperatures = standardbenson.getTemperaturesAsDoubleValues();
            String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
            gasConstant = Double.valueOf(gasconstantS).doubleValue();
            formRH = new AddHydrogenToSingleRadical();
            symmetryCorrections = new CalculateSymmetryCorrection(connect);
            findCyclesThermo = new ThermodyanmicsForSubstructures(cycleTypesS, connect);
            findTorsionsThermo = new ThermodyanmicsForSubstructures(torsionTypesS, connect);
            gauche = new ComputeGaucheInteractions(connect);

        } catch (ThermodynamicException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in setting up database: " + ex.toString());
        } catch (CDKException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in setting up database: " + ex.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in setting up database: " + ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in setting up database: " + ex.toString());
        }
    }
    /** Compute the combined thermodynamics from the InChI string.
     * 
     * @param inchi The InChI string
     * @return The full thermodynamics (all Benson rules and corrections combined)
     * @throws ThermodynamicComputeException
     */
    public ThermodynamicInformation computeThermodynamicsFromInChI(String inchi) throws ThermodynamicComputeException {
        SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
       return computeThermodynamicsFromInChI(inchi,thermodynamics);
   }
    /** Compute the combined thermodynamics from the InChI string.
     * 
     * This converts the InChI string to a CDK molecule and calls the {@link computeThermodynamics} routine.
     * The Benson rules and corrections are added to {@link thermodynamics) and the combined value is returned.
     * 
     * @param inchi inchi The InChI string
        * @param thermodynamics  The Benson rules and corrections are added individually to this structure.
        * @return The full thermodynamics (all Benson rules and corrections combined)
     * @throws ThermodynamicComputeException
     */
    public ThermodynamicInformation computeThermodynamicsFromInChI(String inchi,SetOfBensonThermodynamicBase thermodynamics) throws ThermodynamicComputeException {
        ThermodynamicInformation thermo = null;
try {
            if(inchifactory == null) {
                    inchifactory = InChIGeneratorFactory.getInstance();
            }
            InChIToStructure istruct = inchifactory.getInChIToStructure(inchi, DefaultChemObjectBuilder.getInstance());
            molecule = new Molecule(istruct.getAtomContainer());
             substitute.substitute(molecule);
            thermo = computeThermodynamics(molecule, thermodynamics);
            

        } catch (CDKException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return thermo;
    }

    /** Compute thermodynamics from the Nancy Linear form.
     *
     * The routine converts the Nancy linear form to a CDK molecule and then
     * calls computeThermodyanmics (in this class).
     *
     * @param nancy The string with the nancy linear form.
     * @return The full thermodynamics (all Benson rules and corrections combined).
     * @throws ThermodynamicComputeException
     */
    public ThermodynamicInformation computeThermodynamics(String nancy) throws ThermodynamicComputeException {
        ThermodynamicInformation thermo = null;
        SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
        return computeThermodynamics(nancy,thermodynamics);
        }

    /** Compute thermodynamics from the Nancy Linear form.
     * 
     * This converts the Nancy Linear Form string to a CDK molecule and calls the {@link computeThermodynamics} routine.
     * The Benson rules and corrections are added to {@link thermodynamics) and the combined value is returned.

     * @param nancy The string with the nancy linear form.
     * @param thermodynamics The Benson rules and corrections are added individually to this structure.
     * @return The full thermodynamics (all Benson rules and corrections combined).
     * @throws ThermodynamicComputeException
     */
    public ThermodynamicInformation computeThermodynamics(String nancy,SetOfBensonThermodynamicBase thermodynamics) throws ThermodynamicComputeException {
        ThermodynamicInformation fullthermo = null;
        try {
            molecule = nancyFormToMolecule.convert(nancy);
            CDKHueckelAromaticityDetector aromatic = new CDKHueckelAromaticityDetector();
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
            boolean sromaticB = CDKHueckelAromaticityDetector.detectAromaticity(molecule);
            StructureAsCML cmlstruct = new StructureAsCML(molecule);
            System.out.println("Molecule after Aromatic Detection: \n" + cmlstruct.getCmlStructureString());
            substitute.substitute(molecule);
            StructureAsCML cmlstruct2 = new StructureAsCML(molecule);
            System.out.println("Molecule: \n" + cmlstruct2.getCmlStructureString());

            fullthermo = computeThermodynamics(molecule,thermodynamics);
        } catch (CDKException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in setting up molecule from linear form");
        }
        return fullthermo;
    }
    /** Compute thermodynamics from the CML structure.
     *
     * @param struct The molecule in CML form
     * @return The full thermodynamics (all Benson rules and corrections combined)
     * @throws ThermodynamicComputeException
     */
    public ThermodynamicInformation computeThermodynamics(StructureAsCML struct) throws ThermodynamicComputeException {
        SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
        return computeThermodynamics(struct,thermodynamics);
    }
    /**
     * 
     * This extracts the CDK molecule from the CML structure definition and calls the {@link computeThermodynamics} routine.
     * The Benson rules and corrections are added to {@link thermodynamics) and the combined value is returned.
     *
     * @param struct The molecule in CML form
     * @param thermodynamics The Benson rules and corrections are added individually to this structure.
     * @return The full thermodynamics (all Benson rules and corrections combined)
     * @throws ThermodynamicComputeException
     */
    public ThermodynamicInformation computeThermodynamics(StructureAsCML struct,SetOfBensonThermodynamicBase thermodynamics) throws ThermodynamicComputeException {
        try {
            System.out.println(struct.getCmlStructureString());
            molecule = struct.getMolecule();
        } catch (CDKException ex) {
            throw new ThermodynamicComputeException(ex.toString());
        }
        return computeThermodynamics(molecule,thermodynamics);
    }

    /** This computes the thermodynamic information given the CDK molecule
     *
     * @param mol The molecule in CDK Molecule form
     * @return The full thermodynamics (all Benson rules and corrections combined)
     * @throws ThermodynamicComputeException
     */
    public ThermodynamicInformation computeThermodynamics(Molecule mol) throws ThermodynamicComputeException {
        molecule = mol;
        SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
        return computeThermodynamics(mol,thermodynamics);
    }
    /** Compute the thermodynamics from the CDK molecule.
     *
     * The Benson rules and corrections are added to {@link thermodynamics) and the combined value is returned.
     *
     * This routine checks if the molecule is a radical or a stable species.
     * If the molecule is a radical, then the {@link computeThermodynamicsForRadicalContributions} is called
     * to get the radical corrections. If the molecule is a stable species, then {@link computeThermodynamicsForMolecule} is called.
     *
     * The list of corrections from either routine (added to the thermodynamics structure) is then combined into one value
     * and this value is returned and printed.
     *
     * @param molecule The molecule in CDK Molecule form
     * @param thermodynamics The Benson rules and corrections are added individually to this structure.
     * @return The full thermodynamics (all Benson rules and corrections combined)
     * @throws ThermodynamicComputeException
     */
    public ThermodynamicInformation computeThermodynamics(Molecule molecule, SetOfBensonThermodynamicBase thermodynamics) throws ThermodynamicComputeException {
        BensonThermodynamicBase combinedThermodynamics = null;
        try {
            CDKHueckelAromaticityDetector aromatic = new CDKHueckelAromaticityDetector();

            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
            StructureAsCML cmlstruct = new StructureAsCML(molecule);
            System.out.println(cmlstruct.getCmlStructureString());

            boolean sromaticB = CDKHueckelAromaticityDetector.detectAromaticity(molecule);
            if(formRH.isARadical(molecule)) {
                computeThermodynamicsForRadicalContributions(molecule, thermodynamics);
            } else {
                computeThermodynamicsForMolecule(molecule, thermodynamics);
            }
            System.out.println("");
            System.out.println("=========== Contributions ==============================");
            System.out.println(thermodynamics.toString());
            System.out.println("========================================================");

            combinedThermodynamics = thermodynamics.combineToOneBensonRule(temperatures);
            combinedThermodynamics.setID("Total");
            combinedThermodynamics.setReference("Sum Total");
            System.out.println(combinedThermodynamics.toString());
            System.out.println("========================================================");
        } catch (ThermodynamicException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in calculating thermodyanmics: " + ex.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in calculating thermodyanmics: " + ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThermodynamicComputeException("Error in calculating thermodyanmics: " + ex.toString());
        } catch (SQLException ex) {
                    Logger.getLogger(ComputeThermodynamicsFromMolecule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return combinedThermodynamics;
    }
    /** This computes the thermodynamics for a CDK stable molecule.
     *
     * @param mol The molecule in CDK Molecule form
     * @param thermo The Benson rules and corrections are added individually to this structure.
     * @throws ThermodynamicException
     * @throws CDKException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public void computeThermodynamicsForMolecule(Molecule mol, SetOfBensonThermodynamicBase thermo) throws ThermodynamicException, CDKException, ClassNotFoundException, IOException, SQLException {
        //StructureAsCML struct = new StructureAsCML(mol);
        Molecule substituted = metaAtomSubstitutions.substitute(mol);
        SetOfBensonGroupStructures bensonset = bensonGroups.deriveBensonGroupStructures(substituted);
        sqlthermodynamics.setUpFromSetOfBensonGroupStructures(bensonset,thermo);
        System.out.println(bensonset.toString());

        symmetryCorrections.calculate(mol, thermo);
        BensonThermodynamicBase cycle = (BensonThermodynamicBase) findCyclesThermo.FindLargestStructureThermodynamics(substituted);
        if(cycle != null)
            thermo.add(cycle);

        gauche.compute(substituted, thermo);
    }
    /** Compute thermodynamic corrections for radical
     *
     * For both the radical and the stable species (radical with a hydrogen added: {@link AddHydrogenToSingleRadical} compute:
     * <ul>
     * <li> Benson Groups
     * <li> Symmetry
     * <li> Disassociation energy
     * <li> Spin contribution
     * <li> Hydrogen radical contribution
     * <li> Vibrational structures
     * </ul>
     *
     * The radical species corrections are subtracted from the stable species values.
     *
     * @param R The radical in CDK Molecule form
     * @param thermo The Benson rules and corrections are added individually to this structure.
     * @throws NotARadicalException routine called for a stable species
     * @throws ThermodynamicException
     * @throws SQLException Usually means no SQL connection
     * @throws CDKException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void computeThermodynamicsForRadicalContributions(Molecule R, SetOfBensonThermodynamicBase thermo) throws NotARadicalException, ThermodynamicException, SQLException, CDKException, IOException, ClassNotFoundException{

        SetOfBensonThermodynamicBase thermominus = new SetOfBensonThermodynamicBase();
        Molecule RH = formRH.convert(R);

        MoleculeUtilities.normalizeMolecule(RH);
        StructureAsCML struct = new StructureAsCML(RH);
        Molecule substituted = metaAtomSubstitutions.substitute(struct);
        SetOfBensonGroupStructures bensonset = bensonGroups.deriveBensonGroupStructures(substituted);
        sqlthermodynamics.setUpFromSetOfBensonGroupStructures(bensonset,thermo);
        symmetryCorrections.calculate(RH, thermo);
        disassociation.calculate(R,thermo);

        BensonThermodynamicBase spinthermo = computeSpinContribution();
        thermo.add(spinthermo);

        BensonThermodynamicBase hradical = computeHydrogenRadicalContribution();
        thermo.add(hradical);

        CalculateVibrationalCorrectionForRadical vibrational = new CalculateVibrationalCorrectionForRadical(connect);
        vibrational.calculate(R, RH, thermo);

        symmetryCorrections.calculate(R, thermominus);
        thermominus.Minus();
        thermo.add(thermominus);
    }
    BensonThermodynamicBase computeSpinContribution() {
        double spin = gasConstant * Math.log1p(1.0);
        Double ln2 = Math.log1p(1.0);
        Double spinD = new Double(spin);
        String spinS = "Spin Contribution: Rln(2) = (" + gasConstant + ")*(" + Math.log1p(1.0) + ")";
        BensonThermodynamicBase spinthermo = new BensonThermodynamicBase(spinS, null, 0.0, spinD);
        spinthermo.setReference(spinS);
        return spinthermo;
    }
    Molecule getMolecule() {
        return molecule;
    }

    private BensonThermodynamicBase computeHydrogenRadicalContribution() {
        double hrad = -52.1;
        Double hradD = new Double(hrad);
        String hradS = "Hydrogen Radical";
        BensonThermodynamicBase hradical = new BensonThermodynamicBase(hradS, null, hradD, 0.0);
        hradical.setReference(hradS);
        return hradical;

    }
    public BensonThermodynamicBase combineThermodynamics(SetOfBensonThermodynamicBase thermodynamics) throws ThermodynamicComputeException{
            return thermodynamics.combineToOneBensonRule(temperatures);
    }
}
