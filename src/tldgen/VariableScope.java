package tldgen;

/**
 * Enumeration for the scopes of a scripting variaable.
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
