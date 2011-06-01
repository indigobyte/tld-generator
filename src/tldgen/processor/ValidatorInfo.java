/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tldgen.processor;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Victor
 */
@XmlType(propOrder={"validatorClass", "parameters"})
public class ValidatorInfo {
    String validatorClass;
    private List<ParameterInfo> parameters=new LinkedList<ParameterInfo>();

    @XmlElement(name="validator-class")
    public String getValidatorClass() {
        return validatorClass;
    }

    public void setValidatorClass(String validatorClass) {
        this.validatorClass = validatorClass;
    }

    @XmlElement(name="init-param")
    public List<ParameterInfo> getParameters() {
        return parameters;
    }
    
}
