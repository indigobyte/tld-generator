package tldgen;

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
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Tag {
    
    /**
     * Name of the tag
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
