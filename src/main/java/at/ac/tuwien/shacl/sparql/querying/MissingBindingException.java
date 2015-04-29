package at.ac.tuwien.shacl.sparql.querying;

public class MissingBindingException extends Exception {

	private static final long serialVersionUID = -9096561884850189421L;

	public MissingBindingException() {
		super();
	}
	
	public MissingBindingException(String message) {
		super(message);
	}
}
