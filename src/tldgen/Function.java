package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a public static method as an EL function. If the method is not public static, the function is not considered in the descriptor.
 * 
 * <p>
 * <b>Example:</b> Basic function.
 * <pre>
 * public class MyFunctions {
 *     
 *     &#64;Function
 *     public static String reverse(String string){
 *         return new StringBuilder(string).reverse().toString();
 *     }
 *     
 * }
 * </pre>
 * </p>
 *  
 * @author Victor Hugo Herrera Maldonado
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Function {
    
    /**
     * Name of the function. If not specified, the name will be the name of the method.
     */
    String value() default "";
    
    /**
     * Description of the function.
     */
    String description() default "";
    
    /**
     * Display name that could be used by a tool at design time.
     */
    String displayName() default "";
    
    /**
     * Icon that could be used by a tool at design time.
     */
    String icon() default "";
    
    /**
     * Example of use of this function.
     */
    String example() default "";
    
}
