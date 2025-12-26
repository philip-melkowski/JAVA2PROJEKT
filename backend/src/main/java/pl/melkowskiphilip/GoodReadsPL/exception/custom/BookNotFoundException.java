package pl.melkowskiphilip.GoodReadsPL.exception.custom;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String message) {
        super(message);
    }
}
