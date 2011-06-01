/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tldgen.processor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import tldgen.VariableScope;

/**
 *
 * @author Victor
 */
@XmlType(propOrder={"description", "nameGiven", "nameFromAttribute", "variableClass", "declare", "scope"})
public class VariableInfo {
    private String description;
    private String nameGiven;
    private String nameFromAttribute;
    private String variableClass;
    private boolean declare;
    private VariableScope scope;

    @XmlElement
    public boolean isDeclare() {
        return declare;
    }

    public void setDeclare(boolean declare) {
        this.declare = declare;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name="name-from-attribute")
    public String getNameFromAttribute() {
        return nameFromAttribute;
    }

    public void setNameFromAttribute(String nameFromAttribute) {
        this.nameFromAttribute = nameFromAttribute;
    }

    @XmlElement(name="name-given")
    public String getNameGiven() {
        return nameGiven;
    }

    public void setNameGiven(String nameGiven) {
        this.nameGiven = nameGiven;
    }

    public VariableScope getScope() {
        return scope;
    }

    public void setScope(VariableScope scope) {
        this.scope = scope;
    }

    @XmlElement(name="variable-class")
    public String getVariableClass() {
        return variableClass;
    }

    public void setVariableClass(String variableClass) {
        this.variableClass = variableClass;
    }
    
}
