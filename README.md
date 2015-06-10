# SHACL Engine

## Description
* This is a Java project for an engine supporting the Shapes Constraint Language (SHACL). 
* It is based on this proposal: https://w3c.github.io/data-shapes/shacl/. 
* This project contains a running SHACL engine of the SHACL Core profile and some additional advanced features.

## Current status
{{feature-table.txt}}

## Basic validation process
1. The input must be a valid RDF graph
2. A jena RDF model is created from the input
3. A validator (in this case we use SPARQL to validate the input) takes the model and looks for violations. To do so, the input must contain some data instances to evaluate from (i.e. they must be linked to a Shape through a shape selector)
4. If there is data to be evaluated, the validator checks for constraints violations. A model containing the errors is returned. If the input is valid, the error model is empty, otherwise it contains a RDF graph.