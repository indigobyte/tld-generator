# TLD Generator Guide #

## Contents ##


## Configuration ##

To use the library, just include the jar in the classpath, or if your using an IDE, add it to your project.

## Tag library ##

Tag library is the main element of the descriptor.

TagLibrary annotation is used to declare a descriptor. This is a [package annotation](http://tech.puredanger.com/2007/02/28/package-annotations) and supports the followings attributes:

  * value. URI of the library
  * description (optional)
  * shortName (optional)
  * displayName (optional)
  * libraryVersion (default is 1.0)
  * jspVersion (default is 2.1)
  * icon (optional)
  * descriptorFile. This is the name for the generated TLD file. TLD files are generated in META-INF directory. The default value is taglib.tld.

Example:

```
@TagLibrary(value="http://example-guide.org", libraryVersion="1.1", description="Example tags")
package example.guide;

import tldgen.TagLibrary;
```

### Tag library explicit elements ###

A tag library can define explicitly an array of tag handlers, function classes, listeners and validator. When elements are defined is this way, only the listed elements are included in the descriptor no matter what package they have.

In the case of this elements are not set in the annotation, only elements with the same package as the element with TagLibrary are considered in the descriptor.

Example with implicit declaration:
```
@TagLibrary("http://example-guide.org")
package example.guide;

import tldgen.TagLibrary;
```


Example with explicit declaration:
```
@TagLibrary(value="http://example-guide.org", 
     tagHandlers={ExampleTag.class, Example2Tag.class},
     functionClasses=FunctionExample.class)
package example.guide;

import tldgen.TagLibrary;
```


## Tag Handlers ##

A tag handler class is the java class where the logic of the tag is implemented. Tag annotation to indicate a tag handler. The annotation attributes are:

  * name (if not specified, the name will be the simple name of the class removing the suffix Tag if present).
  * description (optional)
  * displayName (optional)
  * icon (optional)
  * example (optional)
  * bodyContentType (default is EMPTY)
  * teiClass (optional)
  * variables (optional)

Example of tag handler:

```
package example.guide;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import tldgen.BodyContentType;
import tldgen.Tag;

@Tag(bodyContentType= BodyContentType.SCRIPTLESS, teiClass=ExampleTagExtraInfo.class)
public class ExampleTag extends SimpleTagSupport{

    @Override
    public void doTag() throws JspException, IOException {
        super.doTag();
    }
    
}
```

### Variables ###
You can declare variable for a Tag.

Tag annotation may define an array of Variable annotation.

Variable annotation has:

  * description (optional)
  * nameGiven
  * nameFromAttribute
  * type (default is String.class)
  * declare (default is true)
  * scope (default is NESTED)

```
package example.guide;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import tldgen.Tag;
import tldgen.Variable;
import tldgen.VariableScope;

@Tag(variables={@Variable(nameGiven="result", type=Integer.class, scope= VariableScope.AT_END)})
public class Example2Tag extends BodyTagSupport{

    @Override
    public int doEndTag() throws JspException {
        return BodyTag.EVAL_PAGE;
    }
    
}
```

### Tag Attributes ###

An attribute is annotated with TagAttribute. The annotation can specify:

  * required (default is false)
  * description (optional)
  * runtimeValueAllowed (default is false)

Example of a tag attribute:

```
package example.guide;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import tldgen.Tag;
import tldgen.TagAttribute;

@Tag
public class Example3Tag extends SimpleTagSupport{
    private String messsage;
    
    @TagAttribute(required=true, description="message to out", runtimeValueAllowed=true)
    public void setMessage(String m){
        messsage=m;
    }
    

    @Override
    public void doTag() throws JspException, IOException {
        getJspContext().getOut().print(messsage);
    }
    
}

```

Attributes with a type of javax.el.ValueExpression are marked as deferred values. If you want to mark it explicitly or define the type of value that expression return, you can use
DeferredValue annotation.

Attributes with a type of javax.el.MethodExpression are marked as deferred methods. If you want to mark it explicitly or define the signature of method, you can use
DeferredMethod annotation.

Example:
```
package example.guide;

import java.io.IOException;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import tldgen.DeferredValue;
import tldgen.Tag;
import tldgen.TagAttribute;

@Tag
public class Example4Tag extends SimpleTagSupport{
    private ValueExpression exp;

    @TagAttribute(required=true)
    @DeferredValue(String.class)
    public void setExp(ValueExpression exp) {
        this.exp = exp;
    }
    
    @Override
    public void doTag() throws JspException, IOException {
        getJspContext().getOut().print(exp.getValue(getJspContext().getELContext()));
    }
    
}
```

An attribute can be annotated with Variable to specify that this attribute generates a variable.

Example:
```
package example.guide;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import tldgen.Tag;
import tldgen.TagAttribute;
import tldgen.Variable;

@Tag
public class Example5Tag extends TagSupport{
    private String varName;

    @TagAttribute(required=true)
    @Variable
    public void setVarName(String varName) {
        this.varName = varName;
    }
    
    @Override
    public int doEndTag() throws JspException {
        pageContext.setAttribute(varName, "Hello, world!");
        return TagSupport.EVAL_PAGE;
    }
    
}
```

## Functions ##

The function annotation is used to define a function. Its attributes are:

  * description (optional)
  * displayName (optional)
  * example (optional)
  * icon (optional)
  * value The name of the function, but if it's not specified, the name will be the name of the method.

```
package example.guide;

import tldgen.Function;

public class FunctionExample {
    
    @Function(description="Reverse a String", example="reverse('Hello, world')")
    public static String reverse(String string){
        return new StringBuilder(string).reverse().toString();
    }
    
}

```

## Tag Files ##

A tag file is an implementation of the functionality of a tag, but instead of using a class, JSP syntax is used.

To define a tag file use TagFile annotation. The attributes of the annotation are:

  * description (optional)
  * displayName (optional)
  * icon (optional)
  * name
  * path
  * example (optional)

The tag files have to be defined in the attribute tagFiles of TagLibrary annotation.

Example:

```
@TagLibrary(value="http://example-guide.org", libraryVersion="1.1", description="Example tags"
        , tagFiles={@TagFile(name="greeting", path="META-INF/tags/greeting.tag")})
package example.guide;

import tldgen.TagFile;
import tldgen.TagLibrary;
```

## Web Listener ##

A tag library can contain Web Listeners. The standard annotation javax.servlet.annotation.WebListener is recognized by the generator.

Example:

```
package example.guide;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyServletContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("init");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("destroy");
    }
    
}
```

## Tag Library Validator ##

A tag validator is a class to specify additional validations for the tags.

Validator annotation is used in the class. Only one validator can be attached to the library, so you have to beware of this.

Attributes of Validator annotation are:

  * initParams (optional)

Example:

```
package example.guide;

import javax.servlet.jsp.tagext.PageData;
import javax.servlet.jsp.tagext.TagLibraryValidator;
import javax.servlet.jsp.tagext.ValidationMessage;
import tldgen.InitParam;
import tldgen.Validator;

@Validator(initParams={@InitParam(name="admin.email", value="admin@server.org")})
public class MyTagLibraryValidator extends TagLibraryValidator{

    @Override
    public ValidationMessage[] validate(String prefix, String uri, PageData page) {
        return super.validate(prefix, uri, page);
    }
    
}
```