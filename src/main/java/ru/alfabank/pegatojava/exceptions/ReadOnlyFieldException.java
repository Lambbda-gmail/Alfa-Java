package main.java.ru.alfabank.pegatojava.exceptions;

public class ReadOnlyFieldException extends RuntimeException {
    public ReadOnlyFieldException(String s) {
        super(s);
    }
}
