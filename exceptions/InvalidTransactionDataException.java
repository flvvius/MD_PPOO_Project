package exceptions;

public class InvalidTransactionDataException extends Exception {
    private static final long serialVersionUID = 1L;

	public InvalidTransactionDataException(String message) {
        super(message);
    }
}
