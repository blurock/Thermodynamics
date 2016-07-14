/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.substructure;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.isomorphism.mcss.RMap;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.GetSubstructureMatches;

/**
 *
 * @author edwardblurock
 */
public class SubStructure {
    protected AtomContainer substructure;
    protected String sourceOfStructure;

    public SubStructure() {
        substructure = null;
    }


    public SubStructure(AtomContainer substructure, String source) {
        this.substructure = substructure;
    }

    public List<List<RMap>> structureMatch(SubStructure molecule) throws CDKException {
        boolean ans = false;
        GetSubstructureMatches matches = new GetSubstructureMatches();
        List<List<RMap>> map = matches.getAtomMatches((AtomContainer) molecule.getSubstructure(), substructure);
        return map;
    }
    public boolean isMatch(SubStructure molecule) throws CDKException {
        boolean ans = false;
        List<List<RMap>> map = structureMatch(molecule);
        if(map.size() == substructure.getAtomCount())
            ans = true;
        return ans;
    }

    public IAtomContainer getSubstructure() {
        return substructure;
    }

    public void setSubstructure(AtomContainer substructure) {
        this.substructure = substructure;
    }

    public String getSourceOfStructure() {
        return sourceOfStructure;
    }

    public void setSourceOfStructure(String sourceOfStructure) {
        this.sourceOfStructure = sourceOfStructure;
    }
    public String toString() {
        StringBuffer buf = new StringBuffer();
        try {
            StructureAsCML cmlstruct = new StructureAsCML(substructure);
            buf.append("Source:" + sourceOfStructure + "\n");
            buf.append(cmlstruct.getCmlStructureString());
        } catch (CDKException ex) {
            buf.append("Error in structure (converting to CML): " + substructure.getID());
        }
        return buf.toString();
    }
}
