package at.ac.tuwien.shacl.util;

/**
 * Class defining exceptions against the SHACL notation.
 * 
 * @author xlin
 *
 */
public class SHACLParsingException extends Exception {
	private static final long serialVersionUID = -8591532139672895352L;

	public SHACLParsingException() {
		super();
	}
	
	public SHACLParsingException(String e) {
		super(e);
	}
}
