/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.vibrational;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.HeatCapacityTemperaturePair;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.structure.AddHydrogenToSingleRadical;
import thermo.exception.NotARadicalException;
import thermo.properties.SProperties;

/** CalculateVibrationalCorrectionForRadical
 *
 * This calculates the vibrational correction for radicals
 * The difference in vibrational modes are compared between
 * (R.) and (RH). For each frequency, a given correction
 * (given by {@link FrequencyCorrection}), corrected by the
 * number of matches and the symmetry of the vibrational group,
 * is made for the entropy and heat capacities (no correction for
 * enthalpy).
 *
 * @author blurock
 * @version 2009
 */
public class CalculateVibrationalCorrectionForRadical {
    ThermoSQLConnection connect;
    SubstituteVibrationalStructures substitute;
    FrequencyCorrection frequencyCorrection;
    double standardTemperature = 298.0;
    String referenceRoot = "Correction: ";
    String typeS = "Correction";

    /**
     *
     * @param c
     * @throws SQLException
     */
    public CalculateVibrationalCorrectionForRadical(ThermoSQLConnection c) throws SQLException {
        connect= c;
        substitute = new SubstituteVibrationalStructures(connect);
        frequencyCorrection = new FrequencyCorrection();
    }
    
    /** Calculate vibrational corrections for radicals
     *
     * this calls calculate({@link Molecule} mol, {@link SetOfBensonThermodynamicBase} set)
     * with an initialized {@link SetOfBensonThermodynamicBase}
     *
     * @param mol The radical molecule
     * @return The vector of corrections
     * @throws NotARadicalException
     * @throws SQLException
     * @throws CDKException
     * @throws IOException
     */
    public SetOfBensonThermodynamicBase calculate(Molecule mol) throws NotARadicalException, SQLException, CDKException, IOException {
        SetOfBensonThermodynamicBase corrections = new SetOfBensonThermodynamicBase();
        return calculate(mol,corrections);
    }
    /** Calculate vibrational corrections for radicals
     *
     *
     * <ul>
     * <li> Substitute the radical, R, with hydrogen to form RH using
     *      {@link AddHydrogenToSingleRadical}
     * <li> Determine the vibrational substitutions in both R and RH
     *       using {@link SetOfVibrationalStructureCounts}
     * <li> Find the vibrational differences between R and RH
     *         subtract method in {@link SetOfVibrationalStructureCounts}
     * <li> The factor of the correction, for each mode, is the difference
     *         in matches divided by the symmetry of the vibrational group
     * <li> The standard entropy correction is made through {@link FrequencyCorrection}
     * <li> For each temperature in "thermo.data.bensonstandard.temperatures"
     *         (from {@link SProperties}) the heat capacity correction is made
     *         again using {@link FrequencyCorrection}
     * </ul>
     *
     * For each vibrational correction, a {@link BensonThermodynamicBase}
     * is made and collected in {@link SetOfBensonThermodynamicBase}
     *
     * @param mol The radical molecule
     * @param corrections Add these corrections to set of other corrections
     * @return  The vector of corrections
     * @throws NotARadicalException The original molecule is not a radical
     * @throws SQLException
     * @throws CDKException
     * @throws IOException
     */
    public SetOfBensonThermodynamicBase calculate(Molecule mol,SetOfBensonThermodynamicBase corrections) throws NotARadicalException, SQLException, CDKException, IOException {
        AddHydrogenToSingleRadical formRH = new AddHydrogenToSingleRadical();
        Molecule RH = formRH.convert(mol);
        return calculate(mol,RH,corrections);
        }

        public SetOfBensonThermodynamicBase calculate(Molecule mol,Molecule RH, SetOfBensonThermodynamicBase corrections) throws NotARadicalException, SQLException, CDKException, IOException {

            //System.out.println(substitute.toString());
            System.out.println("-----------------------------------------------------------");
            SetOfVibrationalStructureCounts counts = substitute.findSubstitutions(RH);
            System.out.println("Vibrations in RH");
            System.out.println(counts.toString());
            System.out.println("-----------------------------------------------------------");
            SetOfVibrationalStructureCounts countsR = substitute.findSubstitutions(mol);
            System.out.println("Vibrations in Radical R");
            System.out.println(countsR.toString());
            System.out.println("-----------------------------------------------------------");

            counts.subtract(countsR);
            System.out.println("Difference in Vibrational modes");
            System.out.println(counts.toString());
            
            Iterator<VibrationalStructureInfoCount> iter = counts.iterator();
            while(iter.hasNext()) {
                VibrationalStructureInfoCount count = iter.next();
                double frequency = count.getFrequency();
                double symmetry = count.getSymmetry();
                double matches = (double) count.countMatches;
                double factor = matches/symmetry;
                String reference = referenceRoot + "Frequency:" + frequency;
                double entropyC = -frequencyCorrection.correctCpInCalories(frequency, standardTemperature)*factor;
                HashSet<HeatCapacityTemperaturePair> pairs = heatCapacityPairs(reference);
                Iterator<HeatCapacityTemperaturePair> capacityiter = pairs.iterator();
                while(capacityiter.hasNext()){
                    HeatCapacityTemperaturePair pair = capacityiter.next();
                    double fcorrection = -frequencyCorrection.correctCpInCalories(frequency, pair.getTemperatureValue());
                    pair.setHeatCapacityValue(fcorrection*factor);
                }
                String correctionS = referenceRoot + " " + count.getElementName() + ": " + frequency + "\t Count=" + matches;
                BensonThermodynamicBase benson = new BensonThermodynamicBase(typeS, pairs, 0.0, entropyC);
                benson.setReference(correctionS);
                corrections.add(benson);
            }

        return corrections;
    }
    private HashSet<HeatCapacityTemperaturePair> heatCapacityPairs(String referenceS) throws IOException {
        HashSet<HeatCapacityTemperaturePair> pairs = new HashSet<HeatCapacityTemperaturePair>();
        String temperature = SProperties.getProperty("thermo.data.bensonstandard.temperatures");
        StringTokenizer tok = new StringTokenizer(temperature,",");
        while(tok.hasMoreElements()) {
            String tempS = tok.nextToken();
            Double tempD = Double.valueOf(tempS);
            HeatCapacityTemperaturePair pair = new HeatCapacityTemperaturePair();
            pair.setReference(referenceS);
            pair.setTemperatureValue(tempD.doubleValue());
            pairs.add(pair);
        }
        return pairs;
    }


}
