/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.CML;

import java.io.IOException;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLProperty;


/**
 *
 * @author blurock
 */
public abstract class CMLAbstractThermo extends CMLProperty {
    
    private Object structure;
    
    public CMLAbstractThermo() {
        
    }
    
    public void parse(String data) throws ValidityException, ParsingException, IOException {
        /*
        CMLReader writer = new CMLReader();
        StringReader read = new StringReader(data);
        ByteArrayInputStream str = new ByteArrayInputStream(data.getBytes());
        CMLReader reader = new CMLReader(str);
        ChemFile chemfile = (ChemFile) reader.read((ChemObject) new ChemFile());
        System.out.println(chemfile.getChemSequenceCount());
        IChemSequence chemSequence = chemfile.getChemSequence(0);
	ChemModel cml = (ChemModel) chemSequence.getChemModel(0);
     */
       //Element ele = CMLUtil.parseXML(data);
      
       CMLBuilder build = new CMLBuilder();
       Element ele = build.parseString(data);
       CMLElement cml = (CMLElement) ele;
       //CMLProperty cml = (CMLProperty) ele;
  
       //System.out.println("Size: " + c.size());
      
        this.copyChildrenFrom( cml);
        /*
          StringReader read = new StringReader(data);
        ByteArrayInputStream str = new ByteArrayInputStream(data.getBytes());
        DefaultChemObjectBuilder build = DefaultChemObjectBuilder.getInstance();
        IChemFile file = build.newChemFile();
        CMLReader reader = new CMLReader(str);
        IChemObject chemobj = reader.read(file);
        IChemFile chemfile = (IChemFile) chemobj;
        System.out.println(chemfile.getChemSequenceCount());
        IChemSequence chemSequence = chemfile.getChemSequence(0);
	ChemModel chemModel = (ChemModel) chemSequence.getChemModel(0);
      */
        
        fromCML();
    }
    
    public String restore() {
 
                toCML();
                Document doc = new Document(this);
                String docxml = doc.toXML();
                String thisxml = this.toXML();
                
                return thisxml;
    }

    /**
     * 
     */
    abstract public void toCML();
    abstract public void fromCML();

    public Object getStructure() {
        return structure;
    }
    public void setStructure(Object s) {
        this.structure = s;
    }
}
