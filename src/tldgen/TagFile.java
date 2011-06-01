/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tldgen;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Victor
 */
@Retention(RetentionPolicy.SOURCE)
@Target({})
public @interface TagFile {
    
    String description() default "";
    
    String displayName() default "";
    
    String icon() default "";
    
    String name();
    
    String path();
    
    String example() default "";
    
}
