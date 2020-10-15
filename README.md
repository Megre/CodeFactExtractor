<a href="#zh-cn">（中文说明）</a>

What's CodeFactExtractor
----------
CodeFactExtractor is a lightweight framework to extract facts from source code for code search and inference. CodeFactExtractor assists both static and dynamic analysis in several ways:

- searching code elements based on self-defined rules or mechanisms of a programming language.
- inferring relationships of code elements, e.g. implementation, inheritance, override, and overload.
- identifying design patterns, e.g. the 23 classic GoF (Gang of Four) design patterns.
- detecting smells, anti-patterns, and defects, e.g. smells such as Parallel Inheritance Hierarchies, Message Chains, Middle Man, and Data Class, and anti-patterns such as Call Super, Circular Dependency, Constant Interface, Circle-Ellipse Problem, and Object Orgy.
- encapsulating temporal information to infer runtime behaviors, e.g. the invocation sequence of methods and the transmission of data (e.g. through assignment such as field modification).

Basic Concepts
----------
### OWL 
The W3C <a href="https://www.w3.org/2001/sw/wiki/OWL">Web Ontology Language (OWL)</a> is a Semantic Web language designed to represent rich and complex knowledge about things, groups of things, and relations between things.

In OWL, a `class` represents a group of elements. An instance of a class is called an `individual` belonging to the class. `Object property` represents the relationship between individuals. `Data type properties` associate individuals with data type values. The subclass relationship defines the hierarchy of classes, and the subproperty relationship defines the hierarchy of attributes.

A ".owl" file is a text file usually saved as RDF/XML syntax.

### Facts

Facts represent knowledge in form of `triples`. A triple is in form of:

(Subject *predicate* Object)

`Example 1`

> (exam:Lily *exam:father* exam:Johnson), (exam:Lily *rdf:type* exam:Person), (exam:Lily *exam:age* 10), (exam:Lily *exam:brother* exam:Tom)

Lily, Johnson, and Tom are all individuals whose type (*rdf:type*) is Person. 

The properties *`exam:father`*, *`rdf:type`*, *`exam:age`*, and *`exam:brother`* links individuals to other individuals or literal. While *`exam:father`*, *`exam:brother`*, and *`rdf:type`* are all object properties, *`exam:age`* is a data  type property.

The prefix "rdf" is a XML namespace. It is defined as <a href="http://www.w3.org/1999/02/22-rdf-syntax-ns#">http://www.w3.org/1999/02/22-rdf-syntax-ns#</a>, where the property *`rdf:type`* is defined.

We also define another XML namespace "exam" as "http://www.spart.group/exam#", where the ontology class *`exam:Person`*, and the properties *`exam:father`*, *`exam:age`*, and *`exam:brother`* are defined.

We can save the facts in `Example 1` in the following "<a href="readme/exam.owl">exam.owl</a>" file in RDF/XML format:

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
    
        <owl:ObjectProperty rdf:about="http://www.spart.group/exam#brother"/>
        <owl:ObjectProperty rdf:about="http://www.spart.group/exam#father"/>
        <owl:ObjectProperty rdf:about="http://www.spart.group/exam#sister"/>
        <owl:DatatypeProperty rdf:about="http://www.spart.group/exam#age"/>
        
        <owl:Class rdf:about="http://www.spart.group/exam#Person"/>
    
        <owl:NamedIndividual rdf:about="http://www.spart.group/exam#Johnson">
            <rdf:type rdf:resource="http://www.spart.group/exam#Person"/>
        </owl:NamedIndividual>
    
        <owl:NamedIndividual rdf:about="http://www.spart.group/exam#Lily">
            <rdf:type rdf:resource="http://www.spart.group/exam#Person"/>
            <brother rdf:resource="http://www.spart.group/exam#Tom"/>
            <father rdf:resource="http://www.spart.group/exam#Johnson"/>
            <age rdf:datatype="http://www.w3.org/2001/XMLSchema#integer">10</age>
        </owl:NamedIndividual>
        
        <owl:NamedIndividual rdf:about="http://www.spart.group/exam#Tom">
            <rdf:type rdf:resource="http://www.spart.group/exam#Person"/>
        </owl:NamedIndividual>
        
    </rdf:RDF>


### Inference

Base on the facts in `Example 1`, we can infer the following facts:

> (exam:Tom exam:father exam:Johnson), (exam:Tom exam:sister exam:Lily)

We represent the inference as:

> (exam:Tom exam:father exam:Johnson), (exam:Tom exam:sister exam:Lily) <- (exam:Lily *exam:father* exam:Johnson), (exam:Lily *exam:brother* exam:Tom)


### SPARQL Query/Update Language

...



Visitor Layout Configuration
----------

...

----------

<label id="zh-cn">CodeFactExtractor是什么</label>
----------
