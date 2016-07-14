/*
 */

package jThergas.exceptions;

/** The base exceptin for the JThergas package
 *
 * @author blurock
 */
public class JThergasException extends Exception {
    
    /** The constructor
     *
     * @param error The error message associated with the exception
     */
    public JThergasException(String error) {
        super(error);
    }

}
