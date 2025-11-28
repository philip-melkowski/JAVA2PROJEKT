package pl.melkowskiphilip.GoodReadsPL.exception.custom;

public class InvalidReviewIdException extends RuntimeException{
    public InvalidReviewIdException(String message) {
        super(message);
    }
}
