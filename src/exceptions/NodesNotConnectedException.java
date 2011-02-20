package exceptions;

public class NodesNotConnectedException extends Exception {
	private static final long serialVersionUID = 1L;
	public NodesNotConnectedException(String message) {
		super(message);
	}
}
