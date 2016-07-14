/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.ParsingException;
import thermo.data.benson.BensonConnectAtomStructure;
import thermo.data.benson.CML.CMLBensonConnectAtomStructure;

/**
 *
 * @author blurock
 */
public class TestCML {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BensonConnectAtomStructure structure = new BensonConnectAtomStructure("C",3);
        
        CMLBensonConnectAtomStructure cml = new CMLBensonConnectAtomStructure();
        cml.setStructure(structure);
        
        String out = cml.restore();
        System.out.println(out);
        try {
            cml.parse(out);
        } catch (ParsingException ex) {
            Logger.getLogger(TestCML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestCML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
