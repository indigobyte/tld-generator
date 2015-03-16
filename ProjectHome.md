# TLD Generator #

This is an annotation-based library to generate Tag Library Descriptors. The TLD is based in the schema specified in JSP 2.1 (see http://download.oracle.com/javaee/5/tutorial/doc/bnamu.html#bnank).

The annotations are only source annotations so you don't have to include the TLD Generator with the distribution of your tag library. And, because the TLD file is generated in META-INF, it will be already included in your jar file (no need to copy or move files).

## How to use ##

You just have to make sure the [TLD generator jar](http://code.google.com/p/tld-generator/downloads/detail?name=TLDGenerator.jar) is in the classpath of your project.

It requires JDK 6 and annotation processor invocation has to be enabled (by default it is).

**Maven Projects:** tld-generator can be included using the following dependency:

```
<dependency>
    <groupId>com.google.code.tld-generator</groupId>
    <artifactId>tld-generator</artifactId>
    <version>1.1</version>
    <scope>compile</scope>
    <optional>true</optional>
</dependency>
```

## How does it work ##

Most of the information in TLD can be deduced from implicit information (class name, method signature, attribute type) in your tag handlers and functions.
Additional information can be provided as values of the annotations.

An annotation processor is registered to be invoked automatically at compilation stage to process the annotations and generate the Tag Library Descriptor in the META-INF directory.

## Examples ##

Here is an example of a tag handler for printing a Hello message.

```
package example;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import tldgen.Tag;
import tldgen.TagAttribute;


@Tag
public class HelloTag extends SimpleTagSupport{
    private String name;

    @TagAttribute(required=true)
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public void doTag() throws JspException, IOException {
        getJspContext().getOut().print("Hello, "+name+"!");
    }
    
}
```


The example above uses annotations to indicate that this is a TagHandler with an empty body. The setter for attribute name has an annotation that defines it as required attribute.

We need to add some information about the uri of the tag library using a [package annotation](http://tech.puredanger.com/2007/02/28/package-annotations).
```
@TagLibrary("http://example.org")
package example;

import tldgen.TagLibrary;
```


This is an example of a function to be included in the descriptor file.

```
package example;

import tldgen.Function;

public class MyFunctions {
    
    @Function
    public static String reverse(String string){
        return new StringBuilder(string).reverse().toString();
    }
    
}
```

Now, just compile your project and TLD will be in the META-INF directory.

## Documentation ##

  * [TLD generator guide](guide.md).

  * [Javadoc](http://wiki.tld-generator.googlecode.com/hg/javadoc/index.html).