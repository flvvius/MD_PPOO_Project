package exceptions;

public class InvalidAccountDataException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidAccountDataException(String message) {
        super(message);
    }
}
