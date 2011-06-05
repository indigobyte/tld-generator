package tldgen.processor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 */
@XmlType(name="function", propOrder={"description", "displayName", "icon", "name", "functionClass", "functionSignature", "example"})
class FunctionInfo {
    private String name;
    private String description;
    private String displayName;
    private String icon;
    private String functionClass;
    private String functionSignature;
    private String example;
    
    protected FunctionInfo(){
        
    }

    public FunctionInfo(String name, String functionClass, String functionSignature) {
        this.name = name;
        this.functionClass = functionClass;
        this.functionSignature = functionSignature;
    }
    
    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement(name="function-class")
    public String getFunctionClass() {
        return functionClass;
    }

    @XmlElement(name="function-signature")
    public String getFunctionSignature() {
        return functionSignature;
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

    @XmlElement
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "FunctionInfo{" + "name=" + name + ", functionClass=" + functionClass + ", functionSignature=" + functionSignature + '}';
    }
    
}
