package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.jsp.tagext.TagExtraInfo;

/**
 * Class-level definition for a tag handler.
 * <p>
 * The tag handler may define tag attributes setters that must be annotated with <code>TagAttribute</code>.
 * </p>
 * <p>
 * <b>Example 1:</b> A simple tag.
 * <pre>
 *  package example;
 *
 * //import statements
 *
 *
 * &#64;Tag
 * public class HelloTag extends SimpleTagSupport{
 *     private String name;
 *
 *     &#64;TagAttribute(required=true)
 *     public void setName(String name) {
 *         this.name = name;
 *     }
 *     
 *     &#64;Override
 *     public void doTag() throws JspException, IOException {
 *         getJspContext().getOut().print("Hello, "+name+"!");
 *     }
 *     
 * }
 * </pre>
 * 
 * <p>
 * <b>Example 2:</b> Defines a body for the tag.
 * <pre>
 * &#64;Tag(bodyContentType=BodyContentType.SCRIPTLESS)
 * public class ExampleTag extends SimpleTagSupport{
 *
 *     &#64;Override
 *     public void doTag() throws JspException, IOException {
 *         super.doTag();
 *     }
 *     
 * }
 * </pre>
 * 
 * @author Victor Hugo Herrera Maldonado
 * @since 1.0
 * @see TagAttribute
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Tag {
    
    /**
     * Name of the tag. If this value is not specified, the name of the tag will be the decapitalized simple name of the annotated class without the suffix "Tag" if any.
     * 
     * <p>
     * <b>Example 1:</b> Tag handler with explicit name
     * <pre>
     * &#64;Tag("myHello")
     * public class HelloTag extends SimpleTagSupport{
     *
     * }
     * </pre>
     * 
     * <p>
     * <b>Example 2:</b> The implicit name of tag will be "hello".
     * <pre>
     * &#64;Tag
     * public class HelloTag extends SimpleTagSupport{
     *
     * }
     * </pre>
     */
    String value() default "";

    /**
     * Description of the tag.
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
     * Example of use of this tag.
     */
    String example() default "";
    
    /**
     * The type of content for the body of the tag.
     */
    BodyContentType bodyContentType() default BodyContentType.EMPTY;
    
    /**
     * A class with extra information for the tag handler.
     */
    Class<? extends TagExtraInfo> teiClass() default TagExtraInfo.class;
    
    /**
     * Scripting variables for the tag handler.
     */
    Variable[] variables() default {};
    
}
