# SHACL Engine

## Description
* This is a Java project for an engine supporting the Shapes Constraint Language (SHACL). 
* It is based on this proposal: [https://w3c.github.io/data-shapes/shacl/]. 
* The current status is work in progess.

## Basic validation process
1. The input must be a valid RDF graph
2. A jena RDF model is created from the input
3. A validator (in this case we use SPARQL to validate the input) takes the model and looks for violations. To do so, the input must contain some data instances to evaluate from (i.e. they must be linked to a Shape through a shape selector)
4. If there is data to be evaluated, the validator checks for constraints violations. A model containing the errors is returned. If the input is valid, the error model is empty, otherwise it contains a RDF graph.

## Current status
#### Validation engine
* shape validation for ```rdfs:Class```/```rdf:type``` not implemented yet
* only property constraints are currently supported

#### Property Constraints 
Validation engine partially implemented. Known issue points:
* ```sh:datatype``` needs to have function ```sh:hasDatatype``` (SPARQL query) implemented and linked
* ```sh:allowedValues`` query is not working, might have something to do with the passed list of allowedValues
* ```sh:minCount```/```sh:maxCount``` needs to have function ```sh:valueDatatype``` (SPARQL query) implemented and linked
* ```sh:nodeKind``` needs to have function ```sh:hasNodeKind``` (SPARQL query) implemented and linked
* ```sh:valueShape``` needs to have function ```sh:valueShape``` (SPARQL query) implemented and linked