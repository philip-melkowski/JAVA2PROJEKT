package pl.melkowskiphilip.GoodReadsPL.exception.custom;

// token aktywacji wygasł
public class ActivationTokenExpiredException extends RuntimeException{
    public ActivationTokenExpiredException(String message) {
        super(message);
    }
}
