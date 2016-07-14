/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * this class 
 *
 * @author blurock
 */
public class NameListSet {
    private String setName;
    List<String> groupNames;
    public NameListSet(String name) {
        setName = name;
        groupNames = new ArrayList<String>();
    }
    public NameListSet(String name, String[] names) {
        setName = name;
        groupNames = new ArrayList<String>();
        for(int i=0;i<names.length;i++) {
            groupNames.add(names[i]);
        }
    }
    
    public boolean add(String group) {
        boolean success = true;
        if(!groupNames.contains(group)) {
            groupNames.add(group);
        } else {
            success = false;
        }
        return success;
    }
    public boolean remove(String group) {
        return groupNames.remove(group);
    }
    public String[] toArray() {
        String[] names = new String[groupNames.size()];
        return (String[]) groupNames.toArray(names);
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(setName);
        buf.append(":\t");
        Iterator<String> igroup = groupNames.iterator();
        while(igroup.hasNext()) {
            String group = igroup.next();
            buf.append("'");
            buf.append(group);
            buf.append("'\t");            
        }
        return buf.toString();
    }

    boolean equals(NameListSet set) {
        boolean ans = false;
        if(set.getSetName().equals(this.getSetName())) {
            String[] names = set.toArray();
            int count = 0;
            boolean found = true;
            while(found && count < names.length) {
                found = this.groupNames.contains(names[count]);
                count++;
            }
        }
        
        return ans;
    }
    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }
}
