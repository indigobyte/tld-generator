/* Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0) */
package tldgen.processor;

import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 */
@XmlType
class DeferredValueInfo {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
