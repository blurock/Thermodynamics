/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author edwardblurock
 */
public class SetOfNASAPolynomials extends ArrayList<NASAPolynomial> {

    public SetOfNASAPolynomials() {
    }

    void parse(String setofpolynomials) throws IOException {
        StringTokenizer tok = new StringTokenizer(setofpolynomials,"\n");
        while(tok.countTokens() > 4) {
            NASAPolynomial nasa = new NASAPolynomial();
            nasa.parse(tok);
            this.add(nasa);
        }
    }

}
