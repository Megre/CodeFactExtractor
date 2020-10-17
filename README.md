[（中文说明）](#CodeFactExtractor是什么)

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

Lily, Johnson, and Tom are all individuals whose type (*rdf:type*) is Person. 

The properties *`exam:fatherIs`*, *`rdf:type`*, *`exam:ageIs`*, and *`exam:hasBrother`* links individuals to other individuals or literal. While *`exam:fatherIs`*, *`exam:hasBrother`*, and *`rdf:type`* are all object properties, *`exam:ageIs`* is a data  type property.

The prefix "rdf" is a XML namespace defined as "<a href="http://www.w3.org/1999/02/22-rdf-syntax-ns#">http://www.w3.org/1999/02/22-rdf-syntax-ns#</a>", where the property *`rdf:type`* is defined.

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

To model the the concepts (e.g. ontology classes) and relations (e.g. properties) as shown in "exam.owl", the [Protege](https://protege.stanford.edu) tool is recommended instead of creating "exam.owl" by editing the text. Based on the OWL model, [CodeFactExtractor](https://github.com/Megre/CodeFactExtractor) assists the extraction of facts from source code. 

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

is asserted for each public method of each class/method during the extraction, it can be safely asserted that the methods that are not linked to "public" by *`exam:hasModifier`* are non-public methods. (In Java, the access permission of every method of an interface is public even if the methods are not declared as public. Such methods are also considered as non-public methods since *`exam:hasModifier`* indicates the modifier declared in the source code.)

Based on the facts in [Example 1](#example-1), other facts also can be inferred such as "Johnson has two children at least", "Johnson has a sun Tom", and "Tom is younger than Johnson", etc. These facts are inferred according to the common sense. In [CodeFactExtractor](https://github.com/Megre/CodeFactExtractor), the inference is usually according to the programming mechanisms of a programming language. For example, if two methods of a class has the same name but different signatures, a overload relation of the two methods can be inferred. A property (e.g. *`exam:overloads`*) can be created to represent the relation.

An important inference strategy is reusing exiting relations to build more abstract relations. Given the following facts in addition to [Example 1](#example-1):

    (exam:Johnson exam:fatherIs exam:George)
    (exam:Tom exam:ageIs 12)
    
the following facts can be inferred:
    
    (exam:Lily exam:anElerIs exam:Tom)
    (exam:Tom exam:anElderIs exam:Johnson)
    (exam:Tom exam:anElderIs exam:George)
    ...
    
The property *`exam:anElderIs`* is more general than the properties *`exam:fatherIs`* since a father is always an elder of his son or daughter; on the contrary, an elder-relation not only indicates a son-father/daughter-father relation, but may also indicate a younger-sister-elder-brother relation or a grandson-grandfather relation. The following rule can be used to describe the elder-relation:

    (?x exam:anElderIs ?z) ← (?x exam:anElderIs ?y), (?y exam:anElderIs ?z)
    
which means *`exam:anElderIs`* is a transitive property.

Such inference strategy is called `generalization` which raises the abstraction level by imposing restrictions to existing properties. What to infer depends on your needs based on the clear definition of the meaning of each property.

### SPARQL Query/Update Language

(to be continued)



Visitor Layout Configuration
----------

(to be continued)

----------

CodeFactExtractor是什么
----------
未完待续...