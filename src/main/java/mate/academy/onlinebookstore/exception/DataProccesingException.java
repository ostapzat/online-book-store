package mate.academy.onlinebookstore.exception;

public class DataProccesingException extends RuntimeException {
    public DataProccesingException(String message, Exception e) {
        super(message);
    }

    public DataProccesingException(String message) {
        super(message);
    }
}
