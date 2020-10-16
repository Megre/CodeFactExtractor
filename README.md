[（中文说明）](#CodeFactExtractor是什么)

What's CodeFactExtractor
----------
[CodeFactExtractor](https://github.com/Megre/CodeFactExtractor) is a lightweight framework to extract facts from source code for code search and inference. [CodeFactExtractor](https://github.com/Megre/CodeFactExtractor) assists both static and dynamic analysis in several ways:

- assisting normal static analysis tasks, e.g. control flow analysis, data flow analysis and call graph analysis.
- searching code elements based on self-defined rules or mechanisms of a programming language.
- inferring relationships of code elements, e.g. implementation, inheritance, override, and overload.
- identifying design patterns, e.g. the 23 classic GoF (Gang of Four) design patterns and emerging ones.
- detecting smells, anti-patterns, and defects, e.g. smells such as Parallel Inheritance Hierarchies, Message Chains, Middle Man, and Data Class, and anti-patterns such as Call Super, Circular Dependency, Constant Interface, Circle-Ellipse Problem, and Object Orgy.
- encapsulating temporal information to infer runtime behaviors, e.g. the invocation sequence of methods and the transmission of data (e.g. through assignment such as field modification).

Basics
----------
### OWL 
The W3C <a href="https://www.w3.org/2001/sw/wiki/OWL">Web Ontology Language (OWL)</a> is a Semantic Web language designed to represent rich and complex knowledge about things, groups of things, and relations between things.

In OWL, a `class` represents a group of elements. An instance of a class is called an `individual` belonging to the class. `Object property` represents the relationship between individuals. `Data type properties` associate individuals with data type values. The subclass relationship defines the hierarchy of classes, and the subproperty relationship defines the hierarchy of attributes.

A ".owl" file is a text file usually saved as RDF/XML syntax.

### Facts

Facts represent knowledge in form of `triples`. A triple is in form of:

(Subject *predicate* Object)

`Example 1`

> (exam:Lily *exam:fatherIs* exam:Johnson), (exam:Lily *rdf:type* exam:Person), (exam:Lily *exam:ageIs* 10), (exam:Lily *exam:brotherIs* exam:Tom)

Lily, Johnson, and Tom are all individuals whose type (*rdf:type*) is Person. 

The properties *`exam:fatherIs`*, *`rdf:type`*, *`exam:ageIs`*, and *`exam:brotherIs`* links individuals to other individuals or literal. While *`exam:fatherIs`*, *`exam:brotherIs`*, and *`rdf:type`* are all object properties, *`exam:ageIs`* is a data  type property.

The prefix "rdf" is a XML namespace defined as "<a href="http://www.w3.org/1999/02/22-rdf-syntax-ns#">http://www.w3.org/1999/02/22-rdf-syntax-ns#</a>", where the property *`rdf:type`* is defined.

Another user-defined namespace "exam" is defined as "http://www.spart.group/exam#", where the ontology class *`exam:PersonIs`*, and the properties *`exam:fatherIs`*, *`exam:ageIs`*, and *`exam:brotherIs`* are defined.

The facts in `Example 1` can be saved in RDF/XML format in the following "<a href="readme/exam.owl">exam.owl</a>" file:

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
    
        <owl:ObjectProperty rdf:about="http://www.spart.group/exam#brotherIs"/>
        <owl:ObjectProperty rdf:about="http://www.spart.group/exam#fatherIs"/>
        <owl:ObjectProperty rdf:about="http://www.spart.group/exam#sisterIs"/>
        <owl:DatatypeProperty rdf:about="http://www.spart.group/exam#ageIs"/>
        
        <owl:Class rdf:about="http://www.spart.group/exam#Person"/>
    
        <owl:NamedIndividual rdf:about="http://www.spart.group/exam#Johnson">
            <rdf:type rdf:resource="http://www.spart.group/exam#Person"/>
        </owl:NamedIndividual>
    
        <owl:NamedIndividual rdf:about="http://www.spart.group/exam#Lily">
            <rdf:type rdf:resource="http://www.spart.group/exam#Person"/>
            <brotherIs rdf:resource="http://www.spart.group/exam#Tom"/>
            <fatherIs rdf:resource="http://www.spart.group/exam#Johnson"/>
            <ageIs rdf:datatype="http://www.w3.org/2001/XMLSchema#integer">10</age>
        </owl:NamedIndividual>
        
        <owl:NamedIndividual rdf:about="http://www.spart.group/exam#Tom">
            <rdf:type rdf:resource="http://www.spart.group/exam#Person"/>
        </owl:NamedIndividual>
        
    </rdf:RDF>

To model the the concepts (e.g. ontology classes) and relations (e.g. properties) as shown in "exam.owl", the [Protege](https://protege.stanford.edu) tool is recommended instead of creating "exam.owl" by editing the text. Based on the OWL model, [CodeFactExtractor](https://github.com/Megre/CodeFactExtractor) assists the extraction of facts from source code. 

### Inference

Base on the facts in `Example 1`, the following facts can be inferred:

> (exam:Tom *exam:fatherIs* exam:Johnson), (exam:Tom *exam:sisterIs* exam:Lily)

The inference can be represented as:

> (exam:Tom *exam:fatherIs* exam:Johnson), (exam:Tom *exam:sisterIs* exam:Lily) ← (exam:Lily *exam:fatherIs* exam:Johnson), (exam:Lily *exam:brotherIs* exam:Tom)

Given another person Lucy, it's not known whether 

> (exam:Lucy *exam:teacherIs* exam:Lily)

can be inferred or not based on the facts in `Example 1`. Such inference strategy is called `Open World Assumption (OWA)`. In the other strategy, `Closed World Assumption (CWA)`, an assertion's negative is inferred if it's not known whether the assertion is true or not. Thus, "Lucy is not Lily's teacher" is inferred. OWA is usually more reasonable because other facts may exist somewhere else. 

But the strategy of [CodeFactExtractor](https://github.com/Megre/CodeFactExtractor) works in a user-defined way. For example, if the fact

> (any-public-method *exam:hasModifier* "public")

is asserted for each public method of a class/method during the extraction, it can be safely asserted that the methods that are not linked to "public" by *`exam:hasModifier`* are non-public methods.


### SPARQL Query/Update Language

(to be continued)



Visitor Layout Configuration
----------

(to be continued)

----------

CodeFactExtractor是什么
----------
未完待续...