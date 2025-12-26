package pl.melkowskiphilip.GoodReadsPL.exception.custom;

// nazwa użytkownika już jest zajęta
public class UsernameAlreadyUsedException extends RuntimeException{
    public UsernameAlreadyUsedException(String message) {
        super(message);
    }
}
