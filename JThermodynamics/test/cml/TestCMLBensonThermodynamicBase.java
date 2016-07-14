/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cml;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.CML.CMLSetOfBensonThermodynamicBase;
import thermo.data.benson.HeatCapacityTemperaturePair;
import thermo.data.benson.SetOfBensonThermodynamicBase;

/**
 *
 * @author edwardblurock
 */
public class TestCMLBensonThermodynamicBase {

    public TestCMLBensonThermodynamicBase() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void setCMLSetOfBensonThermodynamicBase() {
        HashSet<HeatCapacityTemperaturePair> cp1 = new HashSet<HeatCapacityTemperaturePair>();
        HashSet<HeatCapacityTemperaturePair> cp2 = new HashSet<HeatCapacityTemperaturePair>();
        double[] temps = {300.0, 500.0, 600.0, 800.0, 1000.0, 1200.0};
        double[] cpD1 = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        double[] cpD2 = {1.5, 2.5, 3.5, 4.5, 5.5, 6.5};
        for(int i=0; i<temps.length;i++) {
            String n1 = Double.toString(temps[i]);
            String n2 = Double.toString(temps[i]);
            HeatCapacityTemperaturePair p2
                    = new HeatCapacityTemperaturePair(n1,temps[i],cpD1[i]);
            HeatCapacityTemperaturePair p1
                    = new HeatCapacityTemperaturePair(n2,temps[i],cpD1[i]);
            cp1.add(p1);
            cp2.add(p2);
        }
        String name1 = "ex1";
        String name2 = "ex2";

        BensonThermodynamicBase base1
                = new BensonThermodynamicBase(name1,cp1,
                             new Double(10.0), new Double(20.0));
        BensonThermodynamicBase base2
                = new BensonThermodynamicBase(name2,cp2,
                             new Double(20.0), new Double(40.0));
        SetOfBensonThermodynamicBase set = new SetOfBensonThermodynamicBase();
        set.add(base1);
        set.add(base2);


        CMLSetOfBensonThermodynamicBase cmlset1 = new CMLSetOfBensonThermodynamicBase();
        cmlset1.setStructure(set);
        String cml1S = cmlset1.restore();
        System.out.println(cml1S);

        CMLSetOfBensonThermodynamicBase cmlset2 = new CMLSetOfBensonThermodynamicBase();
        try {
            System.out.println(cml1S);
            cmlset2.parse(cml1S);
            SetOfBensonThermodynamicBase cmlset = (SetOfBensonThermodynamicBase) cmlset2.getStructure();
            System.out.println(cmlset.toString());
        } catch (ValidityException ex) {
            Logger.getLogger(TestCMLBensonThermodynamicBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParsingException ex) {
            Logger.getLogger(TestCMLBensonThermodynamicBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestCMLBensonThermodynamicBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}