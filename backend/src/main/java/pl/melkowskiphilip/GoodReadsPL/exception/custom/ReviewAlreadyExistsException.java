package pl.melkowskiphilip.GoodReadsPL.exception.custom;

public class ReviewAlreadyExistsException extends RuntimeException{
    public ReviewAlreadyExistsException(String message) {
        super(message);
    }
}
