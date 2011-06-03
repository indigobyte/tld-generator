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
@XmlType(propOrder={"description", "name", "required", "runtimeValueAllowed", "type", "jspFragment", "deferredValue", "deferredMethod"})
class AttributeInfo {
    private String name;
    private String description;
    private Boolean required;
    private String type;
    private Boolean runtimeValueAllowed;
    private DeferredValueInfo deferredValue;
    private DeferredMethodInfo deferredMethod;

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
    
    @XmlElement(defaultValue="false")
    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    @XmlElement(name="rtexprvalue", defaultValue="false")
    public Boolean getRuntimeValueAllowed() {
        return runtimeValueAllowed;
    }

    public void setRuntimeValueAllowed(Boolean b) {
        System.out.println("runtimeValueAllowed: "+b);
        this.runtimeValueAllowed = b;
    }

    @XmlElement
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement(name="deferred-method")
    public DeferredMethodInfo getDeferredMethod() {
        return deferredMethod;
    }

    public void setDeferredMethod(DeferredMethodInfo deferredMethod) {
        this.deferredMethod = deferredMethod;
    }

    @XmlElement(name="deferred-value")
    public DeferredValueInfo getDeferredValue() {
        return deferredValue;
    }

    public void setDeferredValue(DeferredValueInfo deferredValue) {
        this.deferredValue = deferredValue;
    }
    
    @XmlElement(name="fragment", defaultValue="false")
    public Boolean isJspFragment(){
        assert type != null;
        return type.equals("javax.servlet.jsp.tagext.JspFragment") ? true: null;
    }
    
    @Override
    public String toString() {
        return "AttributeInfo{" + "name=" + name + ", required=" + required + ", type=" + type + '}';
    }
    
}
