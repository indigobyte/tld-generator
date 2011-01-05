/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tldgen.annotations;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Victor
 */
public class TagInfo {
    private String name;
    private String tagClass;
    private boolean contentAllowed;
    private List<AttributeInfo> attributes=new LinkedList<AttributeInfo>();

    public TagInfo(String name, String tagClass, boolean isContentAllowed) {
        this.name = name;
        this.tagClass = tagClass;
        this.contentAllowed = isContentAllowed;
    }

    public boolean isContentAllowed() {
        return contentAllowed;
    }

    public String getTagClass() {
        return tagClass;
    }
    
    public String getName() {
        return name;
    }
    
    public List<AttributeInfo> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "TagInfo{" + "name=" + name + ", tagClass=" + tagClass + ", isContentAllowed=" + contentAllowed + ", attributes=" + attributes + '}';
    }

}
