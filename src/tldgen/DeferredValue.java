package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * May be used for tag attributes with a type of javax.el.ValueExpression. The value of the annotation defines the return type of the expression.
 * <p>
 * If the type of an attribute is of javax.el.ValueExpression (even if this annotation is not present), it
 * will be declared as having a deferred value in the descriptor.
 * </p>
 * <p>
 * <b>Example: </b> Tag with an attribute for a value expression.
 * <pre>
 * &#64;Tag
 * public class Example4Tag extends SimpleTagSupport{
 *     private javax.el.ValueExpression exp;

 *     &#64;TagAttribute(required=true)
 *     &#64;DeferredValue(String.class)
 *     public void setExp(javax.el.ValueExpression exp) {
 *         this.exp = exp;
 *     }
 *     
 *     &#64;Override
 *     public void doTag() throws JspException, IOException {
 *         getJspContext().getOut().print(exp.getValue(getJspContext().getELContext()));
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
public @interface DeferredValue {
    
    /**
     * The return type of the value expression.
     */
    Class<?> value() default Object.class;
    
}
