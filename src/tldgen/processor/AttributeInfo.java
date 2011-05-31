/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tldgen.processor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 * 
 * TODO missing deferred value and method
 */
@XmlType(propOrder={"description", "name", "required", "runtimeValueAllowed", "type", "jspFragment"})
class AttributeInfo {
    private String name;
    private String description;
    private boolean required;
    private String type;
    private boolean runtimeValueAllowed;

    protected AttributeInfo() {
    }
    
    public AttributeInfo(String name) {
        this.name = name;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @XmlElement
    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @XmlElement(name="rtexprvalue")
    public boolean isRuntimeValueAllowed() {
        return runtimeValueAllowed;
    }

    public void setRuntimeValueAllowed(boolean b) {
        this.runtimeValueAllowed = b;
    }

    @XmlElement
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @XmlElement(name="fragment")
    public boolean isJspFragment(){
        assert type != null;
        return type.equals("javax.servlet.jsp.tagext.JspFragment");
    }

    @Override
    public String toString() {
        return "AttributeInfo{" + "name=" + name + ", required=" + required + ", type=" + type + '}';
    }
    
}
