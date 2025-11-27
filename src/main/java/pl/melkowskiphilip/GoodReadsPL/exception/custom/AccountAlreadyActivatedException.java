package pl.melkowskiphilip.GoodReadsPL.exception.custom;

// konto już zostało aktywowane
public class AccountAlreadyActivatedException extends RuntimeException{
    public AccountAlreadyActivatedException(String message) {
        super(message);
    }
}
