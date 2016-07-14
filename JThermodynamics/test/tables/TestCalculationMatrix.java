/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tables;

import jThergas.data.read.ReadFileToString;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.tables.CalculationMatrix;
import thermo.data.tables.DB.SQLCalculationMatrix;
import thermo.data.tables.NumericColumn;

/**
 *
 * @author edwardblurock
 */
public class TestCalculationMatrix {

    public TestCalculationMatrix() {
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
    public void TestFunction() {
        NumericColumn col1 = new NumericColumn();
        NumericColumn col2 = new NumericColumn();
        NumericColumn col3 = new NumericColumn();
        CalculationMatrix matrix = new CalculationMatrix();

        matrix.addColumn("1.0  2.0  3.0");
        matrix.addColumn("1.0  4.0  9.0");
        matrix.addColumn("1.0  8.0 27.0");

        matrix.addRowHeader("10.0  20.0  30.0");
        matrix.addColumnHeader("1.0 2.0 3.0");
        matrix.setDescription("Test Calcultion Matrix");
        matrix.setNameOfData("Test");


        System.out.println(matrix.toString());

        System.out.println("F(25.0,1.5) = " + matrix.getFunctionalValue(25.0, 1.5));
        System.out.println("F(25.0,2.5) = " + matrix.getFunctionalValue(25.0, 2.5));
        System.out.println("F(15.0,1.5) = " + matrix.getFunctionalValue(15.0, 1.5));
        System.out.println("F(15.0,2.5) = " + matrix.getFunctionalValue(15.0, 2.5));
        
    }
    @Test
    public void readTable() {
        FileInputStream fileR = null;
        String fileS = "TableA16.don";
        File fileF = new File(fileS);
        ReadFileToString read = new ReadFileToString();
        read.read(fileF);
        String matrixS = read.outputString;

        CalculationMatrix matrix = new CalculationMatrix();
        matrix.readCalculationMatrixFromString(matrixS);

        System.out.println(matrix.toString());
    }
    @Test
    public void sqlTable() {
         FileInputStream fileR = null;
        String fileS = "TableA16.don";
        File fileF = new File(fileS);
        ReadFileToString read = new ReadFileToString();
        read.read(fileF);
        String matrixS = read.outputString;

        CalculationMatrix matrix = new CalculationMatrix();
        matrix.readCalculationMatrixFromString(matrixS);

        System.out.println(matrix.toString());

        ThermoSQLConnection connect = new ThermoSQLConnection();
        connect.connect();
        SQLCalculationMatrix sqlmatrix = new SQLCalculationMatrix(connect);
        try {
            sqlmatrix.addToDatabase(matrix);

            HashSet vec = sqlmatrix.retrieveStructuresFromDatabase(matrix.getNameOfData());
            Iterator<CalculationMatrix> iter = vec.iterator();
            CalculationMatrix mat = iter.next();
            System.out.println(mat.toString());

            sqlmatrix.deleteByKey(matrix.getNameOfData());
        } catch (SQLException ex) {
            Logger.getLogger(TestCalculationMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}