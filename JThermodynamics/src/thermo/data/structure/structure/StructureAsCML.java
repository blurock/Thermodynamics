/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Iterator;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemModel;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.io.CMLWriter;
import org.openscience.cdk.libio.cml.Convertor;
import org.xmlcml.cml.element.CMLMolecule;

/**
 *
 * @author blurock
 */
public class StructureAsCML  {
    private String nameOfStructure;
    private String cmlStructureString;
    public String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public StructureAsCML(AtomContainer mol) throws CDKException {
        nameOfStructure = mol.getID();
        source = "Standard";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CMLWriter write = new CMLWriter(output);
        write.write(mol);
        cmlStructureString = output.toString();
    }
    public StructureAsCML(AtomContainer mol, String src) throws CDKException {
        nameOfStructure = mol.getID();
        source = src;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CMLWriter write = new CMLWriter(output);
        write.write(mol);
        cmlStructureString = output.toString();
    }
    
    public StructureAsCML(String name, String cml) {
        nameOfStructure = name;
        cmlStructureString = cml;
        source = "Standard";
    }
    public StructureAsCML(String name, String cml, String src) {
        nameOfStructure = name;
        cmlStructureString = cml;
        source = src;
    }
    public StructureAsCML() {
        nameOfStructure = "Default";
        cmlStructureString = "--";
        source = "Standard";
    }
    public StructureAsCML(CMLMolecule molecule) {
        cmlStructureString = molecule.toXML();
        source = "Standard";
        nameOfStructure = molecule.getId();
    }
    
    public CMLMolecule getCMLMolecule() throws CDKException  {
        Molecule molecule = getMolecule();
        Convertor convert = new Convertor(true, nameOfStructure);
        CMLMolecule cml = convert.cdkMoleculeToCMLMolecule(molecule);
        return cml;
    }
    public Molecule getMolecule() throws CDKException {
        StringReader read = new StringReader(cmlStructureString);
        ByteArrayInputStream str = new ByteArrayInputStream(cmlStructureString.getBytes());
        DefaultChemObjectBuilder build = (DefaultChemObjectBuilder) DefaultChemObjectBuilder.getInstance();
        IChemFile file =    new ChemFile();   //build.newChemFile();
        CMLReader reader = new CMLReader(str);
        IChemObject chemobj = reader.read(file);
        IChemFile chemfile = (IChemFile) chemobj;
        //System.out.println(chemfile.getChemSequenceCount());
        IChemSequence chemSequence = chemfile.getChemSequence(0);
	ChemModel chemModel = (ChemModel) chemSequence.getChemModel(0);
        
	IMoleculeSet setOfMolecules = chemModel.getMoleculeSet();
	Molecule molecule = (Molecule) setOfMolecules.getMolecule(0);
        setAromaticAtomsFromBonds(molecule);
        return molecule;
     }
    public void setAromaticAtomsFromBonds(Molecule molecule) {
        Iterator<IAtom> atomiterator = molecule.atoms().iterator();
        while(atomiterator.hasNext()) {
            IAtom atom = atomiterator.next();
            if(atom.getFlag(CDKConstants.ISAROMATIC)) {
                System.out.println("Aromatic: " + atom.toString());
            }
        }
        Iterator<IBond> bonditerator = molecule.bonds().iterator();
        while(bonditerator.hasNext()) {
            IBond bond = bonditerator.next();
            if(bond.getFlag(CDKConstants.ISAROMATIC)) {
                IAtom atom1 = bond.getAtom(0);
                IAtom atom2 = bond.getAtom(1);
                //atom1.setFlag(CDKConstants.ISAROMATIC, true);
                //atom2.setFlag(CDKConstants.ISAROMATIC, true);
            }
        }
        atomiterator = molecule.atoms().iterator();
        while(atomiterator.hasNext()) {
            IAtom atom = atomiterator.next();
            if(atom.getFlag(CDKConstants.ISAROMATIC)) {
                System.out.println("Aromatic: " + atom.toString());
            }
        }
    }
    public String getNameOfStructure() {
        return nameOfStructure;
    }

    public void setNameOfStructure(String nameOfStructure) {
        this.nameOfStructure = nameOfStructure;
    }

    public String getCmlStructureString() {
        return cmlStructureString;
    }

    public void setCmlStructureString(String cmlStructureString) {
        this.cmlStructureString = cmlStructureString;
    }


}
