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
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Validator {
    
    InitParam[] initParams() default {};
    
}
