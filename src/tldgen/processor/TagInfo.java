/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tldgen.processor;

import java.util.LinkedList;
import java.util.List;
import javax.lang.model.element.PackageElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import tldgen.BodyContentType;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 * TODO missing tei-class, variable
 */
@XmlType(propOrder={"description", "displayName", "icon", "name", "tagClass", "teiClass", "bodyContentType", "variables", "attributes", "dynamicAttributesAccepted", "example"})
class TagInfo {
    private String name;
    private String description;
    private String displayName;
    private String icon;
    private String example;
    private String tagClass;
    private String teiClass;
    private BodyContentType bodyContentType;
    private boolean dynamicAttributesAccepted;
    private List<AttributeInfo> attributes=new LinkedList<AttributeInfo>();
    private List<VariableInfo> variables=new LinkedList<VariableInfo>();
    
    protected TagInfo(){
        
    }

    public TagInfo(String name, String tagClass, BodyContentType type) {
        this.name = name;
        this.tagClass = tagClass;
        this.bodyContentType=type;
    }

    @XmlElement(name="body-content")
    public BodyContentType getBodyContentType() {
        return bodyContentType;
    }
    
    @XmlElement(name="tag-class")
    public String getTagClass() {
        return tagClass;
    }
    
    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement(name="tei-class")
    public String getTeiClass() {
        return teiClass;
    }

    public void setTeiClass(String teiClass) {
        this.teiClass = teiClass;
    }
    
    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name="display-name")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @XmlElement
    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getIcon() {
        return icon;
    }

    @XmlElement
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    @XmlElement(name="attribute")
    public List<AttributeInfo> getAttributes() {
        return attributes;
    }

    @XmlElement(name="variable")
    public List<VariableInfo> getVariables() {
        return variables;
    }
    
    @Override
    public String toString() {
        return "TagInfo{" + "name=" + name + ", tagClass=" + tagClass + ", bodyContentType=" + bodyContentType + ", attributes=" + attributes + '}';
    }

    public void setDynamicAttributesAccepted(boolean b) {
        this.dynamicAttributesAccepted=b;
    }

    @XmlElement(name="dynamic-attributes")
    public boolean isDynamicAttributesAccepted() {
        return dynamicAttributesAccepted;
    }

}
