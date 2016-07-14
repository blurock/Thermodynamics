/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.io.IOException;
import java.util.List;
import org.openscience.cdk.exception.CDKException;
import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.StructureAsCML;

/**
 *
 * @author blurock
 */
public class SymmetryDefinition extends MetaAtomDefinition {
    protected SetOfSymmetryAssignments table;
    Double internalSymmetryFactor;
    public SymmetryDefinition(String symname, 
            StructureAsCML structure, 
            List<SymmetryPair> pairlist) throws CDKException, ClassNotFoundException, IOException {
        super(symname, structure);
        internalSymmetryFactor = new Double(1.0);
        table = new SetOfSymmetryAssignments(pairlist,getMolecule());
    }
    public SymmetryDefinition(MetaAtomDefinition info,
            List<SymmetryPair> pairlist) {
        super(info);
        table = new SetOfSymmetryAssignments(pairlist,getMolecule());
    }
    public SymmetryDefinition(String symname,
            StructureAsCML structure,
            List<SymmetryPair> pairlist,
            Double factor) throws CDKException, ClassNotFoundException, IOException {
        super(symname, structure);
        internalSymmetryFactor = factor;
        table = new SetOfSymmetryAssignments(pairlist,getMolecule());
    }
    public SymmetryDefinition(MetaAtomDefinition info,
            List<SymmetryPair> pairlist,
            Double factor) {
        super(info);
        internalSymmetryFactor = factor;
        table = new SetOfSymmetryAssignments(pairlist,getMolecule());
    }
    public SymmetryDefinition(SymmetryDefinition symmetry) {
        super(symmetry);
        table = new SetOfSymmetryAssignments(symmetry.table);
        internalSymmetryFactor = new Double(symmetry.internalSymmetryFactor);
    }
    /**
     * 
     * @return
     */
    public List<SymmetryPair> extractListOfSymmetryPairs() {
        return table.extractListOfSymmetryPairs();
    }
    @Override
    public String toString() {
        String metaS = super.toString();
        String pairsS = table.toString();
        String out = "SymmetryDefinition:\nSymmetry=" + internalSymmetryFactor + "\n" +
                pairsS + "\n" + metaS + "\n";
        return out;
    }
    public Double getInternalSymmetryFactor() {
        return internalSymmetryFactor;
    }

    public void setInternalSymmetryFactor(Double internalSymmetryFactor) {
        this.internalSymmetryFactor = internalSymmetryFactor;
    }

    public SetOfSymmetryAssignments getSymmetryAssignmentsTable() {
        return table;
    }

}

