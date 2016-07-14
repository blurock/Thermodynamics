/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import org.openscience.cdk.RingSet;
import org.openscience.cdk.exception.CDKException;
//import org.openscience.cdk.tools.ValencyChecker;
//import org.openscience.cdk.atomtype.HybridizationMatcher;
//import org.openscience.cdk.tools.HydrogenAdder;
//import org.openscience.cdk.aromaticity.HueckelAromaticityDetector;
import org.openscience.cdk.interfaces.IBond.Order;

public class MolelculePropertiesTest {

    public MolelculePropertiesTest() {
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
    public void ValencyChecker1() {
        System.out.println("---------------- Valency Checker 1 ----------------------");
        /*
        try {
            Molecule mol = new Molecule();
            mol.setID("Aldehyde");
            Atom at1 = new Atom("C");
            Atom at2 = new Atom("H");
            Atom at3 = new Atom("H");
            Atom at4 = new Atom("O");
            Bond bnd1 = new Bond(at1, at2);
            Bond bnd2 = new Bond(at1, at3);
            Bond bnd3 = new Bond(at1, at4,Order.DOUBLE);
            mol.addAtom(at1);
            mol.addAtom(at2);
            mol.addAtom(at3);
            mol.addAtom(at4);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);
            
           
 
            
            BasicValidator validate = new BasicValidator();
            ValidationReport report = validate.validateMolecule(mol);
            StructGenAtomTypeGuesser guesser = new StructGenAtomTypeGuesser();
            HybridizationMatcher hybrid = new HybridizationMatcher();
            ValencyChecker vcheck = new ValencyChecker();
            CDKBasedAtomTypeConfigurator config = new CDKBasedAtomTypeConfigurator();
            //config.readAtomTypes(DefaultChemObjectBuilder.getInstance());
            
            if (vcheck.isSaturated(mol)) {
                System.out.println("Is saturated");
            } else {
                System.out.println("Is not saturated");
            }
            for (int i = 0; i < mol.getAtomCount(); i++) {
                IAtom atm = mol.getAtom(i);
                System.out.println("=======================Atom Type:" + atm.getAtomicNumber());
                System.out.println("Number of Implicite Hydrogens:   " +
                        vcheck.calculateNumberOfImplicitHydrogens(atm));
                System.out.println("Valency:                         " + atm.getValency());
                System.out.println("Bond Order Sum:                  " + atm.getBondOrderSum());
                System.out.println("HydrogenCount:                   " + atm.getHydrogenCount());
                atm.setValency(vcheck.calculateNumberOfImplicitHydrogens(atm));
                List types = guesser.possbibleAtomTypes(mol, atm);
                IAtomType tp = hybrid.findMatchingAtomType(mol, atm);
                if (tp != null) {
                    System.out.println("Atom Types Match:    " + tp.getAtomTypeName() + ", " + tp.getValency() + ", " + tp.getFormalNeighbourCount());
                } else {
                    System.out.println("Type not found");
                }
                for (int t = 0; t < types.size(); t++) {
                    IAtomType type = (IAtomType) types.get(t);
                    System.out.println("Atom Types:    " + type.getAtomTypeName() + ", " + type.getValency() + ", " + type.getFormalNeighbourCount());

                }

            }
        } catch (CDKException ex) {
            Logger.getLogger(MolelculePropertiesTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MolelculePropertiesTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MolelculePropertiesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
    }

    @Test
    public void ValencyChecker2() {
        /*
        System.out.println("---------------- Valency Checker 2 ----------------------");
        try {
            Molecule mol = new Molecule();
            mol.setID("Aldehyde");
            Atom at1 = new Atom("C");
           
            IsotopeFactory.getInstance(DefaultChemObjectBuilder.getInstance()).configure(at1);
            CDKBasedAtomTypeConfigurator configurator = new CDKBasedAtomTypeConfigurator();
            Atom at4 = new Atom("O");
            IsotopeFactory.getInstance(DefaultChemObjectBuilder.getInstance()).configure(at4);
            Bond bnd3 = new Bond(at1, at4);
            bnd3.setOrder(2);
            mol.addAtom(at1);
            mol.addAtom(at4);
            mol.addBond(bnd3);
            StructGenAtomTypeGuesser guesser = new StructGenAtomTypeGuesser();
      
            HybridizationMatcher hybrid = new HybridizationMatcher();
            ValencyChecker vcheck = new ValencyChecker();
            //SaturationChecker schecker = new SaturationChecker();
            //schecker.newSaturate(mol);
            
            
            if (vcheck.isSaturated(mol)) {
                System.out.println("Is saturated");
            } else {
                System.out.println("Is not saturated");
            }
            HydrogenAdder hadder = new HydrogenAdder(vcheck);
            hadder.addHydrogensToSatisfyValency(mol);
            
            System.out.println("Saturate");
            LonePairElectronChecker lonepair = new LonePairElectronChecker();
            lonepair.newSaturate(mol);
            System.out.println("Get Lone Pair Count:   " + mol.getLonePairCount());
            for(int i=0;i<mol.getLonePairCount();i++) {
                ILonePair lp = mol.getLonePair(i);
                IAtom atm = lp.getAtom();
                System.out.println("lone Pair on atom " + atm.getSymbol());
            }
            for (int i = 0; i < mol.getAtomCount(); i++) {
                IAtom atm = mol.getAtom(i);
                System.out.println("====================Atom Type:   " + atm.getSymbol());
                System.out.println("Number of Implicite Hydrogens:   " +
                        vcheck.calculateNumberOfImplicitHydrogens(atm));
                System.out.println("Valency:                         " + atm.getValency());
                System.out.println("Atom Type Name:                  " + atm.getAtomTypeName());
                System.out.println("Bond Order Sum:                  " + atm.getBondOrderSum());
                System.out.println("HydrogenCount:                   " + atm.getHydrogenCount());
                atm.setValency(vcheck.calculateNumberOfImplicitHydrogens(atm));
                List types = guesser.possbibleAtomTypes(mol, atm);
                IAtomType tp = hybrid.findMatchingAtomType(mol, atm);
                if (tp != null) {
                    System.out.println("Atom Type Match:    " + tp.getAtomTypeName() + ", " + tp.getValency() + ", " + tp.getFormalNeighbourCount());
                } else {
                    System.out.println("Type not found");
                }
                for (int t = 0; t < types.size(); t++) {
                    IAtomType type = (IAtomType) types.get(t);
                    System.out.println("Atom Types:    " + type.getAtomTypeName() + ", " + type.getValency() + ", " + type.getFormalNeighbourCount());

                }

            }
            Convertor convertor = new Convertor(true,null);
            CMLMolecule cmlmol = convertor.cdkMoleculeToCMLMolecule(mol);
            System.out.println(cmlmol.toXML());
            
        } catch (CDKException ex) {
            Logger.getLogger(MolelculePropertiesTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MolelculePropertiesTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MolelculePropertiesTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MolelculePropertiesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
    }

    @Test
    public void Aromaticity() {
        System.out.println("-------------------------- Aromaticity -----------------------");
        try {

            Molecule mol = new Molecule();
            Atom atm1 = new Atom("N");
            Atom atm2 = new Atom("C");
            Atom atm3 = new Atom("C");
            Atom atm4 = new Atom("C");
            Atom atm5 = new Atom("C");
            Atom atm6 = new Atom("C");
            Atom atm7 = new Atom("C");
            mol.addAtom(atm1);
            mol.addAtom(atm2);
            mol.addAtom(atm3);
            mol.addAtom(atm4);
            mol.addAtom(atm5);
            mol.addAtom(atm6);
            mol.addAtom(atm7);
            Bond bnd1 = new Bond(atm1, atm2, Order.SINGLE);
            Bond bnd2 = new Bond(atm2, atm3, Order.DOUBLE);
            Bond bnd3 = new Bond(atm3, atm4, Order.SINGLE);
            Bond bnd4 = new Bond(atm4, atm5, Order.DOUBLE);
            Bond bnd5 = new Bond(atm5, atm6, Order.SINGLE);
            Bond bnd6 = new Bond(atm6, atm1, Order.DOUBLE);
            Bond bnd7 = new Bond(atm1, atm7, Order.SINGLE);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);
            mol.addBond(bnd4);
            mol.addBond(bnd5);
            mol.addBond(bnd6);
            
           
            //AromaticityCalculator calc = new AromaticityCalculator();
            org.openscience.cdk.graph.SpanningTree tree = new org.openscience.cdk.graph.SpanningTree(mol);
            RingSet set = (RingSet) tree.getAllRings();
            System.out.println("Number of Rings: " + set.getAtomContainerCount());
            /*
            HueckelAromaticityDetector.detectAromaticity(mol);
            for (int i = 0; i < mol.getAtomCount(); i++) {
                IAtom atm = mol.getAtom(i);
          
                if (atm.getFlag(CDKConstants.ISAROMATIC)) {
                    System.out.println("Atom  " + i + " is Aromatic");
                } else {
                    System.out.println("Atom  " + i +"  is not Aromatic");
                }

            }
             */
        } catch (CDKException ex) {
            Logger.getLogger(MolelculePropertiesTest.class.getName()).log(Level.SEVERE, null, ex);
        }



    }
}