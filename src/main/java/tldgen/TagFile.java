/* Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0) */
package tldgen;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the definition of a tag file. Because tag files are not implemented in classes, 
 * they can't be directly annotated and have to be declared in the tag library.
 * <p>
 * <b>Example:</b> A tag library definition with tag files.
 * <pre>
 * &#64;TagLibrary(value="http://example-guide.org", 
 *           tagFiles={&#64;TagFile(name="greeting", path="META-INF/tags/greeting.tag"), &#64;TagFile(name="header", path="META-INF/tags/header.tag")})
 * </pre>
 * <p>
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
