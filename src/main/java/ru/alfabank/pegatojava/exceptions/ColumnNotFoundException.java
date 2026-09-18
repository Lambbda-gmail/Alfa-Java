package main.java.ru.alfabank.pegatojava.exceptions;

public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException(String s) {
        super(s);
    }
}
