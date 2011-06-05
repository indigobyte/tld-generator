package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated a setter method as a tag attribute. The annotated method must follow the conventions for a bean property setter.
 *
 * @author Victor Hugo Herrera Maldonado
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface TagAttribute {
    
    /**
     * Indicates if the attribute is required.
     * 
     */
    boolean required() default false;
    
    /**
     * Description of the attribute.
     * 
     */
    String description() default "";
    
    /**
     * Defines if the attribute can have a value calculated at runtime.
     */
    boolean runtimeValueAllowed() default false;

}
