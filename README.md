[（中文说明）](https://github.com/Megre/CodeFactExtractor/blob/master/readme/说明.md)

### Content
- [What's CodeFactExtractor](#whats-codefactextractor)
- [Basics](#basics)
    - [OWL](#owl)
    - [Facts](#facts)
    - [Inference](#inference)
    - [SPARQL Query/Update Language](#sparql-queryupdate-language)
- [Fact Extraction](#fact-extraction)
    - [Visitors](#visitors)
    - [Property Handlers](#property-handlers)
    - [Naming Individuals](#naming-individuals)
    - [Visitor Layout](#visitor-layout)
    - [Extending CodeFactExtractor](#extending-codefactextractor)

What's CodeFactExtractor
----------
[CodeFactExtractor](https://github.com/Megre/CodeFactExtractor) is a lightweight framework to extract facts from source code for code search and inference. [CodeFactExtractor](https://github.com/Megre/CodeFactExtractor) assists both static and dynamic analysis in several ways:

- assisting normal static analysis tasks, e.g. control flow analysis, data flow analysis and call graph analysis.
- searching code elements based on self-defined rules or mechanisms of a programming language.
- inferring relations of code elements, e.g. implementation, inheritance, override, and overload.
- identifying design patterns, e.g. the 23 classic GoF (Gang of Four) design patterns and emerging ones.
- detecting smells, anti-patterns, and defects, e.g. smells such as Parallel Inheritance Hierarchies, Message Chains, Middle Man, and Data Class, and anti-patterns such as Call Super, Circular Dependency, Constant Interface, Circle-Ellipse Problem, and Object Orgy.
- encapsulating temporal information to infer runtime behaviors, e.g. the invocation sequence of methods and the transmission of data (e.g. by monitoring field modification).

Basics
----------
### OWL 
The W3C <a href="https://www.w3.org/2001/sw/wiki/OWL">Web Ontology Language (OWL)</a> is a Semantic Web language designed to represent rich and complex knowledge about things, groups of things, and relations between things.

In OWL, a `class` represents a group of elements. An instance of a class is called an `individual` belonging to the class. `Object property` represents the relation between individuals. `Data type properties` associate individuals with data type values. The subclass relation defines the hierarchy of classes, and the subproperty relation defines the hierarchy of attributes.

A ".owl" file is a text file usually saved as RDF/XML syntax.

### Facts

Facts represent knowledge in form of `triples`. A triple is in form of:

(Subject *predicate* Object)

##### Example 1
    
    (exam:Lily exam:fatherIs exam:Johnson)
    (exam:Lily rdf:type exam:Person)
    (exam:Lily exam:ageIs 10)
    (exam:Lily exam:hasBrother exam:Tom)

Lily, Johnson, and Tom are all individuals whose type (*`rdf:type`*) is Person. 

The properties *`exam:fatherIs`*, *`rdf:type`*, *`exam:ageIs`*, and *`exam:hasBrother`* links individuals to other individuals or literal. While *`exam:fatherIs`*, *`exam:hasBrother`*, and *`rdf:type`* are all object properties, *`exam:ageIs`* is a data  type property.

The prefix "rdf" is a XML namespace defined as "<a href="http://www.w3.org/1999/02/22-rdf-syntax-ns#" target="_blank">http://www.w3.org/1999/02/22-rdf-syntax-ns#</a>", where the property *`rdf:type`* is defined.

Another user-defined namespace "exam" is defined as "[http://www.spart.group/exam#](https://github.com/Megre/CodeFactExtractor/blob/master/readme/exam.owl)", where the ontology class *`exam:PersonIs`*, and the properties *`exam:fatherIs`*, *`exam:ageIs`*, and *`exam:hasBrother`* are defined.

The facts in [Example 1](#example-1) can be saved in RDF/XML format in the following [exam.owl](https://github.com/Megre/CodeFactExtractor/blob/master/readme/exam.owl) file:

    <?xml version="1.0"?>
    <rdf:RDF xmlns="http://www.spart.group/exam#"
        xml:base="http://www.spart.group/exam"
        xmlns:owl="http://www.w3.org/2002/07/owl#"
        xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
        xmlns:xml="http://www.w3.org/XML/1998/namespace"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
        xmlns:exam="http://www.spart.group/exam#"
        xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#">
        
        <owl:Ontology rdf:about="http://www.spart.group/exam"/>
    
        <owl:ObjectProperty rdf:about="http://www.spart.group/exam#hasBrother"/>
        <owl:ObjectProperty rdf:about="http://www.spart.group/exam#fatherIs"/>
        <owl:ObjectProperty rdf:about="http://www.spart.group/exam#hasSister"/>
        <owl:DatatypeProperty rdf:about="http://www.spart.group/exam#ageIs"/>
        
        <owl:Class rdf:about="http://www.spart.group/exam#Person"/>
    
        <owl:NamedIndividual rdf:about="http://www.spart.group/exam#Johnson">
            <rdf:type rdf:resource="http://www.spart.group/exam#Person"/>
        </owl:NamedIndividual>
    
        <owl:NamedIndividual rdf:about="http://www.spart.group/exam#Lily">
            <rdf:type rdf:resource="http://www.spart.group/exam#Person"/>
            <hasBrother rdf:resource="http://www.spart.group/exam#Tom"/>
            <fatherIs rdf:resource="http://www.spart.group/exam#Johnson"/>
            <ageIs rdf:datatype="http://www.w3.org/2001/XMLSchema#integer">10</age>
        </owl:NamedIndividual>
        
        <owl:NamedIndividual rdf:about="http://www.spart.group/exam#Tom">
            <rdf:type rdf:resource="http://www.spart.group/exam#Person"/>
        </owl:NamedIndividual>
        
    </rdf:RDF>

To model the the concepts (e.g. ontology classes) and relations (e.g. properties) as shown in [exam.owl](https://github.com/Megre/CodeFactExtractor/blob/master/readme/exam.owl), the [Protege](https://protege.stanford.edu) tool is recommended instead of creating "exam.owl" by editing the text. Based on the OWL model, [CodeFactExtractor](https://github.com/Megre/CodeFactExtractor) assists the extraction of facts from source code. 

### Inference

Base on the facts in [Example 1](#example-1), the following facts can be inferred:

    (exam:Tom exam:fatherIs exam:Johnson)
    (exam:Tom exam:hasSister exam:Lily)

The inference can be represented as:

    (exam:Tom exam:fatherIs exam:Johnson), (exam:Tom exam:hasSister exam:Lily) ← (exam:Lily exam:fatherIs exam:Johnson), (exam:Lily exam:hasBrother exam:Tom)

Given another person Lucy, it's not known whether 

    (exam:Lucy exam:teacherIs exam:Lily)

can be inferred or not based on the facts in [Example 1](#example-1). Such inference strategy is called `Open World Assumption (OWA)`. In the other strategy, `Closed World Assumption (CWA)`, an assertion's negative is inferred if it's not known whether the assertion is true or not. Thus, "Lucy is not Lily's teacher" is inferred. OWA is usually more reasonable because other facts may exist somewhere else. 

But the strategy of [CodeFactExtractor](https://github.com/Megre/CodeFactExtractor) works in a user-defined way. For example, if the fact

    (any-public-method exam:hasModifier "public")

is asserted for each public method of each class/interface during the extraction, it can be safely asserted that the methods that are not linked to "public" by *`exam:hasModifier`* are non-public methods. (In Java, the access permission of every method of an interface is public even if the methods are not declared as public. Such methods are also considered as non-public methods since *`exam:hasModifier`* indicates the modifier declared in the source code.)

Based on the facts in [Example 1](#example-1), other facts also can be inferred such as "Johnson has two children at least", "Johnson has a son Tom", and "Tom is younger than Johnson", etc. These facts are inferred according to the common sense. In [CodeFactExtractor](https://github.com/Megre/CodeFactExtractor), the inference is usually according to the programming mechanisms of a programming language. For example, if two methods of a class has the same name but different signatures, a overload relation of the two methods can be inferred. A property (e.g. *`exam:overloads`*) can be created to represent the relation.

An important inference strategy is reusing exiting relations to build more abstract relations. Given the following facts in addition to [Example 1](#example-1):

    (exam:Johnson exam:fatherIs exam:George)
    (exam:Tom exam:ageIs 12)
    
the following facts can be inferred:
    
    (exam:Lily exam:anElderIs exam:Tom)
    (exam:Tom exam:anElderIs exam:Johnson)
    (exam:Tom exam:anElderIs exam:George)
    ...
    
The property *`exam:anElderIs`* is more general than the properties *`exam:fatherIs`* since a father is always an elder of his son or daughter; on the contrary, an elder-relation not only indicates a son-father/daughter-father relation, but may also indicate a younger-sister-elder-brother relation or a grandson-grandfather relation. The following rule can be used to describe the elder-relation:

    (?x exam:anElderIs ?z) ← (?x exam:anElderIs ?y), (?y exam:anElderIs ?z)
    
which means *`exam:anElderIs`* is a transitive property.

Such inference strategy is called `generalization` which raises the abstraction level by composing existing properties or imposing restrictions to existing properties. What and How to infer depends on your needs based on the clear definition of the meaning of each property.

### SPARQL Query/Update Language

[RDF (Resource Description Framework)](https://www.w3.org/RDF/) is a standard model for data interchange on the Web. The linking structure of RDF uses [URIs (Universal Resource Identifiers)](https://www.w3.org/wiki/URI) to name things and relations as in a triple, e.g. (exam:Tom *`exam:fatherIs`* exam:Johnson). 

This linking structure forms a directed, labeled graph, where the edges represent the named link  (property) between two resources (represented by the graph nodes). This graph view is the easiest possible mental model for RDF and is often used in easy-to-understand visual explanations.

[SPARQL (Simple Protocol and RDF Query Language)](https://www.w3.org/TR/sparql11-query/) is a query language for RDF. SPARQL contains capabilities for querying required and optional graph patterns along with their conjunctions and disjunctions. SPARQL also supports aggregation, subqueries, negation, creating values by expressions, extensible value testing, and constraining queries by source RDF graph. 

##### Example 2

    prefix exam: <http://www.spart.group/exam#>
    
    select ?x 
    where {
        ?x exam:fatherIs exam:Johnson
    }

This query retrieves the children whose father is Johnson. The query results are bound to the variable `x` prefixed with a question mark(`?`).

##### Example 3

    prefix exam: <http://www.spart.group/exam#>
    
    insert {
        ?x exam:anElderIs ?y
    } 
    where {
        { ?x exam:fatherIs+ ?y }
        union
        { ?x exam:hasBrother ?y.  ?x exam:ageIs ?ageX. ?y exam:ageIs ?ageY.
          filter(?ageX < ?ageY)
        }
        # other situations
        # ...
    }

This update infers *`exam:anElderIs`* from child-father relation and sister-brother relation and insert new facts to RDF graph. There are also other situations from which *`exam:anElderIs`* can be inferred.

The plus sign (`+`) postfixed to *`exam:anElderIs`* matches one or more *`exam:anElderIs`*, which means there is a directed path between the two linked nodes along one or more *`exam:anElderIs`*.

##### Example 4

    prefix exam: <http://www.spart.group/exam#>
    
    insert {
        ?x exam:anElderIs ?y
    } 
    where {
        ?x exam:anElderIs+ ?y
    }
    
This update represents the inference of the transitive property *`exam:anElderIs`*. 

Fact Extraction
----------

### Visitors
[CodeFactExtractor](https://github.com/Megre/CodeFactExtractor) is based on the Visitor design pattern. The framework traverses the AST (Abstract Syntax Tree), during which each AST node accepts registered visitors and as a result the visit method of each visitor is invoked. 

Individuals are generated during the invocation of visit methods. Each visit method corresponds to one type of AST node which is used to generate individuals of an ontology class.

### Property Handlers
Each property handler processes one property which links an individual to another. A property handler is initialized by a visitor's visit method with the instances of the visitor and current AST node. Then the handle method of the handler is invoked to generate facts using the property. 

### Naming Individuals
Visitors and property handlers need a unique name to create or retrieve an individual. An individual corresponding to an AST node should have a unique name, because the individual may be created in different visitors or property handlers. Creating an individual with the same name of an existing individual will reuse the existing individual. Thus, a visitor or property handler doesn't need to check whether an individual already exists. 

### Visitor Layout
The Visitor layout specifies the visitors and property handlers that are invoked by the framework. The visitor layout is specified in a configuration file, e.g. [visitor_layout.cfg](https://github.com/Megre/CodeFactExtractor/blob/master/data/visitor_layout.cfg):

    [package]
    visitor = group.spart.kg.java.visitor 
        # the package name of java visitors
    handler = group.spart.kg.java.prop 
        # the package name of java property handlers
    
    [visitor]
    name = EntryVisitor 
        # the first visitor of the configuration file is the entry visitor, 
        # i.e., group.spart.kg.java.visitor.EntryVisitor
    namespace = java 
        # the default prefix used to create individuals
    context = TypeDeclaration 
        # type of visited AST node, i.e., org.eclipse.jdt.core.dom.TypeDeclaration
    child = MethodDeclarationVisitor, AnonymousClassDeclarationVisitor, ArrayTypeVisitor
    child = FieldDeclarationVisitor
        # currently visited AST node will accept the child visitors. 
        # These visitors also needs to be specfied in the configuration file.
    
    # ...
    
    [property]
    name = hasMethod
        # name of the property
    namespace = java
        # the default prefix used to create individuals or retrieve propertyies, i.e., java:hasMethod
    handler = HasMethodHandler
        # the property handler that process the property java:hasMethod, 
        # i.e., group.spart.kg.java.prop.HasMethodHandler
    context = MethodDeclaration
        # type of AST node. The framework invokes property handlers in the visit method of visitors. 
        # If the type of visited AST node matches the context of a property handler, 
        # the property handler will be initialized by the AST node visited, after which 
        # the handler's handle method will be invoked.
    
    # ...

The configuration uses a # to start a comment. Inline comments are supported. 

There are three types of configuration item: [package], [visitor], and [property] (see  [group.spart.kg.layout.ConfigItem](https://github.com/Megre/CodeFactExtractor/blob/master/src/group/spart/kg/layout/ConfigItem.java)).  A configuration item is composed of several lines, each of which is a key-value pair separated by "=". A key is a plain text; a value may be a single plain string, a list (e.g. "value1, value2, value3"), or a multiple-list (e.g. "value1; value2, value3, value4; value5, value6"), (see [group.spart.kg.layout.ConfigItemValue](https://github.com/Megre/CodeFactExtractor/blob/master/src/group/spart/kg/layout/ConfigItemValue.java)). Some keys (e.g. "child") may occur more than once within a configuration item. Such keys are equal to a list or multiple-list (depends on the value type of the repetitive keys' first key) in case that the key-value line is too long.

### Extending CodeFactExtractor

To extend the framework, you need to add a visitor or property handler, then configure it in the visitor layout. For Java, a visitor must extend [group.spart.kg.java.visitor.AbstractASTNodeVisitor](https://github.com/Megre/CodeFactExtractor/blob/master/java/group/spart/kg/java/visitor/AbstractASTNodeVisitor.java); a property handler must extend [group.spart.kg.java.prop.AbstractPropertyHandler](https://github.com/Megre/CodeFactExtractor/blob/master/java/group/spart/kg/java/prop/AbstractPropertyHandler.java).

A visitor item or property item should be added to the visitor layout to configure a visitor or property handler. The extraction starts from the first visitor of the visitor layout (i.e., the entry visitor), which recursively links to all other visitors by specifying the "child" key.

Further, if the configured child visitors are expected to be processed, the processChildVisitors method of [AbstractASTNodeVisitor](https://github.com/Megre/CodeFactExtractor/blob/master/java/group/spart/kg/java/visitor/AbstractASTNodeVisitor.java) should be called within the visitor's visit method. Similarly, if the properties, whose context matches the currently visited node, are expected to be handled, the handleProperties method of [AbstractASTNodeVisitor](https://github.com/Megre/CodeFactExtractor/blob/master/java/group/spart/kg/java/visitor/AbstractASTNodeVisitor.java) should also be called within the visitor's visit method. The framework will load the visitor layout and invoke all visitors and handlers as specified.
		
