# SHACL Engine

#### Basic validation process
0. The input must be a valid RDF graph
1. A jena RDF model is created from the input
2. A validator (in this case we use SPARQL to validate the input) takes the model and looks for violations. To do so, the input must contain some 

data instances to evaluate from (i.e. they must be linked to a Shape through a shape selector)
3. If there is data to be evaluated, the validator checks for constraints violations. A model containing the errors is returned. If the input is 

valid, the error model is empty, otherwise it contains a RDF graph.

#### Architecture
There are 6 main class types.
1. SHACLEntity classes: Contain necessary parameters of a SHACL vocable
2. ModelBuilder classes: Create SHACLEntity and ConstraintViolation classes from a RDF node type
3. SPARQLConstraintQuery and subclasses: Contain all needed information so that a SPARQL query can be executed
4. SHACL class: Contains the SHACL RDF information
5. SPARQLValidator: Validates a RDF model
6. ModelRegistry: Registers and links ModelBuilder classes and SPARQLConstraintQuery classes together. This is important as we need to know which ModelBuilder belongs to which Query to automate the process of constraint checking (otherwise every constraint would need to be checked separately).

Furthermore, there are several helper classes.

The validator is the main access point as it ties the different functions together.
1. On instantiating, the ModelRegistry registers all ModelBuilder subclasses and SPARQLConstraintQuery subclasses and ties them together.
2. The validator then checks for data instances to be evaluated
3. Then it checks for constraint violations:
	* It iterates over all registered ModelBuilder classes in the registry. Each ModelBuilder represents on constraint that needs to be  checked.
	* Each ModelBuilder instance extracts all necessary information and stores it into a SHACLEntity object
	* The SPARQLConstraintQuery class that is linked with the ModelBuilder class takes the generated SHACLEntity and with a SPARQL query checks whether the entity violates the model. If so, a non empty error model is returned.