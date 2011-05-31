/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tldgen.processor;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import tldgen.BodyContentType;

/**
 *
 * @author Victor
 * 
 * TODO missing validator, listener, tag-file, tag-extension
 */
@XmlRootElement(name="taglib")
@XmlType(propOrder={"description", "displayName", "icon", "libraryVersion", "shortName", "uri", "tagHandlers", "functions"})
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
    
    @XmlElement(name="tag")
    List<TagInfo> getTagHandlers() {
        return tagHandlers;
    }

    @XmlElement(name="function")
    List<FunctionInfo> getFunctions() {
        return functions;
    }

    public static void main(String[] args) throws JAXBException {
        TagLibraryInfo info=new TagLibraryInfo("http://simplehtml.net");
        info.setJspVersion("2.1");
        info.setLibraryVersion("1.0");
        info.setDescription("Tags for common HTML elements");
        info.getFunctions().add(new FunctionInfo("sum", "functions.Sum", "int sum(int a, int b)"));
        info.getFunctions().add(new FunctionInfo("sum2", "functions.Sum", "int sum(int a, int b)"));
        final TagInfo tagInfo = new TagInfo("img", "my.ImageTag", BodyContentType.TAG_DEPENDENT);
        final AttributeInfo attributeInfo = new AttributeInfo("src");
        attributeInfo.setType("java.lang.String");
        tagInfo.getAttributes().add(attributeInfo);
        info.getTagHandlers().add(tagInfo);
        JAXBContext context=JAXBContext.newInstance(TagLibraryInfo.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
        m.marshal(info, System.out);
    }
    
}
