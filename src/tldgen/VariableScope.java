/* Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0) */
package tldgen;

/**
 * Possible scopes for a scripting variable. Used in {@link Variable} annotation.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
public enum VariableScope {
    /**
     * The variable is visible only within start/end tags.
     */
    NESTED,
    
    /**
     * The variable is visible after start tag.
     */
    AT_BEGIN,
    
    /**
     * The variable is visible after end tag.
     */
    AT_END;
    
}
