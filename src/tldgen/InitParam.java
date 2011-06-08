/* Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0) */
package tldgen;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a init parameter for a tag library validator.
 * <p>
 * For an example, see @{@link Validator}.
 * </p>
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
