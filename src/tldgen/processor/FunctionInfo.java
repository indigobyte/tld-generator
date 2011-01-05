/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tldgen.processor;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 */
public class FunctionInfo {
    private String name;
    private String functionClass;
    private String functionSignature;

    public FunctionInfo(String name, String functionClass, String functionSignature) {
        this.name = name;
        this.functionClass = functionClass;
        this.functionSignature = functionSignature;
    }

    public String getFunctionClass() {
        return functionClass;
    }

    public String getFunctionSignature() {
        return functionSignature;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "FunctionInfo{" + "name=" + name + ", functionClass=" + functionClass + ", functionSignature=" + functionSignature + '}';
    }
    
}
