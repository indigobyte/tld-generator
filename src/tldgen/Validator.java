package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a tag library validator.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Validator {
    
    /**
     * The init parameters for the validator.
     */
    InitParam[] initParams() default {};
    
}
