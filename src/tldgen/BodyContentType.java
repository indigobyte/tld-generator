/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tldgen;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 *
 * @author Victor
 */
@XmlEnum
public enum BodyContentType {
    @XmlEnumValue("JSP")
    JSP,
    
    @XmlEnumValue("scriptless")
    SCRIPTLESS,
    
    @XmlEnumValue("empty")
    EMPTY,
    
    @XmlEnumValue("tagdependent")
    TAG_DEPENDENT;
}
