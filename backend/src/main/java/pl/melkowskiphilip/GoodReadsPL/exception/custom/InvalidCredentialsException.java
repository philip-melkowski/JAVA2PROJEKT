package pl.melkowskiphilip.GoodReadsPL.exception.custom;


// zle dane logowania
public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
