package exceptions;

public class InvalidTransactionAmountsDataException extends Exception {
    private static final long serialVersionUID = 1L;

	public InvalidTransactionAmountsDataException(String message) {
        super(message);
    }
}
