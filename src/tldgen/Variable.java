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
public @interface Variable {
    
    String description() default "";
    
    String nameGiven() default "";
    
    String nameFromAttribute() default "";
    
    Class<?> type() default String.class;
    
    boolean declare() default true;
    
    VariableScope scope() default VariableScope.NESTED;
    
}
