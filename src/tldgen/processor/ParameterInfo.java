/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tldgen.processor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Victor
 */
@XmlType(propOrder={"description", "name", "value"})
public class ParameterInfo {
    private String description;
    private String name;
    private String value;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @XmlElement
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
