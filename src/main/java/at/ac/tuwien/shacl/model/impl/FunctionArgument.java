package at.ac.tuwien.shacl.model.impl;

/** 12.1 Function Arguments
 * 
 * - Arguments are ordered, corresponding to the notation of function calls 
 *   in SPARQL (e.g. ex:exampleFunction(?arg1, arg2))
 *   -> implement in parent class (Function)
 * - The values of sh:predicate must be sh:arg1 for the first arguments, 
 *   sh:arg2 for the second and so on
     -> implement in parent class (Function)
 * - Arguments are “inherited” from the superclasses of the function 
 *   (e.g. is a superclass already declares sh:arg2, then subclasses may 
 *   only define sh:arg2)
 * - Each sh:Argument may have sh:optional; 
 *   if an arguments has been declared optional, all succeeding arguments 
 *   must also be declared optional
 * @author xlin
 *
 */
public class FunctionArgument extends ArgumentImpl {

}
