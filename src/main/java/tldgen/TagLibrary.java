/* Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0) */
package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.annotation.WebListener;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagLibraryValidator;

/**
 * Package-level annotation for the definition of a tag library. This annotation will be transformed into the root element of 
 * the tag library descriptor that will enclose all other elements.
 * 
 * <p><b>Example 1: </b> A basic definition of a tag library.
 * <pre>
 *    //file: package-info.java
 *    &#64;TagLibrary("http://example.org")
 *    package example;
 *     
 *    import tldgen.TagLibrary;
 * </pre>
 * 
 * <p>
 * <b>Note 1:</b> The path of the generated descriptor file is specified with <code>descriptorFile</code> which default value is "taglib.tld".
 * You have to care of not having two or more libraries with the same descriptor file. In fact, TLD generator checks for this situation and will notify you about this.
 * </p>
 * <p>
 * A tag library can list explicitly tag handlers, classes containing functions, listeners and validator. In case of not defining a value for these,
 * the elements in the same package of the tag library definition will be added.
 * </p>
 * <p>
 * <b>Example 2:</b> This tag handler will be in library defined above because is in the same package and the library doesn't have explicit tag handlers.</p>
 * <pre>
 * package example;
 * 
 * //... import statements
 * 
 * &#64;Tag
 * public class HelloTag extends SimpleTagSupport{
 *     private String name;

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
 * <b>Example 3:</b> This library has explicit definition of tag handlers and functions.
 * <pre>
 *    //file: package-info.java
 *    &#64;TagLibrary(value="http://example.org", tagHandlers={PrintTag.class, HelloTag.class}, functionClasses={MathOperations.class})
 *    package example;
 *     
 *    import tldgen.TagLibrary;
 * </pre>
 * 
 * <p>
 * <b>Note 2: </b> Even if the elements are defined explicitly, they must still be annotated to be included in the descriptor.
 * </p>
 * 
 * @author Victor Hugo Herrera Maldonado
 * @see Tag
 * @see Function
 * @see Validator
 * @see javax.servlet.annotation.WebListener
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
public @interface TagLibrary {
    
    /**
     * The uri of the tag library.
     */
    String value();
    
    /**
     * The description of the library.
     */
    String description() default "";
    
    /**
     * Display name that could be used by a tool at design time.
     */
    String displayName() default "";
    
    /**
     * Short name that could be used by a tool at design time.
     */
    String shortName() default "";
    
    /**
     * Version of the tag library.
     */
    String libraryVersion() default "1.0";
    
    /**
     * Version of the JSP.
     */
    String jspVersion() default "2.1";
    
    /**
     * Icon that could be used by a tool at design time.
     */
    String icon() default "";
    
    /**
     * Tag handlers for the library. If this array is empty, all handlers in the same package
     * as the annotated element will be added to the library.
     */
    Class<? extends JspTag>[] tagHandlers() default {};
    
    /**
     * An array of tag files to define in the library.
     */
    TagFile[] tagFiles() default {};
    
    /**
     * Function classes for the library. If this array is empty, all functions in the classes in the same package
     * as the annotated element will be added to the library.
     */
    Class<?>[] functionClasses() default {};
    
    /**
     * Web listeners for the library. If this array is empty, all listeners in the same package
     * as the annotated element will be added to the library.
     */
    Class<?>[] webListeners() default {};
    
    /**
     * The validator class for this library. If not specified, the validator in the same package as the element is considered.
     * <p/>
     * Note that there can only be one validator in the library.
     */
    Class<? extends TagLibraryValidator> validator() default TagLibraryValidator.class;
    
    /**
     * The path of the generated TLD file relative to META-INF.
     */
    String descriptorFile() default "taglib.tld";
    
}
