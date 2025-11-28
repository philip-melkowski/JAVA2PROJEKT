package pl.melkowskiphilip.GoodReadsPL.exception.custom;

public class ReviewNotFoundException extends RuntimeException{
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
