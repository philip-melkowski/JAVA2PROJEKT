package pl.melkowskiphilip.GoodReadsPL.exception.custom;

// mail juz zajęty - nie można założyć kolejnego konta
public class EmailAlreadyUsedException extends RuntimeException{
    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
