/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;

/**
 *
 * @author edwardblurock
 */
public class DatabaseMolecule {

    String CMLStructure;
    String Source;
    String Molecule;

    public DatabaseMolecule(String mol, String cmlstruct, String src) {
        Molecule = mol;
        CMLStructure = cmlstruct;
        Source = src;
    }
    public DatabaseMolecule(Molecule mol, String src) throws CDKException {
        StructureAsCML cmlstruct = new StructureAsCML(mol);
        CMLStructure = cmlstruct.getCmlStructureString();
        Source = src;
        Molecule = mol.getID();
    }

    public String getCMLStructure() {
        return CMLStructure;
    }

    public void setCMLStructure(String CMLStructure) {
        this.CMLStructure = CMLStructure;
    }

    public String getMolecule() {
        return Molecule;
    }

    public void setMolecule(String Molecule) {
        this.Molecule = Molecule;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String Source) {
        this.Source = Source;
    }

}
