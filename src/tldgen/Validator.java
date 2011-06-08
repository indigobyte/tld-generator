/* Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0) */
package tldgen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a tag library validator.
 * 
 * <p>
 * <b>Example: A tag library validator.</b>
 * <pre>
 * &#64;Validator(initParams={&#64;InitParam(name="admin.email", value="admin@server.org")})
 * public class MyTagLibraryValidator extends TagLibraryValidator{
 *
 *     &#64;Override
 *     public ValidationMessage[] validate(String prefix, String uri, PageData page) {
 *         return super.validate(prefix, uri, page);
 *     }
 *     
 * }
 * </pre>
 * </p>
 * 
 * @author Victor Hugo Herrera Maldonado
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Validator {
    
    /**
     * The init parameters for the validator.
     */
    InitParam[] initParams() default {};
    
}
