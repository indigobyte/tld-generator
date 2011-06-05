package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a scripting variable.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Variable {
    
    /**
     * Description of the variable.
     */
    String description() default "";
    
    /**
     * Specifies the fixed name for the variable.
     */
    String nameGiven() default "";
    
    /**
     * The name of the variable will be taken from the value of the specified attribute of the tag.
     */
    String nameFromAttribute() default "";
    
    /**
     * The type of the variable.
     */
    Class<?> type() default String.class;
    
    /**
     * Specifies whether the variable is declared or not.
     */
    boolean declare() default true;
    
    /**
     * The scope of the variable.
     */
    VariableScope scope() default VariableScope.NESTED;
    
}
