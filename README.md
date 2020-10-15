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

### Fact

Triple

### SPARQL Query/Update Language




Visitor Layout Configuration
----------


----------

<label name="zh-cn">CodeFactExtractor是什么</label>
----------
