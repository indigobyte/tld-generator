/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tldgen.processor;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 */
public class AttributeInfo {
    private String name;
    private boolean required;
    private String type;

    public AttributeInfo(String name, boolean required, String type) {
        this.name = name;
        this.required = required;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public String getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "AttributeInfo{" + "name=" + name + ", required=" + required + ", type=" + type + '}';
    }
    
}
