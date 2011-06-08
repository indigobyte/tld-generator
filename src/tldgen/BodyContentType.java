package tldgen;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * The type of valid content for the body of a tag. Used in {@link Tag} annotation.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
@XmlEnum
public enum BodyContentType {
    /**
     * The body of the tag can be any kind of valid JSP content.
     */
    @XmlEnumValue("JSP")
    JSP,
    
    /**
     * The body of the tag doesn't allow scripts.
     */
    @XmlEnumValue("scriptless")
    SCRIPTLESS,
    
    /**
     * The tag can not have a body.
     */
    @XmlEnumValue("empty")
    EMPTY,
    
    /**
     * The body content is dependent on the tag (Could be in another language like SQL for example). 
     */
    @XmlEnumValue("tagdependent")
    TAG_DEPENDENT;
    
}
