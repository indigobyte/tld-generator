package tldgen;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a init parameter for a tag library validator.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
@Target({})
@Retention(RetentionPolicy.SOURCE)
public @interface InitParam {
    
    /**
     * Description of the parameter.
     */
    String description() default "";
    
    /**
     * Name of the parameter.
     */
    String name();
    
    /**
     * Value of the parameter.
     */
    String value();
    
}
