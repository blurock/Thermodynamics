/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure;

/**
 *
 * @author blurock
 */
public class StructureType {
    private String nameOfStructure;
    private String typeOfStructure;

    public StructureType(String name, String type) {
        nameOfStructure = name;
        typeOfStructure = type;
        
    }
    public StructureType() {
        nameOfStructure = "";
        typeOfStructure = "";
    }
    
    public String getTypeOfStructure() {
        return typeOfStructure;
    }

    public void setTypeOfStructure(String typeOfStructure) {
        this.typeOfStructure = typeOfStructure;
    }
    public String getNameOfStructure() {
        return nameOfStructure;
    }

    public void setNameOfStructure(String nameOfStructure) {
        this.nameOfStructure = nameOfStructure;
    }
    public String writeAsString() {
        StringBuffer buf = new StringBuffer();
        
        buf.append("ElementName: '" + nameOfStructure + "'");
        buf.append("Type: '" + typeOfStructure + "'");
        
        return buf.toString();
    }

}
