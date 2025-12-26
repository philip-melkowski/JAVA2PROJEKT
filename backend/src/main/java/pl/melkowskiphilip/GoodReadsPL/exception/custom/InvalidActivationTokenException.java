package pl.melkowskiphilip.GoodReadsPL.exception.custom;

// niepoprawny token aktywacji konta
public class InvalidActivationTokenException extends RuntimeException{
    public InvalidActivationTokenException(String message) {
        super(message);
    }
}
