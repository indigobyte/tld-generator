package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a scripting variable.
 * <p>
 * This annotation can be applied to the tag handler class or in any of its attributes. When applied to the class-level, the name of the variable
 * can be fixed or derived from an attribute; but if attribute-level, is derived from the annotated attribute.
 * </p>
 * <p>
 * <b>Example 1:</b> A variable defined in the class-level.
 * <pre>
 * &#64;Tag(variables={&#64;Variable(nameGiven="result", type=Integer.class, scope= VariableScope.AT_END)})
 * public class Example2Tag extends BodyTagSupport{
 *
 *     &#64;Override
 *     public int doEndTag() throws JspException {
 *         return BodyTag.EVAL_PAGE;
 *     }
 *     
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * <b>Example 2:</b> A variable defined in the attribute-level.
 * <pre>
 * &#64;Tag
 * public class Example5Tag extends TagSupport{
 *     private String varName;
 *
 *     &#64;TagAttribute(required=true)
 *     &#64;Variable
 *     public void setVarName(String varName) {
 *         this.varName = varName;
 *     }
 *     
 *     &#64;Override
 *     public int doEndTag() throws JspException {
 *         pageContext.setAttribute(varName, "Hello, world!");
 *         return TagSupport.EVAL_PAGE;
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
