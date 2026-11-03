package main.java.ru.alfabank.pegatojava.exceptions;

public class CancelException extends RuntimeException {
    public CancelException(String s) {
        super(s);
    }
}
