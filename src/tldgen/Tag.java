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
import javax.servlet.jsp.tagext.TagExtraInfo;

/**
 * Indicates that a class is a tag handler.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
//@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Tag {
    
    /**
     * Name of the tag
     * 
     * return tag name.
     */
    String value() default "";
    
    String description() default "";
    
    String displayName() default "";
    
    String icon() default "";
    
    String example() default "";
    
    BodyContentType bodyContentType() default BodyContentType.EMPTY;
    
    Class<? extends TagExtraInfo> teiClass() default TagExtraInfo.class;
    
    Variable[] variables() default {};
    
}
