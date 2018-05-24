/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.properties;

import jThergas.data.read.ReadFileToString;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author blurock
 */
public class SProperties {

    protected static Properties properties = new Properties();
    protected static Vector listeners = new Vector();


    static {
        setProperty("thermo.data.bensonstandard.temperatures", "300,400,500,600,800,1000,1500");
        setProperty("thermo.database.vibrationalstructure","VibrationalStructures");
        setProperty("thermo.data.vibrationcorrections","thermo.resources.ContributionsToCpDueToFrequencyAndTemperature.csv");
        setProperty("thermo.database.bensongroup.resourcefile","/thermo/resources/Groups.input");
        setProperty("thermo.database.connection","jdbc.mysql://127.0.0.1:3306/Thermodynamics");
        setProperty("thermo.data.bensonstandard.temperatures","300,400,500,600,800,1000,1500");
        setProperty("thermo.types.name.thergasbenson","StandardThergasBenson");
        setProperty("thermo.data.gasconstant.clasmolsk","1.98587755");
        setProperty("thermo.database.connection","jdbc:mysql://google/thermodynamics?cloudSqlInstance=kubernetesdemo-162512:europe-west1:thermodynamics&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false");
        //    setProperty("thermo.database.connection","jdbc.mysql://35.205.162.227:3306/thermodynamics");
        setProperty("thermo.database.dbuser","root");
        setProperty("thermo.database.dbpassword","laguna");
        load();
        //loadOnClassPath();
        //System.out.println("\nSQL: " + getProperty("thermo.database.dbuser") + "\t" + getProperty("thermo.database.dbpassword"));
    }

    public static void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.add(pcl);
    }

    public static void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.remove(pcl);
    }

    public static String setProperty(String name, String value) {
        String old = (String) properties.setProperty(name, value);

        PropertyChangeEvent pce = new PropertyChangeEvent(new Object(), name, old, value);
        for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
            ((PropertyChangeListener) e.nextElement()).propertyChange(pce);
        }

        return old;
    }

    public static String getProperty(String name) {
        return properties.getProperty(name);
    }

    public static void save() {
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream("Jthermodyanamic.properties");
            properties.store(fos, "Thermodynamic properties");
        } catch (java.io.IOException e) {
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.WARNING, "Failed to save settings." + e.toString());
        }

    }

    public static void load() {
        try {
           String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
 String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" +currentDir);
            java.io.FileInputStream fis = new java.io.FileInputStream("Jthermodyanamic.properties");
            properties.load(fis);
        } catch (FileNotFoundException ex) {
            Logger.getLogger("global").log(Level.SEVERE, "Properties file not found: " + ex.toString());
        } catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, "Exception on reading the properties file" + ex.toString());
        }
    }

    public static void loadOnClassPath() {
        try {
            System.out.println("SProperties: get resource");
            java.io.InputStream in = properties.getClass().getResourceAsStream("Jthermodyanamic.properties");
            if(in == null) {
                System.out.println("The stream is null: with just name");
            } else {
                properties.load(in);
            }
        } catch (java.io.IOException e) {
            Logger.getLogger("global").log(Level.SEVERE, "Exception on reading the properties file" + e.toString());
        }
    }
    public static InputStream getResourceAsStream(String resource) {
        return properties.getClass().getResourceAsStream(resource);
    }
    public static String getResourceAsString(String resourcename) throws IOException {
        String resource = SProperties.getProperty(resourcename);
        InputStream inp = getResourceAsStream(resource);
        InputStreamReader inpreader = new InputStreamReader(inp);
        ReadFileToString file2string = new ReadFileToString();
        BufferedReader reader = new BufferedReader(inpreader);
        file2string.read(reader);
        return file2string.outputString;
    }
}
