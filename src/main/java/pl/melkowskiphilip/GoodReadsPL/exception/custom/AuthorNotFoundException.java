package pl.melkowskiphilip.GoodReadsPL.exception.custom;

public class AuthorNotFoundException extends RuntimeException{
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
