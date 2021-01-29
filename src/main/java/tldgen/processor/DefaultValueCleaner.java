/* Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0) */
package tldgen.processor;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 */
class DefaultValueCleaner extends Marshaller.Listener{

    @Override
    public void beforeMarshal(Object source) {
        try {
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(source.getClass()).getPropertyDescriptors();
            for(PropertyDescriptor descriptor:descriptors){
                if(descriptor.getReadMethod() != null){
                    Object currentValue=descriptor.getReadMethod().invoke(source);
                    XmlElement xmlElement = descriptor.getReadMethod().getAnnotation(XmlElement.class);
                    String defaultValue=null;
                    if(xmlElement != null){
                        defaultValue=xmlElement.defaultValue();
                    }
                    if(currentValue != null && currentValue.toString().equals(defaultValue)){
                        if(descriptor.getWriteMethod() != null){
                            descriptor.getWriteMethod().invoke(source, new Object[]{null});
                        }
                    }
                }
            }
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
