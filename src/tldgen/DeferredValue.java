package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an attribute is of type javax.el.ValueExpression.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface DeferredValue {
    
    /**
     * The return type of the value expression.
     */
    Class<?> value() default Object.class;
    
}
