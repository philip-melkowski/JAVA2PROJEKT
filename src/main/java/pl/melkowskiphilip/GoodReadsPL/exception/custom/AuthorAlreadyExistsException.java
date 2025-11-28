package pl.melkowskiphilip.GoodReadsPL.exception.custom;

public class AuthorAlreadyExistsException extends RuntimeException{
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}
