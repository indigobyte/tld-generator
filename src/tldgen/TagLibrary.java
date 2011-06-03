/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tldgen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagLibraryValidator;

/**
 *
 * @author Victor
 */
//@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
public @interface TagLibrary {
    
    String value();
    
    String description() default "";
    
    String displayName() default "";
    
    String shortName() default "";
    
    String libraryVersion() default "1.0";
    
    String jspVersion() default "2.0";
    
    String icon() default "";
    
    Class<? extends JspTag>[] tagHandlers() default {};
    
    TagFile[] tagFiles() default {};
    
    Class<?>[] functionClasses() default {};
    
    Class<?>[] webListeners() default {};
    
    Class<? extends TagLibraryValidator> validator() default TagLibraryValidator.class;
    
    String descriptorFile() default "taglib.tld";
    
}
