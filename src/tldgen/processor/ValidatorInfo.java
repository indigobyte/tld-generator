/* Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0) */
package tldgen.processor;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 */
@XmlType(propOrder={"validatorClass", "parameters"})
class ValidatorInfo {
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
