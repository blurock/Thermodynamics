/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.test;

import java.io.IOException;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author blurock
 */
public class GenerateStructures {
    
    /**
     * 
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    static public  StructureAsCML createGeneralCarbon() throws CDKException, ClassNotFoundException, IOException {
                    // Build Single Bonded Carbon
            Molecule mol = new Molecule();
            mol.setID("GeneralCarbon");
            Atom at1 = new Atom("C");
            Atom at2 = new Atom("Du");
            Atom at3 = new Atom("Du");
            Atom at4 = new Atom("Du");
            Atom at5 = new Atom("Du");
            mol.addAtom(at1);
            mol.addAtom(at2);
            mol.addAtom(at3);
            mol.addAtom(at4);
            mol.addAtom(at5);

            Bond bnd1 = new Bond(at1, at2);
            Bond bnd2 = new Bond(at1, at3);
            Bond bnd3 = new Bond(at1, at4);
            Bond bnd4 = new Bond(at1, at5);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);
            mol.addBond(bnd4);

            
            StructureAsCML cmlcarbon = new StructureAsCML(mol);
            cmlcarbon.setNameOfStructure("GeneralCarbon");
            return cmlcarbon;
    }
    /**
     * 
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    static public   StructureAsCML createMethyl() throws CDKException, ClassNotFoundException, IOException {
                    // Build Single Bonded Carbon
            Molecule mol = new Molecule();
            Atom at1 = new Atom("C");
            Atom at2 = new Atom("Du");
            Atom at3 = new Atom("H");
            Atom at4 = new Atom("H");
            Atom at5 = new Atom("H");
            mol.addAtom(at1);
            mol.addAtom(at2);
            mol.addAtom(at3);
            mol.addAtom(at4);
            mol.addAtom(at5);

            Bond bnd1 = new Bond(at1, at2);
            Bond bnd2 = new Bond(at1, at3);
            Bond bnd3 = new Bond(at1, at4);
            Bond bnd4 = new Bond(at1, at5);
            mol.addBond(bnd1);
            mol.addBond(bnd2);
            mol.addBond(bnd3);
            mol.addBond(bnd4);

            
            StructureAsCML cmlcarbon = new StructureAsCML(mol);
            return cmlcarbon;
    }
    /**
     * 
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    static public   StructureAsCML createEthane() throws CDKException, ClassNotFoundException, IOException {
            Molecule ethane = new Molecule();
            Atom eat1 = new Atom("C");
            Atom eat2 = new Atom("C");
            Atom eat3 = new Atom("H");
            Atom eat4 = new Atom("H");
            Atom eat5 = new Atom("H");
            Atom eat6 = new Atom("H");
            Atom eat7 = new Atom("H");
            Atom eat8 = new Atom("H");
            ethane.addAtom(eat1);
            ethane.addAtom(eat2);
            ethane.addAtom(eat3);
            ethane.addAtom(eat4);
            ethane.addAtom(eat5);
            ethane.addAtom(eat6);
            ethane.addAtom(eat7);
            ethane.addAtom(eat8);

            Bond ebnd1 = new Bond(eat1, eat2);
            Bond ebnd2 = new Bond(eat1, eat3);
            Bond ebnd3 = new Bond(eat1, eat4);
            Bond ebnd4 = new Bond(eat1, eat5);
            Bond ebnd5 = new Bond(eat2, eat6);
            Bond ebnd6 = new Bond(eat2, eat7);
            Bond ebnd7 = new Bond(eat2, eat8);
            ethane.addBond(ebnd1);
            ethane.addBond(ebnd2);
            ethane.addBond(ebnd3);
            ethane.addBond(ebnd4);
            ethane.addBond(ebnd5);
            ethane.addBond(ebnd6);
            ethane.addBond(ebnd7);

            
            StructureAsCML cmlethane = new StructureAsCML(ethane);
            

            return cmlethane;
    }
    /**
     * 
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    static public   StructureAsCML createPropane() throws CDKException, ClassNotFoundException, IOException {
            Molecule propane = new Molecule();
            Atom eat1  = new Atom("C");
            Atom eat2  = new Atom("C");
            Atom eat3  = new Atom("C");
            Atom eat4  = new Atom("H");
            Atom eat5  = new Atom("H");
            Atom eat6  = new Atom("H");
            Atom eat7  = new Atom("H");
            Atom eat8  = new Atom("H");
            Atom eat9  = new Atom("H");
            Atom eat10 = new Atom("H");
            Atom eat11 = new Atom("H");
            propane.addAtom(eat1);
            propane.addAtom(eat2);
            propane.addAtom(eat3);
            propane.addAtom(eat4);
            propane.addAtom(eat5);
            propane.addAtom(eat6);
            propane.addAtom(eat7);
            propane.addAtom(eat8);
            propane.addAtom(eat9);
            propane.addAtom(eat10);
            propane.addAtom(eat11);

            Bond ebnd1  = new Bond(eat1, eat2);
            Bond ebnd2  = new Bond(eat2, eat3);
            Bond ebnd3  = new Bond(eat1, eat4);
            Bond ebnd4  = new Bond(eat1, eat5);
            Bond ebnd5  = new Bond(eat1, eat6);
            Bond ebnd6  = new Bond(eat2, eat7);
            Bond ebnd7  = new Bond(eat2, eat8);
            Bond ebnd8  = new Bond(eat3, eat9);
            Bond ebnd9  = new Bond(eat3, eat10);
            Bond ebnd10 = new Bond(eat3, eat11);
            
            propane.addBond(ebnd1);
            propane.addBond(ebnd2);
            propane.addBond(ebnd3);
            propane.addBond(ebnd4);
            propane.addBond(ebnd5);
            propane.addBond(ebnd6);
            propane.addBond(ebnd7);
            propane.addBond(ebnd8);
            propane.addBond(ebnd9);
            propane.addBond(ebnd10);

            
            StructureAsCML cmlethane = new StructureAsCML(propane);
            

            return cmlethane;
    }
     /**
     * 
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    static public   StructureAsCML createCH3CHO() throws CDKException, ClassNotFoundException, IOException {
            Molecule mol = new Molecule();
            Atom eat1 = new Atom("C");
            Atom eat2 = new Atom("C");
            Atom eat3 = new Atom("O");
            Atom eat4 = new Atom("H");
            Atom eat5 = new Atom("H");
            Atom eat6 = new Atom("H");
            Atom eat7 = new Atom("H");
            mol.addAtom(eat1);
            mol.addAtom(eat2);
            mol.addAtom(eat3);
            mol.addAtom(eat4);
            mol.addAtom(eat5);
            mol.addAtom(eat6);
            mol.addAtom(eat7);

            Bond ebnd1 = new Bond(eat1, eat2);
            Bond ebnd2 = new Bond(eat2, eat3,Order.DOUBLE);
            Bond ebnd3 = new Bond(eat1, eat4);
            Bond ebnd4 = new Bond(eat1, eat5);
            Bond ebnd5 = new Bond(eat1, eat6);
            Bond ebnd6 = new Bond(eat2, eat7);
            mol.addBond(ebnd1);
            mol.addBond(ebnd2);
            mol.addBond(ebnd3);
            mol.addBond(ebnd4);
            mol.addBond(ebnd5);
            mol.addBond(ebnd6);

            
            StructureAsCML cmlmol = new StructureAsCML(mol);
            

            return cmlmol;
    }
    /**
     * 
     * @return
     * @throws org.openscience.cdk.exception.CDKException
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    static public   StructureAsCML createGeneralKetone() throws CDKException, ClassNotFoundException, IOException {
            Molecule mol = new Molecule();
            Atom eat1 = new Atom("C");
            Atom eat2 = new Atom("O");
            Atom eat3 = new Atom("Du");
            Atom eat4 = new Atom("Du");
            mol.addAtom(eat1);
            mol.addAtom(eat2);
            mol.addAtom(eat3);
            mol.addAtom(eat4);

            Bond ebnd1 = new Bond(eat1, eat2,Order.DOUBLE);
            Bond ebnd2 = new Bond(eat1, eat3);
            Bond ebnd3 = new Bond(eat1, eat4);
            mol.addBond(ebnd1);
            mol.addBond(ebnd2);
            mol.addBond(ebnd3);

            
            StructureAsCML cmlmol = new StructureAsCML(mol);
            

            return cmlmol;
    }
    static public StructureAsCML create2methylpropane() throws CDKException {
        SmilesParser generator = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        Molecule mol = (Molecule) generator.parseSmiles("CC(C)C");
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance());
        adder.addImplicitHydrogens(mol);
        for(int a=0;a<4;a++) {
            IAtom atom = mol.getAtom(a);
            int hydrogens = atom.getImplicitHydrogenCount();
            for(int i=0;i<hydrogens;i++) {
                Atom atm = new Atom("H");
                mol.addAtom(atm);
                Bond bnd = new Bond(atom,atm);
                mol.addBond(bnd);
            }
        }
        StructureAsCML structcml = new StructureAsCML(mol);
        return structcml; 
    }
        static public StructureAsCML createFromSmiles(String smiles) throws CDKException {
        SmilesParser generator = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        Molecule mol = (Molecule) generator.parseSmiles(smiles);
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance());
        adder.addImplicitHydrogens(mol);
        for(int a=0;a<4;a++) {
            IAtom atom = mol.getAtom(a);
            int hydrogens = atom.getImplicitHydrogenCount();
            for(int i=0;i<hydrogens;i++) {
                Atom atm = new Atom("H");
                mol.addAtom(atm);
                Bond bnd = new Bond(atom,atm);
                mol.addBond(bnd);
            }
        }
        StructureAsCML structcml = new StructureAsCML(mol);
        return structcml; 
    }
}
