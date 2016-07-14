/*
 */
package jThergas.data.group;

import jThergas.data.JThermgasThermoStructureDataPoint;
import jThergas.data.read.JThergasReadStructureThermo;

/** This overides the
 * {@link JThergasReadGroupStructureThermo#getNewThermoDataPoint() getNewThermoDataPoint()}
 * in {@link JThergasReadGroupStructureThermo}
 * to give back {@link jThergas.data.read.JThergasReadStructureThermo }
 *
 * @author blurock
 */
public class JThergasReadGroupStructureThermo extends JThergasReadStructureThermo {

    /** the empty constructor
     *
     */
    public JThergasReadGroupStructureThermo() {
    }

    @Override
    public JThermgasThermoStructureDataPoint getNewThermoDataPoint() {
        return new JThergasThermoStructureGroupPoint();
    }
}
