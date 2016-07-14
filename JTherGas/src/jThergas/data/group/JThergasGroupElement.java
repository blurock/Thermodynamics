/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jThergas.data.group;

import jThergas.exceptions.JThergasNotAGroupElement;

/**
 *
 * @author blurock
 */
public class JThergasGroupElement {
    private String groupElementName;
    private int numberOfElements;

    /* parse a single group element
     *
     * The group name begins with a left parenthesis
     * and ends with a right parens.
     * After the group name is the multiplicity of the group.
     * There are three cases:
     * <ul>
     * <li> No multiplicity, so number of elements is one
     * <li> The character after the parens is a slash, followed by the number of elements
     * <li> The number of elements follows directly
     * </ul>
     *
     *
     * @param elementS The group as string
     * @throws jthergas.exceptions.JThergasNotAGroupElement
     */
    public void parse(String elementS) throws JThergasNotAGroupElement, NumberFormatException {
        if (elementS.substring(0, 1).equals("(")) {
            int index = elementS.indexOf(')');
            if (index > 0) {
                groupElementName = elementS.substring(1, index);
                if (index + 1 < elementS.length()) {
                    if (elementS.substring(index + 1, index + 2).equals("/")) {
                        String numberOfElementsS = elementS.substring(index + 2).trim();
                        numberOfElements = Integer.parseInt(numberOfElementsS);
                    } else {
                        String numberOfElementsS = elementS.substring(index + 1).trim();
                        numberOfElements = Integer.parseInt(numberOfElementsS);
                    }
                } else {
                    numberOfElements = 1;
                }
            } else {
                throw new JThergasNotAGroupElement("No matching end parens found: " + elementS);
            }
        } else {
            throw new JThergasNotAGroupElement("Expecting begin parens as first character: " + elementS);
        }
    }

    /** Get the group element name
     *
     * @return the group element name
     */
    public String getGroupElementName() {
        return groupElementName;
    }

    /** Get the multiplicity of the group
     *
     * @return the multiplicity of the group
     */
    public int getNumberOfElements() {
        return numberOfElements;
    }
}
