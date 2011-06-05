package tldgen;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the definition of a tag file.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
@Retention(RetentionPolicy.SOURCE)
@Target({})
public @interface TagFile {
    
    /**
     * Description of the tag file.
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
     * Name of the tag.
     */
    String name();
    
    /**
     * Path of the tag file. It must be in META-INF/tags.
     */
    String path();
    
    /**
     * Example of use of the tag.
     */
    String example() default "";
    
}
