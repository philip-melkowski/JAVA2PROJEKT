package pl.melkowskiphilip.GoodReadsPL.exception.custom;

// użytkownik nie istnieje
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
