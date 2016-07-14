/*
 */

package jThergas.exceptions;

/** Exception associated with reading in Benson groups
 *
 * @author blurock
 */
public class JThergasNotAGroupElement extends JThergasReadException {
    /** Constructor
     *
     * @param error The associated error message
     */
    public JThergasNotAGroupElement(String error) {
        super(error);
    }
    
}
