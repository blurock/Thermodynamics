/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package symmetry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.ParsingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.CML.CMLSymmetryDefinition;
import thermo.data.structure.structure.symmetry.ListOfSymmetryPairs;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;
import thermo.data.structure.structure.symmetry.SymmetryPair;
import thermo.test.GenerateStructures;

/**
 *
 * @author edwardblurock
 */
public class CMLSymmetryDefinitionTest {
    
    public CMLSymmetryDefinitionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    @Test
    public void cmlTest() {
        try {
            GenerateStructures generate = new GenerateStructures();
            StructureAsCML ethane = generate.createEthane();
            
            String name = "ethaneSymmetry";
            Double symmetryFactor = new Double(2.0);
            
            ArrayList<SymmetryPair>  pairlist = new ArrayList<SymmetryPair>();
            String name1 = "pair1";
            String struct1 = "ch3";
            String connectedSym = "SSS";
            SymmetryPair pair1 = new SymmetryPair(name1,struct1, connectedSym);
            pairlist.add(pair1);
            
            SymmetryDefinition def = new SymmetryDefinition(name, ethane, pairlist, symmetryFactor);
            
            CMLSymmetryDefinition cmlsymmetry = new CMLSymmetryDefinition();
            cmlsymmetry.setStructure(def);
            String cmlstring = cmlsymmetry.restore();
            System.out.println("CMLSymmetryDefinition:\n" + cmlstring);
            
            CMLSymmetryDefinition cmlsymmetry1 = new CMLSymmetryDefinition();
            cmlsymmetry1.parse(cmlstring);
            SymmetryDefinition symdef = (SymmetryDefinition) cmlsymmetry1.getStructure();
            System.out.println("Element Name   :    " + symdef.getMetaAtomName());
            System.out.println("Type           :    " + symdef.getMetaAtomType());
            Molecule molecule = symdef.getMolecule();
            System.out.println("atoms and bonds:    " + molecule.getAtomCount() + ", " + molecule.getBondCount());
            System.out.println("CMLStructure   :    " + symdef.getElementName());
            Double internalSymmetryFactor = symdef.getInternalSymmetryFactor();
            System.out.println("Symmetry Factor: " + internalSymmetryFactor.toString());
            List<SymmetryPair> listOfSymmetryPairs = symdef.extractListOfSymmetryPairs();
            Iterator<SymmetryPair> iterator = listOfSymmetryPairs.iterator();
            while(iterator.hasNext()){
                SymmetryPair next = iterator.next();
                System.out.println("     Symmetry Pair: " + next.getGroupName() 
                        + ": " + next.getStructureName()
                        + ", " +  next.getConnectedSymmetry());
            }
            
        } catch (CDKException ex) {
            Logger.getLogger(CMLSymmetryDefinitionTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CMLSymmetryDefinitionTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CMLSymmetryDefinitionTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParsingException ex) {
            Logger.getLogger(CMLSymmetryDefinitionTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    
    }
}
