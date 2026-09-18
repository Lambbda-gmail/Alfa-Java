package test.java.ru.alfabank.pegatojava.annotations;

import main.java.ru.alfabank.pegatojava.annotations.AnnotationsHandler;
import main.java.ru.alfabank.pegatojava.annotations.NotEmpty;
import main.java.ru.alfabank.pegatojava.annotations.NotNull;
import main.java.ru.alfabank.pegatojava.annotations.Size;
import main.java.ru.alfabank.pegatojava.exceptions.CannotBeEmptyException;
import main.java.ru.alfabank.pegatojava.exceptions.FieldSizeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnnotationsHandlerTest {
    private record Sample(@NotNull @NotEmpty String field1, @Size(4) String field2) {}
    private final AnnotationsHandler<Sample> annotationsHandler = new AnnotationsHandler<>();

    @Test
    void notNullTest() {
        assertThrows(NullPointerException.class, () -> {
            Sample sample = new Sample(null, null);
            annotationsHandler.checkAnnotations(sample);
        });
    }

    @Test
    void notEmptyTest() {
        assertThrows(CannotBeEmptyException.class, () -> {
            Sample sample = new Sample("", null);
            annotationsHandler.checkAnnotations(sample);
        });
    }

    @Test
    void sizeTest() {
        assertThrows(FieldSizeException.class, () -> {
            Sample sample = new Sample("something", "12345");
            annotationsHandler.checkAnnotations(sample);
        });
    }

    @Test
    void positiveTest() {
        assertDoesNotThrow(() -> {
            Sample sample = new Sample("something", "1234");
            annotationsHandler.checkAnnotations(sample);
        });
    }
}
