/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tldgen;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.servlet.jsp.tagext.JspTag;

/**
 *
 * @author Victor
 */
@Retention(RetentionPolicy.SOURCE)
public @interface TagLibrary {
    
    String value();
    
    String description() default "";
    
    String displayName() default "";
    
    String shortName() default "";
    
    String libraryVersion() default "1.0";
    
    String jspVersion() default "2.0";
    
    String icon() default "";
    
    Class<? extends JspTag>[] tagHandlers() default {};
    
    String[] tagFiles() default {};
    
    Class<?>[] functionClasses() default {};
    
    String descriptorFile() default "taglib.tld";
    
}
