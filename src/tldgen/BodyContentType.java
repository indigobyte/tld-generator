package tldgen;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * The type of content for the body of tag.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
@XmlEnum
public enum BodyContentType {
    /**
     * Any kind of valid JSP content.
     */
    @XmlEnumValue("JSP")
    JSP,
    
    /**
     * No scripts allowed.
     */
    @XmlEnumValue("scriptless")
    SCRIPTLESS,
    
    /**
     * The tag can not have a body.
     */
    @XmlEnumValue("empty")
    EMPTY,
    
    /**
     * The content is dependent on the tag.
     */
    @XmlEnumValue("tagdependent")
    TAG_DEPENDENT;
    
}
