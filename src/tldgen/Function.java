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
 * Indicates that a method is a function. If the method is not public static, the declaration will be omitted by the TLD generator.
 *  
 * @author Victor Hugo Herrera Maldonado
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Function {
    String value() default "";
}
