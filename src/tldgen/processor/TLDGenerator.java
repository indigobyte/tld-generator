/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tldgen.processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import tldgen.Function;
import tldgen.Tag;
import tldgen.TagAttribute;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 */
@SupportedAnnotationTypes("tldgen.*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TLDGenerator extends AbstractProcessor{
    private List<TagInfo> tags=new LinkedList<TagInfo>();
    private List<FunctionInfo> functions=new LinkedList<FunctionInfo>();
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<Element> elements=new HashSet<Element>();
        elements.addAll(roundEnv.getElementsAnnotatedWith(Tag.class));
        for(Element e: elements){
            if(e instanceof TypeElement){
                TypeElement tElement=(TypeElement)e;
                System.out.println("Tag: "+e);
                Tag tag = e.getAnnotation(Tag.class);
                String tagName=null;
                if(!tag.name().equals("")){
                    tagName=tag.name();
                }else{
                    tagName=e.getSimpleName().toString();
                    if(tagName.endsWith("Tag")){
                        tagName=tagName.substring(0, tagName.length()-3);
                    }
                }
                TagInfo tagInfo=new TagInfo(toAttributeName(tagName), tElement.asType().toString(), tag.isContentAllowed());
                System.out.println("tag: "+tagInfo);
                while(tElement != null){
                    List<ExecutableElement> methods = ElementFilter.methodsIn(tElement.getEnclosedElements());
                    for(ExecutableElement method: methods){
                        if(isSetter(method) && method.getAnnotation(TagAttribute.class) != null){
                            TagAttribute tagAttribute=method.getAnnotation(TagAttribute.class);
                            AttributeInfo attributeInfo=new AttributeInfo(getAttributeName(method), tagAttribute.isRequired(), method.getParameters().get(0).asType().toString());
                            tagInfo.getAttributes().add(attributeInfo);
                            System.out.println("attribute: "+attributeInfo);
                        }
                    }
                    TypeMirror superclass = tElement.getSuperclass();
                    if(superclass.getKind().equals(TypeKind.NONE)){
                        tElement=null;
                    }else{
                        tElement=(TypeElement) ((DeclaredType)superclass).asElement();
                    }
                }
                tags.add(tagInfo);
            }
        }
        elements=new HashSet<Element>();
        elements.addAll(roundEnv.getElementsAnnotatedWith(Function.class));
        for(Element e: elements){
            ExecutableElement executable=(ExecutableElement) e;
            if(e.getModifiers().contains(Modifier.STATIC) && e.getModifiers().contains(Modifier.PUBLIC)){
                Function f=e.getAnnotation(Function.class);
                FunctionInfo info=new FunctionInfo(f.value().equals("") ? executable.getSimpleName().toString(): f.value(), executable.getEnclosingElement().asType().toString(), getSignature(executable));
                System.out.println("function: "+info);
                functions.add(info);
            }else{
                processingEnv.getMessager().printMessage(Kind.WARNING, "Function method must be public static. This will be omited in declaration.", e);
            }
        }
        if(!roundEnv.processingOver()){
            try{
                generateXML(processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/taglib.tld", new Element[0]));
            }catch(XMLStreamException ex){
                ex.printStackTrace();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return true;
    }

    private boolean isSetter(ExecutableElement element) {
        return pattern.matcher(element.getSimpleName().toString()).matches() && element.getParameters().size() == 1;
    }
    
    private Pattern pattern=Pattern.compile("set(.+)");
    
    private String getAttributeName(ExecutableElement element){
        Matcher matcher = pattern.matcher(element.getSimpleName().toString());
        matcher.matches();
        return toAttributeName(matcher.group(1));
    }
    
    private static String toAttributeName(String name){
        if(name.length() == 1){
            return name.toLowerCase();
        }else{
            return Character.toLowerCase(name.charAt(0))+name.substring(1);
        }
    }
    
    private int level;

    private void generateXML(FileObject file) throws XMLStreamException, IOException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
        FileObject resource = processingEnv.getFiler().getResource(StandardLocation.SOURCE_PATH, "", "META-INF/taglib.properties");
        
        Properties properties=new Properties();
        InputStream input = resource.openInputStream();
        properties.load(input);
        input.close();
        level=0;
        XMLStreamWriter writer = factory.createXMLStreamWriter(file.openWriter());
        writer.writeStartDocument("UTF-8", "1.0");
        writer.setDefaultNamespace("http://java.sun.com/xml/ns/javaee");
        writer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        writer.writeCharacters("\n");
        writeStartElement(writer, "tag-lib");
        writer.writeAttribute("version", "2.1");
        writer.writeAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
        writer.writeCharacters("\n");
        writeWithContent(writer, "tlib-version", properties.getProperty("tlib-version", "1.0"));
        writeWithContent(writer, "short-name", properties.getProperty("short-name", ""));
        writeWithContent(writer, "uri", properties.getProperty("uri", ""));
        for(TagInfo tagInfo: tags){
//            myWriter.startElement("tag");
            
            writeStartElement(writer, "tag");
            writer.writeCharacters("\n");
            
//            myWriter.startElement("name").setContent(tagInfo.getName());
            
            writeWithContent(writer, "name", tagInfo.getName());
            writeWithContent(writer, "tag-class", tagInfo.getTagClass());
            writeWithContent(writer, "body-content", tagInfo.isContentAllowed()? "scriptless": "empty");
            for(AttributeInfo attributeInfo:tagInfo.getAttributes()){
                writeStartElement(writer, "attribute");
                writer.writeCharacters("\n");
                writeWithContent(writer, "name", attributeInfo.getName());
                writeWithContent(writer, "type", attributeInfo.getType());
                writeWithContent(writer, "required", String.valueOf(attributeInfo.isRequired()));
                writeEndElement(writer); //attribute
            }
            writeEndElement(writer); //tag
        }
        
        for(FunctionInfo functionInfo:functions){
            writeStartElement(writer, "function");
            writer.writeCharacters("\n");
            writeWithContent(writer, "name", functionInfo.getName());
            writeWithContent(writer, "function-class", functionInfo.getFunctionClass());
            writeWithContent(writer, "method-signature", functionInfo.getFunctionSignature());
            writeEndElement(writer);
        }
        
        writeEndElement(writer); //tag-lib
        writer.writeCharacters("\n");
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }

    private void spacer(int level, XMLStreamWriter writer) throws XMLStreamException {
        for(int i=0; i < level; i++){
            for(int j=0; j < 4; j++){
                writer.writeCharacters(" ");
            }
        }
    }
    
    private String getSignature(ExecutableElement e){
        StringBuilder builder=new StringBuilder(e.getReturnType().toString());
        builder.append(" ");
        builder.append(e.getSimpleName().toString());
        builder.append('(');
        System.out.println("number of parameters: "+e.getParameters());
        for(VariableElement parameter:e.getParameters()){
            builder.append(parameter.asType().toString());
            builder.append(',');
        }
        if(builder.charAt(builder.length()-1) == ','){
            builder.deleteCharAt(builder.length()-1);
        }
        builder.append(')');
        return builder.toString();
    }
    
    private void writeWithContent(XMLStreamWriter writer, String tag, String content) throws XMLStreamException{
        spacer(level, writer);
        writer.writeStartElement(tag);
        writer.writeCharacters(content);
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }
    
    private void writeStartElement(XMLStreamWriter writer, String tag) throws XMLStreamException{
        spacer(level, writer);
        writer.writeStartElement(tag);
        level++;
    }
    
    private void writeEndElement(XMLStreamWriter writer) throws XMLStreamException{
        level--;
        spacer(level, writer);
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }
    
}
