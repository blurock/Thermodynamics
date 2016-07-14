/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import thermo.build.ReadThermodynamicsFromExGas;
import thermo.compute.CompareThermodynamicInformationSets;
import thermo.compute.ComputeThermoFromNancyStringFile;
import thermo.compute.SetOfThermodynamicDifferences;
import thermo.data.benson.SetOfThermodynamicInformation;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;

/**
 *
 * @author edwardblurock
 */
public class CompareWithCorrsAndExGas {
    private ReadThermodynamicsFromExGas readTherm;
    private ComputeThermoFromNancyStringFile computeNancy;
    private CompareThermodynamicInformationSets compareThermo;

    SetOfThermodynamicInformation exgasThermo;
    SetOfThermodynamicInformation jthergasThermo;

    /**
     *
     */
    public CompareWithCorrsAndExGas() throws ThermodynamicComputeException {
        readTherm = new ReadThermodynamicsFromExGas();
        compareThermo = new CompareThermodynamicInformationSets();
        computeNancy = new ComputeThermoFromNancyStringFile();
    }
    
    /**
     * 
     * @param corrsF Correlation file (nancy form, molecule name)
     * @param thermoF inside a CHEMKIN file (searches for thermo)
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ThermodynamicComputeException
     */
    public void compare(File corrsF, File thermoF) throws IOException, FileNotFoundException, ThermodynamicComputeException{
        exgasThermo = readTherm.read(thermoF);
        jthergasThermo = computeNancy.computeFromFile(corrsF);

        HashMap<String,ThermodynamicInformation> map = createHashTable();
        SetOfThermodynamicInformation set = buildSet(map);

        System.out.println(set.toString());
        System.out.println(jthergasThermo.toString());

        SetOfThermodynamicDifferences diff = compareThermo.computeDifference(jthergasThermo, set);
        List difflist = diff;
        Collections.sort( difflist);

        System.out.println(difflist.toString());
    }

    private HashMap<String,ThermodynamicInformation> createHashTable() {
         HashMap<String,ThermodynamicInformation> map = new HashMap<String,ThermodynamicInformation>();
         Iterator<ThermodynamicInformation> iter = exgasThermo.iterator();
         while(iter.hasNext()) {
             ThermodynamicInformation thermo = iter.next();
             map.put(thermo.getName(), thermo);
         }
        return map;
    }
    private SetOfThermodynamicInformation buildSet(HashMap<String,ThermodynamicInformation> map) {
        SetOfThermodynamicInformation set = new SetOfThermodynamicInformation("EXGAS");
        Iterator<ThermodynamicInformation> iter = jthergasThermo.iterator();
        while(iter.hasNext()) {
            ThermodynamicInformation thermo = iter.next();
            ThermodynamicInformation exthermo = map.get(thermo.getName());
            set.add(exthermo);
        }
        return set;
    }
    /**
     * 
     */
    public void buildFromCorrsFile() {

        
    }
}
