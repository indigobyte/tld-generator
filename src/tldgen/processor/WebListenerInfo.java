package tldgen.processor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 */
@XmlType
class WebListenerInfo {
    private String type;
    
    protected WebListenerInfo(){
        
    }

    public WebListenerInfo(String type) {
        this.type=type;
    }
    
    @XmlElement(name="listener-class")
    public String getListenerClass() {
        return type;
    }

}
