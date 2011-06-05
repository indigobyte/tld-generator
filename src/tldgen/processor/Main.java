package tldgen.processor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import tldgen.BodyContentType;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 */
class Main {
    
    public static void main(String[] args) throws JAXBException {
        TagLibraryInfo info=new TagLibraryInfo("http://simplehtml.net");
        info.setJspVersion("2.1");
        info.setLibraryVersion("1.0");
        info.setDescription("Tags for common HTML elements");
        info.getFunctions().add(
             new FunctionInfo("sum", "functions.Sum", "int sum(int a, int b)"));
        info.getFunctions().add(
             new FunctionInfo("sum2", "functions.Sum", "int sum(int a, int b)"));
        TagInfo tagInfo = new TagInfo("img", "my.ImageTag", BodyContentType.TAG_DEPENDENT);
        AttributeInfo attributeInfo = new AttributeInfo("src");
        attributeInfo.setType("java.lang.String");
        tagInfo.getAttributes().add(attributeInfo);
        info.getTagHandlers().add(tagInfo);
        JAXBContext context=JAXBContext.newInstance(TagLibraryInfo.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
          "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
        m.marshal(info, System.out);
    }
    
}
