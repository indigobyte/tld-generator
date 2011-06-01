/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Victor
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface DeferredMethod {
    
    String value() default "void methodName()";
    
}
