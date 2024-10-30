package exceptions;

public class MenuItemNotFound extends RuntimeException {
    public MenuItemNotFound(String message) {
        super(message);
    }
}
