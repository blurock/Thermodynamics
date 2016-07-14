package thermo.test;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jThergas.exceptions.JThergasReadException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import thermo.initialize.InitializeThermodynamicCalculator;

/**
 *
 * @author blurock
 */
public class TestBuild {
    //static SProperties properties = new SProperties();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InitializeThermodynamicCalculator init = new InitializeThermodynamicCalculator();
        try {

            init.initializeThermodynamicCalculator();
        } catch (SQLException ex) {
            Logger.getLogger(TestBuild.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JThergasReadException ex) {
            Logger.getLogger(TestBuild.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestBuild.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestBuild.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
