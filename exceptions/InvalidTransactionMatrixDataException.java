package exceptions;

public class InvalidTransactionMatrixDataException extends Exception {
    private static final long serialVersionUID = 1L;

	public InvalidTransactionMatrixDataException(String message) {
        super(message);
    }
}
