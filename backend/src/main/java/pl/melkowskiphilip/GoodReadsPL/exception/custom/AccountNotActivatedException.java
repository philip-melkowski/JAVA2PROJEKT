package pl.melkowskiphilip.GoodReadsPL.exception.custom;

// konto nie jest aktywne
public class AccountNotActivatedException extends RuntimeException{
    public AccountNotActivatedException(String message) {
        super(message);
    }
}
