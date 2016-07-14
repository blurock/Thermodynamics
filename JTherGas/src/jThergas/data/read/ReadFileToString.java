/*
 * ReadFileToString.java
 *
 * Created on October 24, 2002, 2:53 PM
 */

package jThergas.data.read;
import java.io.*;
import jThergas.exceptions.ErrorFrame;

/**
 *
 * @author  reaction
 */
public class ReadFileToString {
    public String outputString = null;
    /** Creates a new instance of ReadFileToString */
    public ReadFileToString() {
    }
    public void read(File f) {
      int bt;
      try {
        FileReader reader = new FileReader(f);
        BufferedReader breader = new BufferedReader(reader);
        read(breader);
        breader.close();
        reader.close();
        } catch(FileNotFoundException io) {
            ErrorFrame fr = new ErrorFrame("File not found: \n" + f.toString());
            fr.show();
            outputString = null;
        } catch(IOException io) {
            ErrorFrame fr = new ErrorFrame("Error while reading file: " +f.toString());
            fr.show();
            outputString = null;
        }
    }
  public void read(BufferedReader reader) throws IOException {
     boolean endofline = true;
     StringBuffer tLine         = new StringBuffer();
           String line = reader.readLine();
           while(line != null) {
           endofline = true;
               if(!line.startsWith("%")) {
                   int index = line.indexOf('%');
                   if(index > 0) {
                       tLine.append(line.substring(0,index));
                   } else {
                       int lastcharindex = line.length() - 1;
                       if(line.lastIndexOf("\\",lastcharindex) > 0) {
                           endofline = false;
                       } else {
                          tLine.append(line);
                       }
                   }
               }
               if(endofline) {
                   tLine.append("\n");
               }
            line = reader.readLine();
           }
        outputString = tLine.toString();
  }    
}
