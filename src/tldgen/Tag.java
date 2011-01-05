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
 * Indicates that a class is a tag handler.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Tag {
    /**
     * Name of the tag
     */
    String name() default "";
    
    boolean isContentAllowed() default false;
    
}
