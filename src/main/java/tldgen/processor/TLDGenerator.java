/* Licensed under the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0) */
package tldgen.processor;

import tldgen.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
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
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Annotation proccesor for the generation of tag library descriptors. This annotation processor is registered
 * as a service so it's automatically invoked at compilation stage.
 *
 * @author Victor Hugo Herrera Maldonado
 */
@SupportedAnnotationTypes( {"tldgen.*", "javax.servlet.annotation.WebListener"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public final class TLDGenerator extends AbstractProcessor {
    private static Map<String, String> nativeTypes = new HashMap<String, String>();
    private HashMap<String, TagLibraryWrapper> librariesMap = new HashMap<String, TagLibraryWrapper>();
    private Pattern pattern = Pattern.compile("set(.+)");

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

    private static <S extends Annotation, T> void copyAnnotationValuesToBean(S annotation, T bean) {
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            try {
                String name = method.getName();
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                Method setter = bean.getClass().getMethod("set" + name, method.getReturnType());
                Object value = method.invoke(annotation);
                if (!value.equals("")) {
                    setter.invoke(bean, value);
                }
            } catch (NoSuchMethodException ex) {
                //do nothing
            } catch (IllegalAccessException ex) {
                //do nothing
            } catch (InvocationTargetException ex) {
                //do nothing
            }
        }
    }

    /**
     * Process the annotated elements to generate the TLD files.
     *
     * @param annotations
     * @param roundEnv
     * @return true
     */
    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(TagLibrary.class)) {
            AnnotationMirrorWrapper wrapper = getAnnotationMirrorWrapper(e, TagLibrary.class);
            PackageElement libraryPackage = processingEnv.getElementUtils().getPackageOf(e);
            TagLibraryInfo libraryInfo = new TagLibraryInfo(wrapper.getValueAsString("value"));
            copyAnnotationValuesToBean(wrapper.getMirror(), libraryInfo);
            TagLibraryWrapper library = new TagLibraryWrapper(wrapper.getValueAsString("descriptorFile"), libraryPackage, libraryInfo);

            library.getTagHandlerClasses().addAll(wrapper.getValueAsClassnameList("tagHandlers"));
            library.getFunctionClasses().addAll(wrapper.getValueAsClassnameList("functionClasses"));
            library.getWebListenerClasses().addAll(wrapper.getValueAsClassnameList("webListeners"));

            String validatorClassname = wrapper.getValueAsClassname("validator");
            if (!validatorClassname.equals("javax.servlet.jsp.tagext.TagLibraryValidator")) {
                library.setValidatorClass(validatorClassname);
            }
            for (AnnotationMirror mirror : wrapper.getValueAsAnnotationMirrorList("tagFiles")) {
                TagFileInfo tagFileInfo = new TagFileInfo();
                copyAnnotationValuesToBean(mirror, tagFileInfo);
                libraryInfo.getTagFiles().add(tagFileInfo);
            }
            if (librariesMap.containsKey(library.getDescriptorFile())) {
                processingEnv.getMessager().printMessage(Kind.ERROR,
                        "At least two libraries (" + librariesMap.get(library.getDescriptorFile()).getInfo().getUri() + " and " + library.getInfo().getUri() + ") have the same descriptor file: " + library.getDescriptorFile(), e);
            }
            librariesMap.put(library.getDescriptorFile(), library);
        }

        for (Element e : roundEnv.getElementsAnnotatedWith(Tag.class)) {
            if (e instanceof TypeElement && !e.getModifiers().contains(Modifier.ABSTRACT)) {
                TypeElement tElement = (TypeElement) e;
                AnnotationMirrorWrapper tagMirrorWrapper = getAnnotationMirrorWrapper(e, Tag.class);
                String tagName = null;
                if (!tagMirrorWrapper.getValueAsString("value").equals("")) {
                    tagName = tagMirrorWrapper.getValueAsString("value");
                } else {
                    tagName = e.getSimpleName().toString();
                    if (tagName.endsWith("Tag")) {
                        tagName = tagName.substring(0, tagName.length() - 3);
                    }
                }
                VariableElement value = (VariableElement) tagMirrorWrapper.getValue("bodyContentType");
                TagInfo tagInfo = new TagInfo(Introspector.decapitalize(tagName), e.asType().toString(), BodyContentType.valueOf(value.toString()));
                copyAnnotationValuesToBean(tagMirrorWrapper.getMirror(), tagInfo);

                if (!tagMirrorWrapper.getValueAsClassname("teiClass").equals("javax.servlet.jsp.tagext.TagExtraInfo")) {
                    tagInfo.setTeiClass(tagMirrorWrapper.getValueAsClassname("teiClass"));
                }

                for (AnnotationMirror mirror : tagMirrorWrapper.getValueAsAnnotationMirrorList("variables")) {
                    VariableInfo info = new VariableInfo();
                    AnnotationMirrorWrapper mirrorWrapper = new AnnotationMirrorWrapper(mirror);
                    copyAnnotationValuesToBean(mirrorWrapper.getMirror(), info);
                    info.setNameFromAttribute(mirrorWrapper.getValueAsString("nameFromAttribute"));
                    info.setNameGiven(mirrorWrapper.getValueAsString("nameGiven"));
                    if (info.getNameFromAttribute().equals("")) {
                        info.setNameFromAttribute(null);
                    }
                    if (info.getNameGiven().equals("")) {
                        info.setNameGiven(null);
                    }
                    if (info.getNameFromAttribute() != null && info.getNameGiven() != null) {
                        processingEnv.getMessager().printMessage(Kind.ERROR, "nameFromAttribute and nameGiven can not be presented at the same time.", e);
                    } else {
                        String type = mirrorWrapper.getValueAsClassname("type");
                        if (!type.equals("java.lang.String")) {
                            info.setVariableClass(type);
                        }
                        tagInfo.getVariables().add(info);
                    }
                }

                for (ExecutableElement m : ElementFilter.methodsIn(processingEnv.getElementUtils().getAllMembers(tElement))) {
                    if (isSetter(m) && m.getAnnotation(TagAttribute.class) != null) {
                        AnnotationMirror annotationMirror = getAnnotationMirror(m, TagAttribute.class);
                        String type = m.getParameters().get(0).asType().toString();
                        if (nativeTypes.containsKey(type)) {
                            type = nativeTypes.get(type);
                        }
                        AttributeInfo attributeInfo = new AttributeInfo(getAttributeName(m));
                        copyAnnotationValuesToBean(annotationMirror, attributeInfo);

                        attributeInfo.setType(type);
                        if (type.equals("javax.servlet.jsp.tagext.JspFragment")) {
                            attributeInfo.setRuntimeValueAllowed(true);
                            attributeInfo.setJspFragment(true);
                        } else {
                            attributeInfo.setJspFragment(false);
                        }

                        if (type.equals("javax.el.ValueExpression")) {
                            DeferredValueInfo info = new DeferredValueInfo();
                            AnnotationMirrorWrapper mirrorWrapper = getAnnotationMirrorWrapper(m, DeferredValue.class);
                            if (mirrorWrapper != null) {
                                info.setType(mirrorWrapper.getValueAsClassname("value"));
                            } else {
                                info.setType(Object.class.getName());
                            }
                            attributeInfo.setDeferredValue(info);
                        } else if (type.equals("javax.el.MethodExpression")) {
                            DeferredMethodInfo info = new DeferredMethodInfo();
                            attributeInfo.setDeferredMethod(info);
                            AnnotationMirrorWrapper mirrorWrapper = getAnnotationMirrorWrapper(m, DeferredMethod.class);
                            if (mirrorWrapper != null) {
                                info.setSignature(mirrorWrapper.getValueAsClassname("value"));
                            } else {
                                info.setSignature("void methodName()");
                            }
                            attributeInfo.setDeferredMethod(info);
                        }

                        AnnotationMirrorWrapper mirrorWrapper = getAnnotationMirrorWrapper(m, Variable.class);
                        if (mirrorWrapper != null) {
                            VariableInfo info = new VariableInfo();
                            copyAnnotationValuesToBean(mirrorWrapper.getMirror(), info);
                            info.setNameFromAttribute(attributeInfo.getName());
                            info.setNameGiven(null);
                            String classname = mirrorWrapper.getValueAsClassname("type");
                            info.setVariableClass(classname);
                            info.setScope(VariableScope.valueOf(mirrorWrapper.getValueAsString("scope")));
                            tagInfo.getVariables().add(info);
                        }
                        tagInfo.getAttributes().add(attributeInfo);
                    }
                }

                /* Look into the parent classes */
                do {
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
                } while (tElement != null);

                for (TagLibraryWrapper library : librariesMap.values()) {
                    if (library.getTagHandlerClasses().contains(tagInfo.getTagClass()) ||
                            (library.getTagHandlerClasses().isEmpty() && haveSamePackage(library, e))) {
                        library.getInfo().getTagHandlers().add(tagInfo);
                    }
                }
            }

        }

        for (Element e : roundEnv.getElementsAnnotatedWith(Function.class)) {
            if (e.getModifiers().contains(Modifier.STATIC) && e.getModifiers().contains(Modifier.PUBLIC)) {
                AnnotationMirrorWrapper functionMirrorWrapper = new AnnotationMirrorWrapper(getAnnotationMirror(e, Function.class));
                String functionName = functionMirrorWrapper.getValueAsString("value");
                if (functionName.equals("")) {
                    functionName = e.getSimpleName().toString();
                }
                FunctionInfo info = new FunctionInfo(functionName, e.getEnclosingElement().asType().toString(), getSignature((ExecutableElement) e));
                copyAnnotationValuesToBean(functionMirrorWrapper.getMirror(), info);
                for (TagLibraryWrapper library : librariesMap.values()) {
                    if (library.getFunctionClasses().contains(info.getFunctionClass()) ||
                            (library.getFunctionClasses().isEmpty() && haveSamePackage(library, e))) {
                        library.getInfo().getFunctions().add(info);
                    }
                }
            } else {
                processingEnv.getMessager().printMessage(Kind.WARNING, "Function method must be public static. This will be omited in declaration.", e);
            }
        }

        for (Element e : roundEnv.getElementsAnnotatedWith(Validator.class)) {
            Validator validator = e.getAnnotation(Validator.class);
            ValidatorInfo validatorInfo = new ValidatorInfo();
            validatorInfo.setValidatorClass(e.asType().toString());
            for (InitParam param : validator.initParams()) {
                ParameterInfo paramInfo = new ParameterInfo();
                copyAnnotationValuesToBean(param, paramInfo);
                validatorInfo.getParameters().add(paramInfo);
            }
            for (TagLibraryWrapper library : librariesMap.values()) {
                if ((library.getValidatorClass() != null && library.getValidatorClass().equals(validatorInfo.getValidatorClass())) ||
                        (library.getValidatorClass() == null && haveSamePackage(library, e))) {
                    if (library.getInfo().getValidator() != null) {
                        processingEnv.getMessager().printMessage(Kind.ERROR, "There can be only one validator per tag library.");
                    } else {
                        library.getInfo().setValidator(validatorInfo);
                    }
                }
            }
        }
        try {
            Class<?> webListenerAnnotationType = Class.forName("javax.servlet.annotation.WebListener");
            for (Element e : roundEnv.getElementsAnnotatedWith((Class<WebListener>) webListenerAnnotationType)) {
                WebListenerInfo webListenerInfo = new WebListenerInfo(e.asType().toString());
                for (TagLibraryWrapper library : librariesMap.values()) {
                    if (library.getWebListenerClasses().contains(webListenerInfo.getListenerClass()) ||
                            (library.getWebListenerClasses().isEmpty() && haveSamePackage(library, e))) {
                        library.getInfo().getWebListeners().add(webListenerInfo);
                    }
                }
            }
        } catch (ClassNotFoundException ex) {

        }


        if (roundEnv.processingOver()) {
            try {
                for (TagLibraryWrapper library : librariesMap.values()) {
                    generateXML(library.getInfo(), library.getDescriptorFile());
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

    private String getAttributeName(ExecutableElement element) {
        Matcher matcher = pattern.matcher(element.getSimpleName().toString());
        matcher.matches();
        return Introspector.decapitalize(matcher.group(1));
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

    private boolean haveSamePackage(TagLibraryWrapper library, Element e) {
        return library.getPackage().getQualifiedName().equals(processingEnv.getElementUtils().getPackageOf(e).getQualifiedName());
    }

    private AnnotationMirrorWrapper getAnnotationMirrorWrapper(Element e, Class<? extends Annotation> klass) {
        AnnotationMirror mirror = getAnnotationMirror(e, klass);
        return mirror != null ? new AnnotationMirrorWrapper(mirror) : null;
    }

    private AnnotationMirror getAnnotationMirror(Element e, Class<? extends Annotation> klass) {
        for (AnnotationMirror mirror : e.getAnnotationMirrors()) {
            if (mirror.getAnnotationType().toString().equals(klass.getName())) {
                return mirror;
            }
        }
        return null;
    }

    private <T> void copyAnnotationValuesToBean(AnnotationMirror annotationMirror, T bean) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> annotationValues = processingEnv.getElementUtils().getElementValuesWithDefaults(annotationMirror);
        try {
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors()) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationValues.entrySet()) {
                    if (entry.getKey().getSimpleName().contentEquals(descriptor.getName())) {
                        Object value = entry.getValue().getValue();
                        if (descriptor.getPropertyType().isEnum()) {
                            for (Object enumConstant : descriptor.getPropertyType().getEnumConstants()) {
                                if (enumConstant.toString().equals(entry.getValue().getValue().toString())) {
                                    value = enumConstant;
                                    break;
                                }
                            }
                        }
                        if (!value.equals("") && descriptor.getWriteMethod() != null && value.getClass().equals(descriptor.getPropertyType())) {
                            descriptor.getWriteMethod().invoke(bean, value);
                        }
                        break;
                    }
                }
            }
        } catch (IllegalAccessException ex) {
            //do nothing
        } catch (InvocationTargetException ex) {
            //do nothing
        } catch (IntrospectionException ex) {
            //do nothing
        }
    }

    private void generateXML(TagLibraryInfo libraryInfo, String descriptorFile) throws JAXBException, IOException {
        // Taken from https://stackoverflow.com/a/61012531/156973
        AtomicReference<IOException> ioException = new AtomicReference<>();
        AtomicReference<JAXBException> jaxbException = new AtomicReference<>();
        CompletableFuture.runAsync(() -> {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader()); // Workaround for class loader
            try {
                FileObject file = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/" + descriptorFile, new Element[0]);

                JAXBContext context = JAXBContext.newInstance(TagLibraryInfo.class);
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                m.setProperty(Marshaller.JAXB_FRAGMENT, true);
                m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
                Writer writer = file.openWriter();
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                m.setListener(new DefaultValueCleaner());
                m.marshal(libraryInfo, writer);
                writer.close();
            } catch (IOException e) {
                ioException.set(e);
            } catch (JAXBException e) {
                jaxbException.set(e);
            }
            // now you have the right ClassLoader here
        }).join();
        if (ioException.get() != null) {
            throw ioException.get();
        }
        if (jaxbException.get() != null) {
            throw jaxbException.get();
        }
    }

    private class AnnotationMirrorWrapper {

        private AnnotationMirror mirror;

        public AnnotationMirrorWrapper(AnnotationMirror mirror) {
            this.mirror = mirror;
        }

        public AnnotationMirror getMirror() {
            return mirror;
        }

        public Object getValue(String annotationAttributeName) {
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : processingEnv.getElementUtils().getElementValuesWithDefaults(mirror).entrySet()) {
                if (entry.getKey().getSimpleName().toString().equals(annotationAttributeName)) {
                    return entry.getValue().getValue();
                }
            }
            return null;
        }

        public String getValueAsString(String annotationAttributeName) {
            Object value = getValue(annotationAttributeName);
            return value != null ? value.toString() : null;
        }

        public String getValueAsClassname(String annotationAttributeName) {
            Object o = getValue(annotationAttributeName);
            return o != null ? o.toString().replace(".class", "") : null;
        }

        public List<?> getValueAsList(String annotationAttributeName) {
            return (List<?>) getValue(annotationAttributeName);
        }

        public List<AnnotationMirror> getValueAsAnnotationMirrorList(String annotationAttributeName) {
            return (List<AnnotationMirror>) getValue(annotationAttributeName);
        }

        public List<String> getValueAsClassnameList(String annotationAttributeName) {
            List<String> classnames = new LinkedList<String>();
            for (Object o : getValueAsList(annotationAttributeName)) {
                classnames.add(o.toString().replace(".class", ""));
            }
            return classnames;
        }

    }

}
