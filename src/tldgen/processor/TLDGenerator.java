/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tldgen.processor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.servlet.annotation.WebListener;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import tldgen.BodyContentType;
import tldgen.DeferredMethod;
import tldgen.DeferredValue;
import tldgen.Function;
import tldgen.InitParam;
import tldgen.Tag;
import tldgen.TagAttribute;
import tldgen.TagLibrary;
import tldgen.Validator;
import tldgen.Variable;
import tldgen.VariableScope;

/**
 * TLD Generator.
 * 
 * @author Victor Hugo Herrera Maldonado
 */
@SupportedAnnotationTypes("tldgen.*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TLDGenerator extends AbstractProcessor {

    
    private Map<String, TagInfo> tagMap = new HashMap<String, TagInfo>();
    private List<FunctionInfo> functions = new LinkedList<FunctionInfo>();
    private Map<String, ValidatorInfo> validatorMap=new HashMap<String, ValidatorInfo>();
    private Map<String, WebListenerInfo> webListenerMap=new HashMap<String, WebListenerInfo>();
    private List<AnnotationMirrorWrapper> libraries = new LinkedList<AnnotationMirrorWrapper>();
    
    private static Map<String, String> nativeTypes = new HashMap<String, String>();
    {
        nativeTypes.put("byte", "java.lang.Byte");
        nativeTypes.put("short", "java.lang.Short");
        nativeTypes.put("int", "java.lang.Integer");
        nativeTypes.put("long", "java.lang.Long");
        nativeTypes.put("float", "java.lang.Float");
        nativeTypes.put("double", "java.lang.Double");
        nativeTypes.put("boolean", "java.lang.Boolean");
        nativeTypes.put("char", "java.lang.Character");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(Tag.class)) {
            if (e instanceof TypeElement) {
                TypeElement tElement = (TypeElement)e;
                AnnotationMirrorWrapper tagMirrorWrapper=new AnnotationMirrorWrapper(getAnnotationMirror(e, Tag.class)) ;
                String tagName = null;
                if (!tagMirrorWrapper.getValue("value").equals("")) {
                    tagName = tagMirrorWrapper.getValue("value").toString();
                } else {
                    tagName = e.getSimpleName().toString();
                    if (tagName.endsWith("Tag")) {
                        tagName = tagName.substring(0, tagName.length() - 3);
                    }
                }
                VariableElement value = (VariableElement) tagMirrorWrapper.getValue("bodyContentType");
                TagInfo tagInfo = new TagInfo(toAttributeName(tagName), e.asType().toString(), BodyContentType.valueOf(value.toString()));
                copyAnnotationValuesToBean(tagMirrorWrapper.getMirror(), tagInfo);
                
                if(!tagMirrorWrapper.getValue("teiClass").toString().replace(".class", "").equals("javax.servlet.jsp.tagext.TagExtraInfo")){
                    tagInfo.setTeiClass(tagMirrorWrapper.getValue("teiClass").toString().replace(".class", ""));
                }
                
                for(AnnotationMirror mirror:(List<AnnotationMirror>)tagMirrorWrapper.getValue("variables")){
                    VariableInfo info=new VariableInfo();
                    AnnotationMirrorWrapper mirrorWrapper=new AnnotationMirrorWrapper(mirror);
                    copyAnnotationValuesToBean(mirrorWrapper.getMirror(), info);
                    info.setNameFromAttribute(mirrorWrapper.getValue("nameFromAttribute").toString());
                    info.setNameGiven(mirrorWrapper.getValue("nameGiven").toString());
                    if(info.getNameFromAttribute().equals("")){
                        info.setNameFromAttribute(null);
                    }
                    if(info.getNameGiven().equals("")){
                        info.setNameGiven(null);
                    }
                    if(info.getNameFromAttribute() != null && info.getNameGiven()!= null){
                        processingEnv.getMessager().printMessage(Kind.ERROR, "nameFromAttribute and nameGiven can not be presented at the same time.", e);
                    }
                    info.setVariableClass(mirrorWrapper.getValue("type").toString().replace(".class", ""));
                    info.setScope(VariableScope.valueOf(mirrorWrapper.getValue("scope").toString()));
                    tagInfo.getVariables().add(info);
                }

                List<ExecutableElement> methodsT = ElementFilter.methodsIn(processingEnv.getElementUtils().getAllMembers(tElement)); 
                for(ExecutableElement m:methodsT){
                    if (isSetter(m) && m.getAnnotation(TagAttribute.class) != null) {
                        TagAttribute tagAttribute = m.getAnnotation(TagAttribute.class);
                        final AnnotationMirror annotationMirror = getAnnotationMirror(m, TagAttribute.class);
                        String type = m.getParameters().get(0).asType().toString();
                        if (nativeTypes.containsKey(type)) {
                            type = nativeTypes.get(type);
                        }
                        AttributeInfo attributeInfo = new AttributeInfo(getAttributeName(m));
                        copyAnnotationValuesToBean(annotationMirror, attributeInfo);
                        
                        attributeInfo.setType(type);
                        if (type.equals("javax.servlet.jsp.tagext.JspFragment")) {
                            attributeInfo.setRuntimeValueAllowed(true);
                        } else {
                            attributeInfo.setRuntimeValueAllowed(tagAttribute.runtimeValueAllowed());
                        }
                        
                        if(type.equals("javax.el.ValueExpression")){
                            DeferredValueInfo info=new DeferredValueInfo();
                            AnnotationMirrorWrapper mirrorWrapper=getAnnotationMirrorWrapper(m, DeferredValue.class);
                            if(mirrorWrapper != null){
                                info.setType(mirrorWrapper.getValue("value").toString().replace(".class", ""));
                            }
                            attributeInfo.setDeferredValue(info);
                        }else if(type.equals("javax.el.MethodExpression")){
                            DeferredMethodInfo info = new DeferredMethodInfo();
                            attributeInfo.setDeferredMethod(info);
                            AnnotationMirrorWrapper mirrorWrapper=getAnnotationMirrorWrapper(m, DeferredMethod.class);
                            if(mirrorWrapper != null){
                                info.setSignature(mirrorWrapper.getValue("value").toString().replace(".class", ""));
                            }
                            attributeInfo.setDeferredMethod(info);
                        }
                        
                        AnnotationMirrorWrapper mirrorWrapper=getAnnotationMirrorWrapper(m, Variable.class);
                        if(mirrorWrapper != null){
                            VariableInfo info=new VariableInfo();
                            copyAnnotationValuesToBean(mirrorWrapper.getMirror(), info);
                            info.setNameFromAttribute(attributeInfo.getName());
                            info.setNameGiven(null);
                            info.setVariableClass(mirrorWrapper.getValue("type").toString().replace(".class", ""));
                            info.setScope(VariableScope.valueOf(mirrorWrapper.getValue("scope").toString()));
                            tagInfo.getVariables().add(info);
                        }
                        tagInfo.getAttributes().add(attributeInfo);
                    }
                }
                
                
                /* Look into the parent classes */
                do{
                    List<? extends TypeMirror> interfaces = tElement.getInterfaces();
                    for (TypeMirror i : interfaces) {
                        if (i.toString().equals("javax.servlet.jsp.tagext.DynamicAttributes")) {
                            tagInfo.setDynamicAttributesAccepted(true);
                            break;
                        }
                    }
                    TypeMirror superclass = tElement.getSuperclass();
                    if (superclass.getKind().equals(TypeKind.NONE)) {
                        tElement = null;
                    } else {
                        tElement = (TypeElement) ((DeclaredType) superclass).asElement();
                    }
                } while( tElement != null );
                tagMap.put(tagInfo.getTagClass(), tagInfo);
            }
        }
        
        for (Element e : roundEnv.getElementsAnnotatedWith(Function.class)) {
            ExecutableElement executable = (ExecutableElement) e;
            if (e.getModifiers().contains(Modifier.STATIC) && e.getModifiers().contains(Modifier.PUBLIC)) {
                AnnotationMirrorWrapper functionMirrorWrapper=new AnnotationMirrorWrapper(getAnnotationMirror(e, Function.class));
                String functionName=functionMirrorWrapper.getValue("value").toString();
                if(functionName.equals("")){
                    functionName=executable.getSimpleName().toString();
                }
                FunctionInfo info = new FunctionInfo(functionName, executable.getEnclosingElement().asType().toString(), getSignature(executable));
                copyAnnotationValuesToBean(functionMirrorWrapper.getMirror(), info);
                functions.add(info);
            } else {
                processingEnv.getMessager().printMessage(Kind.WARNING, "Function method must be public static. This will be omited in declaration.", e);
            }
        }
        
        for(Element e: roundEnv.getElementsAnnotatedWith(Validator.class)){
            Validator validator=e.getAnnotation(Validator.class);
            ValidatorInfo validatorInfo=new ValidatorInfo();
            validatorInfo.setValidatorClass(e.asType().toString());
            for(InitParam param:validator.initParams()){
                ParameterInfo paramInfo=new ParameterInfo();
                copyAnnotationValuesToBean(param, paramInfo);
                validatorInfo.getParameters().add(paramInfo);
            }
            validatorMap.put(validatorInfo.getValidatorClass(), validatorInfo);
        }
        
        for(Element e: roundEnv.getElementsAnnotatedWith(WebListener.class)){
            WebListenerInfo webListenerInfo=new WebListenerInfo(e.asType().toString());
            webListenerMap.put(webListenerInfo.getListenerClass(), webListenerInfo);
        }
        
        for (Element e : roundEnv.getElementsAnnotatedWith(TagLibrary.class)) {
            libraries.add(new AnnotationMirrorWrapper(getAnnotationMirror(e, TagLibrary.class)));
        }
        
        if (!roundEnv.processingOver()) {
            Map<String, TagLibraryInfo> libraryMap = new HashMap<String, TagLibraryInfo>();
            for (AnnotationMirrorWrapper wrapper : libraries) {
                TagLibraryInfo libraryInfo = new TagLibraryInfo(wrapper.getValue("value").toString());
                copyAnnotationValuesToBean(wrapper.getMirror(), libraryInfo);

                for (Object s : ((List) wrapper.getValue("tagHandlers"))) {
                    TagInfo tagInfo = tagMap.get(s.toString().replace(".class", ""));
                    libraryInfo.getTagHandlers().add(tagInfo);
                }
                for(FunctionInfo functionInfo: functions){
                    for (Object s : ((List) wrapper.getValue("functionClasses"))) {
                        if(s.toString().replace(".class", "").equals(functionInfo.getFunctionClass())){
                            libraryInfo.getFunctions().add(functionInfo);
                        }
                    }
                }
                for(Object o:(List)wrapper.getValue("tagFiles")){
                    AnnotationMirrorWrapper tagFileMirror=new AnnotationMirrorWrapper((AnnotationMirror)o);
                    TagFileInfo tagFileInfo=new TagFileInfo();
                    copyAnnotationValuesToBean(tagFileMirror.getMirror(), tagFileInfo);
                    libraryInfo.getTagFiles().add(tagFileInfo);
                }
                libraryMap.put(wrapper.getValue("descriptorFile").toString(), libraryInfo);
            }
            try {
                for (Map.Entry<String, TagLibraryInfo> entry : libraryMap.entrySet()) {
                    generateXML(entry.getValue(), entry.getKey());
                }
            } catch (JAXBException ex) {
                processingEnv.getMessager().printMessage(Kind.ERROR, "Problem while generating TLD: " + ex.toString());
            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Kind.ERROR, "Problem while generating TLD: " + ex.toString());
            }
        }
        return true;
    }

    private boolean isSetter(ExecutableElement element) {
        return pattern.matcher(element.getSimpleName().toString()).matches() && element.getParameters().size() == 1;
    }

    private Pattern pattern = Pattern.compile("set(.+)");

    private String getAttributeName(ExecutableElement element) {
        Matcher matcher = pattern.matcher(element.getSimpleName().toString());
        matcher.matches();
        return toAttributeName(matcher.group(1));
    }

    private static String toAttributeName(String name) {
        if (name.length() == 1) {
            return name.toLowerCase();
        } else {
            return Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }
    }
    
    private String getSignature(ExecutableElement e) {
        StringBuilder builder = new StringBuilder(e.getReturnType().toString());
        builder.append(" ");
        builder.append(e.getSimpleName().toString());
        builder.append('(');
        for (VariableElement parameter : e.getParameters()) {
            builder.append(parameter.asType().toString());
            builder.append(',');
        }
        if (builder.charAt(builder.length() - 1) == ',') {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append(')');
        return builder.toString();
    }
    
    private AnnotationMirrorWrapper getAnnotationMirrorWrapper(Element e, Class<? extends Annotation> klass) {
        AnnotationMirror mirror=getAnnotationMirror(e, klass);
        return mirror != null ? new AnnotationMirrorWrapper(mirror): null;
    }

    private AnnotationMirror getAnnotationMirror(Element e, Class<? extends Annotation> klass) {
        for (AnnotationMirror mirror : e.getAnnotationMirrors()) {
            if (mirror.getAnnotationType().toString().equals(klass.getName())) {
                return mirror;
            }
        }
        return null;
    }
    
    private static <S extends Annotation, T> void copyAnnotationValuesToBean(S annotation, T bean) {
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            try {
                String name = method.getName();
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                Method setter = bean.getClass().getMethod("set" + name, method.getReturnType());
                Object value = method.invoke(annotation);
                if(!value.equals("")){
                    setter.invoke(bean, value);
                }
            } catch (NoSuchMethodException ex) {
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private <T> void copyAnnotationValuesToBean(AnnotationMirror annotationMirror, T bean) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> annotationValues = processingEnv.getElementUtils().getElementValuesWithDefaults(annotationMirror);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationValues.entrySet()) {
            try {
                String returnType = entry.getKey().getReturnType().toString();
                Class type;
                if(returnType.equals("boolean")){
                    type=boolean.class;
                }else if(returnType.equals("byte")){
                    type=byte.class;
                }else if(returnType.equals("short")){
                    type=short.class;
                }else if(returnType.equals("int")){
                    type=int.class;
                }else if(returnType.equals("long")){
                    type=long.class;
                }else if(returnType.equals("float")){
                    type=float.class;
                }else if(returnType.equals("double")){
                    type=double.class;
                }else if(returnType.equals("char")){
                    type=char.class;
                }else{
                    type=entry.getValue().getValue().getClass();
                }
                String name = entry.getKey().getSimpleName().toString();
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                Method setter = null;
                try{
                    if(bean.getClass().equals(VariableInfo.class)){
                        System.out.println("type: "+type);
                        System.out.println("set" + name);
                    }
                    setter=bean.getClass().getMethod("set" + name, type);
                    if(bean.getClass().equals(VariableInfo.class)){
                        System.out.println("setter: "+setter);
                    }
                    
                }catch(NoSuchMethodException ex){
                    
                }
                if (setter != null && !entry.getValue().getValue().equals("")) {
                    setter.invoke(bean, entry.getValue().getValue());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void generateXML(TagLibraryInfo libraryInfo, String descriptorFile) throws JAXBException, IOException {
        FileObject file = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/" + descriptorFile, new Element[0]);
        JAXBContext context = JAXBContext.newInstance(TagLibraryInfo.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
        m.marshal(libraryInfo, file.openWriter());
    }
    
//    private int level;
//
//    private void generateXML(FileObject file) throws XMLStreamException, IOException {
//        XMLOutputFactory factory = XMLOutputFactory.newInstance();
//        factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
//        FileObject resource = processingEnv.getFiler().getResource(StandardLocation.SOURCE_PATH, "", "META-INF/taglib.properties");
//
//        Properties properties = new Properties();
//        InputStream input = resource.openInputStream();
//        properties.load(input);
//        input.close();
//        level = 0;
//        XMLStreamWriter writer = factory.createXMLStreamWriter(file.openWriter());
//        writer.writeStartDocument("UTF-8", "1.0");
//        writer.setDefaultNamespace("http://java.sun.com/xml/ns/javaee");
//        writer.setPrefix("xsi", "http://www.w3.org/2001/XMLSchema-instance");
//        writer.writeCharacters("\n");
//
//        writeStartElement(writer, "tag-lib");
//        writer.writeAttribute("version", "2.1");
//        writer.writeAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
//        writer.writeCharacters("\n");
//        writeWithContent(writer, "tlib-version", properties.getProperty("tlib-version", "1.0"));
//        writeWithContent(writer, "short-name", properties.getProperty("short-name", ""));
//        writeWithContent(writer, "uri", properties.getProperty("uri", ""));
//        for (TagInfo tagInfo : tagMap.values()) {
//            writeStartElement(writer, "tag");
//            writer.writeCharacters("\n");
//            writeWithContent(writer, "name", tagInfo.getName());
//            writeWithContent(writer, "tag-class", tagInfo.getTagClass());
//            writeWithContent(writer, "body-content", tagInfo.getBodyContentType().toString());
//            for (AttributeInfo attributeInfo : tagInfo.getAttributes()) {
//                writeStartElement(writer, "attribute");
//                writer.writeCharacters("\n");
//                writeWithContent(writer, "name", attributeInfo.getName());
//                writeWithContent(writer, "type", attributeInfo.getType());
//                writeWithContent(writer, "required", String.valueOf(attributeInfo.isRequired()));
//                writeWithContent(writer, "rtexprvalue", String.valueOf(attributeInfo.isRuntimeValue()));
//                writeWithContent(writer, "fragment", String.valueOf(attributeInfo.isJspFragment()));
//                writeEndElement(writer); //attribute
//            }
//            writeEndElement(writer); //tag
//        }
//
//        for (FunctionInfo functionInfo : functions) {
////            writeAsElement(function, "name", "functionClass", "functionSignature");
//
//            writeStartElement(writer, "function");
//            writer.writeCharacters("\n");
//            writeWithContent(writer, "name", functionInfo.getName());
//            writeWithContent(writer, "function-class", functionInfo.getFunctionClass());
//            writeWithContent(writer, "function-signature", functionInfo.getFunctionSignature());
//            writeEndElement(writer);
//        }
//
//        writeEndElement(writer); //tag-lib
//        writer.writeCharacters("\n");
//        writer.writeEndDocument();
//        writer.flush();
//        writer.close();
//    }
//
//    private void spacer(int level, XMLStreamWriter writer) throws XMLStreamException {
//        for (int i = 0; i < level; i++) {
//            for (int j = 0; j < 4; j++) {
//                writer.writeCharacters(" ");
//            }
//        }
//    }
//
//    private void writeWithContent(XMLStreamWriter writer, String tag, String content) throws XMLStreamException {
//        spacer(level, writer);
//        writer.writeStartElement(tag);
//        writer.writeCharacters(content);
//        writer.writeEndElement();
//        writer.writeCharacters("\n");
//    }
//
//    private void writeStartElement(XMLStreamWriter writer, String tag) throws XMLStreamException {
//        spacer(level, writer);
//        writer.writeStartElement(tag);
//        level++;
//    }
//
//    private void writeEndElement(XMLStreamWriter writer) throws XMLStreamException {
//        level--;
//        spacer(level, writer);
//        writer.writeEndElement();
//        writer.writeCharacters("\n");
//    }

    private class AnnotationMirrorWrapper {

        private AnnotationMirror mirror;

        public AnnotationMirrorWrapper(AnnotationMirror mirror) {
            this.mirror = mirror;
        }

        public AnnotationMirror getMirror() {
            return mirror;
        }
        
        public Object getValue(String annotationElement) {
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : processingEnv.getElementUtils().getElementValuesWithDefaults(mirror).entrySet()) {
                if (entry.getKey().getSimpleName().toString().equals(annotationElement)) {
                    return entry.getValue().getValue();
                }
            }
            return null;
        }
    }
}
