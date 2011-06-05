package tldgen.processor;

import java.util.LinkedList;
import java.util.List;
import javax.lang.model.element.PackageElement;

/**
 *
 * @author Victor Hugo Herrera Maldonado
 */
class TagLibraryWrapper {
    private PackageElement packageElement;
    private TagLibraryInfo tagLibraryInfo;
    private List<String> tagHandlersClasses=new LinkedList<String>();
    private List<String> functionClasses=new LinkedList<String>();
    private List<String> webListenerClasses=new LinkedList<String>();
    private String validatorClass;
    private String descriptorFile;

    public TagLibraryWrapper(String descriptorFile, PackageElement packageElement, TagLibraryInfo tagLibraryInfo) {
        this.packageElement = packageElement;
        this.tagLibraryInfo = tagLibraryInfo;
        this.descriptorFile=descriptorFile;
    }

    public String getDescriptorFile() {
        return descriptorFile;
    }
    
    public PackageElement getPackage(){
        return packageElement;
    }
    
    public List<String> getTagHandlerClasses(){
        return tagHandlersClasses;
    }
    
    public List<String> getFunctionClasses(){
        return functionClasses;
    }

    public List<String> getWebListenerClasses() {
        return webListenerClasses;
    }

    public String getValidatorClass() {
        return validatorClass;
    }

    public void setValidatorClass(String validatorClass) {
        this.validatorClass = validatorClass;
    }
    
    public TagLibraryInfo getInfo(){
        return tagLibraryInfo;
    }
    
}
