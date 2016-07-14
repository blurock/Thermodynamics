package jThergas.main;


import jThergas.data.group.JThergasReadGroupStructureThermo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jThergas.exceptions.JThergasReadException;

/**
 *
 * @author blurock
 */
public class JThergasReadTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fileS = args[0];
        JThergasReadGroupStructureThermo read = new JThergasReadGroupStructureThermo();
        
        try {
        File file = new File(fileS);
        read.readAndParse(file); 
        } catch (JThergasReadException ex) {
            Logger.getLogger(JThergasReadTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JThergasReadTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JThergasReadTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
