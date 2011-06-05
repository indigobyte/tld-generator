package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagLibraryValidator;

/**
 * Definition for a tag library. This is the entry annotation for generation of tag library descriptors.
 * 
 * @author Victor Hugo Herrera Maldonado
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
