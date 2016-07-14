/*
 */
package thermo.data.benson.thergas;

import jThergas.data.JThermgasThermoStructureDataPoint;
import jThergas.data.group.JThergasCenterAtom;
import jThergas.data.group.JThergasGroupElement;
import jThergas.data.group.JThergasThermoStructureGroupPoint;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.Vector;
import jThergas.data.structure.JThergasStructureData;
import jThergas.data.thermo.JThergasThermoData;
import java.util.HashSet;
import java.util.Iterator;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.BensonConnectAtomStructure;
import thermo.data.benson.BensonGroupStructure;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.HeatCapacityTemperaturePair;
import thermo.data.benson.SetOfMetaAtoms;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.properties.SProperties;

/**
 *
 * @author blurock
 */
public class BuildBensonThermodynamicFromThergas {

    private SetOfMetaAtoms setOfMetaAtoms;
    /**
     * The created thermodyanamic structure
     */
    private ThermodynamicInformation thermoStructure;


    /**
     *
     */
    public BuildBensonThermodynamicFromThergas() {
        buildThermodynamicInformation();
        setOfMetaAtoms = new SetOfMetaAtoms();

    }

    private void buildThermodynamicInformation() {
        thermoStructure = new BensonThermodynamicBase();
    }

    /** Build the {@link BensonGroupStructure} from the {@link JThergasThermoStructureGroupPoint Thergas information}
     *
     * The Thergas thermodynamic information is read into the {@link JThergasThermoStructureGroupPoint} class.
     * This method converts this to a {@link BensonGroupStructure}.
     *
     * In addition, the set of meta atoms is accumulated within this class.
     *
     * <ul>
     * <li> Initial {@link BensonGroupStructure} with name of center atom.
     * </ul>
     * 
     * @param thergasgrp
     * @return
     */
    public BensonGroupStructure buildBensonGroupStructure(JThergasThermoStructureGroupPoint thergasgrp) {


        JThergasCenterAtom center = thergasgrp.getCenterAtomType();
        BensonGroupStructure grp = null;
        if (center != null) {
            BensonThermodynamicBase thermo = (BensonThermodynamicBase) thermoStructure;
            JThergasStructureData structure = thergasgrp.getStructure();
            getSetOfMetaAtoms().addIfNotInSet(center.getCenterAtomAsString());
            String name = structure.getNancyLinearForm().trim();
            thermo.setID(name);


            System.out.println("Build Thermo: name '" + thermo.getID() + "'   center atom: " + center.getCenterAtomAsString() + "'");
            grp = new BensonGroupStructure(name);
            grp.setCenterAtomS(center.getCenterAtomAsString());
            HashSet<JThergasGroupElement> groupElements = thergasgrp.getGroupElements();
            Iterator<JThergasGroupElement> iter = groupElements.iterator();
            while(iter.hasNext()) {
                JThergasGroupElement element = iter.next();
                getSetOfMetaAtoms().addIfNotInSet(element.getGroupElementName());
                System.out.print(element.getGroupElementName() + " (" + element.getNumberOfElements() + "),\t");
                BensonConnectAtomStructure con = new BensonConnectAtomStructure(element.getGroupElementName(), element.getNumberOfElements());
                grp.addBondedAtom(con);                
            }
        } else {
            System.out.println("Not a group");
        }
        return grp;
    }

    public Molecule buildMolecule(JThermgasThermoStructureDataPoint thergasbase, ThermoSQLConnection c) throws CDKException, SQLException {
        JThergasStructureData structure = thergasbase.getStructure();
        String linearform = structure.getNancyLinearForm();
        NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(c);
        Molecule mol = nancy.convert(linearform);
        String name = thergasbase.getStructure().getNancyLinearForm().trim();
        if(thergasbase.getStructure().getNameOfStructure() != null) {
            name = thergasbase.getStructure().getNameOfStructure().trim();
            if(name.length() == 0) {
                name = thergasbase.getStructure().getNancyLinearForm().trim();
            }
        }
        mol.setID(name);
        return mol;
    }
    /**
     *
     * @param thergasgrp
     * @param reference
     * @return
     */
    public BensonThermodynamicBase buildBensonThermodynamicBase(JThermgasThermoStructureDataPoint thergasgrp, String reference) {
        BensonThermodynamicBase thermo = new BensonThermodynamicBase();
        String name = thergasgrp.getStructure().getNancyLinearForm().trim();
        if(thergasgrp.getStructure().getNameOfStructure() != null) {
            name = thergasgrp.getStructure().getNameOfStructure().trim();
            if(name.length() == 0) {
                name = thergasgrp.getStructure().getNancyLinearForm().trim();
            }
        }
        thermo.setID(name);
        JThergasThermoData data = thergasgrp.getThermodynamics();
        double cp[] = data.getCpValues();
        Double enthalpy = new Double(data.getStandardEnthalpy());
        Double entropy = new Double(data.getStandardEntropy());

        thermo.setReference(reference);
        thermo.setStandardEnthalpy(enthalpy);
        thermo.setStandardEntropy(entropy);
        HashSet<HeatCapacityTemperaturePair> cpset = new HashSet<HeatCapacityTemperaturePair>();
        HashSet temps = new HashSet();
        String tempS = SProperties.getProperty("thermo.data.bensonstandard.temperatures");
        StringTokenizer tok = new StringTokenizer(tempS, ",");

        for (int i = 0; i < cp.length; i++) {
            try {
                if (cp[i] != 0.0) {
                    HeatCapacityTemperaturePair pair = new HeatCapacityTemperaturePair();

                    String tS = tok.nextToken();
                    double temp = Double.parseDouble(tS);
                    pair.setTemperatureValue(temp);

                    pair.setHeatCapacityValue(cp[i]);

                    pair.setStructureName(name);
                    pair.setReference(reference);
                    cpset.add(pair);
                }
            } catch (java.lang.NumberFormatException ex) {
                //pair.setTemperatureValue(0.0);
            }
        }
        thermo.setSetOfHeatCapacities(cpset);


        return thermo;
    }

    /**
     *
     * @return
     */
    public ThermodynamicInformation getThermoStructure() {
        return thermoStructure;
    }

    /**
     *
     * @return
     */
    public SetOfMetaAtoms getSetOfMetaAtoms() {
        return setOfMetaAtoms;
    }
}
