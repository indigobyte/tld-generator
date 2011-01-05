/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tldgen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated a setter method as a tag attribute. The annotated method must follow the conventions for a bean property setter.
 *
 * @author Victor Hugo Herrera Maldonado
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface TagAttribute {
    
    boolean isRequired() default false;

}
