package exception;

public class UserRegisterationFailedException extends RuntimeException {
    public UserRegisterationFailedException(String message) {
        super(message);
    }
}
