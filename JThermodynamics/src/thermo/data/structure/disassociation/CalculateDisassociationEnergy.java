/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.disassociation;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.structure.disassociation.DB.SQLDisassociationEnergy;
import thermo.data.structure.substructure.FindSubstructure;

/**
 *
 * @author edwardblurock
 */
public class CalculateDisassociationEnergy {
    ThermoSQLConnection connect;
    String disassociationS = "DisassociationEnergy";

    public CalculateDisassociationEnergy(ThermoSQLConnection connect) {
        this.connect = connect;
    }

    public void calculate(AtomContainer mol, SetOfBensonThermodynamicBase corrections) throws SQLException, CDKException {
        BensonThermodynamicBase thermo = (BensonThermodynamicBase) calculate(mol);
        corrections.add(thermo);
    }
    public ThermodynamicInformation calculate(AtomContainer mol) throws SQLException, CDKException {
        Double entropyD = new Double(0.0);

        DisassociationEnergy energy = getDisassociationEnergyForMolecule(mol);
        Double energyD = energy.getDisassociationEnergy();
        BensonThermodynamicBase thermo = new BensonThermodynamicBase(disassociationS,null,energyD,entropyD);
        String referenceS = "Disassociation Energy From: " + energy.getSubstructure().getID();
        thermo.setReference(referenceS);
        return thermo;
    }

    public Double calculateDisassociationEnergy(AtomContainer mol) throws SQLException, CDKException {
        DisassociationEnergy energy = getDisassociationEnergyForMolecule(mol);
        return energy.getDisassociationEnergy();
    }
    public DisassociationEnergy getDisassociationEnergyForMolecule(AtomContainer mol) throws SQLException, CDKException {
       DisassociationEnergy energy = null;
       FindSubstructure find = new FindSubstructure(mol, connect);
       SQLDisassociationEnergy sqldiss = new SQLDisassociationEnergy(connect);
       List<String> names = sqldiss.listOfDisassociationStructures();
       System.out.println("getDisassociationEnergyForMolecule: Number of candidate" + names.size());
       String name = find.findLargestSubstructure(names);
       HashSet energyV = sqldiss.retrieveStructuresFromDatabase(name);
       if(energyV.size() == 1) {
           Iterator<DisassociationEnergy> iter = energyV.iterator();
           energy = iter.next();
       } else if(energyV.size() == 0) {
           throw new CDKException("Disassociation Energy for " + mol.getID() + " could not be found");
       } else {
           String error = "ERROR in Database: "
                   + energyV.size()
                   + " structures found for single disassociation structure "
                   + name;
           throw new CDKException(error);
       }
      return energy;
    }

}
