/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.CML;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLScalar;

/**
 *
 * @author blurock
 */
public class CMLNameList extends CMLAbstractThermo {

    private String[] nameList;
    
    public CMLNameList(String[] names) {
        nameList = names;
    }
    public CMLNameList(HashSet<String> vec) {
            nameList = new String[vec.size()];
            Iterator<String> iter = vec.iterator();
            int i=0;
            while(iter.hasNext()) {
                nameList[i++] = iter.next();
            }
    }
    public CMLNameList(List<String> vec) {
            nameList = new String[vec.size()];
            Iterator<String> iter = vec.iterator();
            int i=0;
            while(iter.hasNext()) {
                nameList[i++] = iter.next();
            }
    }
     public CMLNameList() {
    }
   
    @Override
    public void toCML() {
        for (int i = 0; i < nameList.length; i++) {
            CMLScalar name = new CMLScalar();
            name.setId(String.valueOf(i));
                name.setValue(nameList[i]);
                this.appendChild(name);

                //Logger.getLogger(CMLNameList.class.getName()).log(Level.INFO, nameList[i]);
            }
    }

    @Override
    public void fromCML() {
        List<CMLElement> proplist = this.getChildCMLElements();
        nameList = new String[proplist.size()];
        for(int i=0;i<proplist.size();i++) {
            CMLScalar name = (CMLScalar) proplist.get(i);
            nameList[i] = name.getStringContent();
        }
    
    }

    public

    String[] getNameList() {
        return nameList;
    }

    public void setNameList(String[] nameList) {
        this.nameList = nameList;
    }

}
