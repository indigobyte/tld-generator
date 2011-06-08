/* Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0) */
package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * May be used for tag attributes with a type of javax.el.MethodExpression. The value of the annotation defines the signature of the method.
 * <p>
 * If the type of an attribute is of javax.el.MethodExpression (even if this annotation is not present), it
 * will be declared as having a deferred method in the descriptor.
 * </p>
 * <p>
 * <b>Example: </b> Tag with an attribute for a method expression.
 * <pre>
 * &#64;Tag
 * public class Example6Tag extends SimpleTagSupport{
 *     private MethodExpression exp;
 *
 *     &#64;TagAttribute(required=true)
 *     &#64;DeferredMethod("int methodName()")
 *     public void setExp(MethodExpression exp) {
 *         this.exp = exp;
 *     }
 *     
 *     &#64;Override
 *     public void doTag() throws JspException, IOException {
 *         getJspContext().getOut().print(exp.invoke(getJspContext().getELContext(), new Object[0]));
 *     }
 *     
 * }
 * </pre>
 * </p>
 * @author Victor Hugo Herrera Maldonado
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface DeferredMethod {
    
    /**
     * Signature of the method.
     */
    String value() default "void methodName()";
    
}
