/*
 */

package jThergas.exceptions;

/** Exception associated with reading in data
 * Two elements are associated with this exception:
 * <ul>
 * <li> The line number where the error occured
 * <li> The file name where the error occured
 * </ul>
 *
 * @author blurock
 */
public class JThergasReadException extends JThergasException {
    
   int lineNumber = -1;
   String fileName = "unknown";

   /** Empty constructor
    *
    */
   public JThergasReadException() {
        super("Error");
    }
   
   /** Constructor with error message
    *
    * @param error The error message
    */
   public JThergasReadException(String error) {
       super(error);
   }
   /** Assign the line number where the error occured
    *
    * @param n
    */
   public void assignLineNumber(int n) {
       lineNumber = n;
   }
   /** Assigne the filename where the error occured
    *
    * @param name
    */
   public void assignFileName(String name) {
       fileName = name;
   }
   /** Write the exception (with the line number and file)
    *
    * @return The string containing the error message
    */
   public String writeToString() {
       String out = "Error in file: " + fileName + "\n"
               +    "at Line Number: " + lineNumber + "\n" 
               + this.toString();
       return out;
   }

}
