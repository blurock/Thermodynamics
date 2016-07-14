/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metaatom;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.StructureAsCML;

import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.SubstituteBackMetaAtomsIntoMolecule;
import thermo.test.GenerateStructures;
/**
 *
 * @author reaction
 */
public class SubstituteMetaAtomsInMoleculeTest {

    public SubstituteMetaAtomsInMoleculeTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void substituteCO() {
        try {
            StructureAsCML generalketone = GenerateStructures.createGeneralKetone();
            System.out.println("Substitute for CO:\n" + generalketone.getCmlStructureString());

        Molecule mol = new Molecule();
        mol.setID("Aldehyde");
        Atom at1 = new Atom("CO");
        Atom at2 = new Atom("H");
        Atom at3 = new Atom("H");
        Bond bnd1 = new Bond(at1, at2);
        Bond bnd2 = new Bond(at1, at3);
        mol.addAtom(at1);
        mol.addAtom(at2);
        mol.addAtom(at3);
        mol.addBond(bnd1);
        mol.addBond(bnd2);

        System.out.println("Set up Meta atom for CO");
        MetaAtomInfo info = new MetaAtomInfo();
        info.setElementName("CarbonMonoxide");
        info.setMetaAtomType("NancyLinearForm");
        info.setMetaAtomName("co");
        MetaAtomDefinition metaatom = new MetaAtomDefinition(info,generalketone);
        SubstituteBackMetaAtomsIntoMolecule substitute = new SubstituteBackMetaAtomsIntoMolecule();
        substitute.add(metaatom);

        System.out.println("Start Substitution");
        System.out.println("Molecule Before Subsitution:\n" + mol.toString());
        substitute.substitute(mol);
        StructureAsCML subbed = new StructureAsCML(mol);
        System.out.println("Molecule After Substitution:\n" + subbed.getCmlStructureString());

        } catch (CDKException ex) {
            Logger.getLogger(SubstituteMetaAtomsInMoleculeTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SubstituteMetaAtomsInMoleculeTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SubstituteMetaAtomsInMoleculeTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}