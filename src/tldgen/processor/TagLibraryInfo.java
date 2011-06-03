package tldgen.processor;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Victor
 * 
 */
@XmlRootElement(name="taglib")
@XmlType(propOrder={"description", "displayName", "icon", "libraryVersion", "shortName", 
            "uri", "validator", "webListeners", "tagFiles", "tagHandlers", "functions"})
public class TagLibraryInfo {
    private String uri;
    private String description;
    private String displayName;
    private String icon;
    private String libraryVersion;
    private String jspVersion;
    private String shortName;
    private List<TagInfo> tagHandlers=new LinkedList<TagInfo>();
    private List<FunctionInfo> functions=new LinkedList<FunctionInfo>();
    private ValidatorInfo validator;
    private List<WebListenerInfo> webListeners=new LinkedList<WebListenerInfo>();
    private List<TagFileInfo> tagFiles=new LinkedList<TagFileInfo>();

    protected TagLibraryInfo() {
    }
    
    public TagLibraryInfo(String uri) {
        this.uri = uri;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name="display-name")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @XmlElement
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @XmlElement(name="short-name")
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @XmlElement
    public String getUri() {
        return uri;
    }

    @XmlElement(name="tlib-version")
    public String getLibraryVersion() {
        return libraryVersion;
    }

    public void setLibraryVersion(String version) {
        this.libraryVersion = version;
    }

    @XmlAttribute(name="version")
    public String getJspVersion() {
        return jspVersion;
    }

    public void setJspVersion(String jspVersion) {
        this.jspVersion = jspVersion;
    }

    public ValidatorInfo getValidator() {
        return validator;
    }

    @XmlElement
    public void setValidator(ValidatorInfo validator) {
        this.validator = validator;
    }
    
    @XmlElement(name="tag")
    List<TagInfo> getTagHandlers() {
        return tagHandlers;
    }

    @XmlElement(name="function")
    List<FunctionInfo> getFunctions() {
        return functions;
    }

    @XmlElement(name="tag-file")
    List<TagFileInfo> getTagFiles() {
        return tagFiles;
    }

    @XmlElement(name="listener")
    List<WebListenerInfo> getWebListeners() {
        return webListeners;
    }
    
}
