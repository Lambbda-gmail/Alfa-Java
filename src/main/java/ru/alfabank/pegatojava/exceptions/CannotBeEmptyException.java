package main.java.ru.alfabank.pegatojava.exceptions;

public class CannotBeEmptyException extends RuntimeException {
    public CannotBeEmptyException(String s) {
        super(s);
    }
}
