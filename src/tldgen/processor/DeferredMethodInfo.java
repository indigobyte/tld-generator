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
@XmlType
public class DeferredMethodInfo {
    private String signature;

    public String getSignature() {
        return signature;
    }

    @XmlElement(name="method-signature")
    public void setSignature(String signature) {
        this.signature = signature;
    }
    
}
