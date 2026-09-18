package main.java.ru.alfabank.pegatojava.annotations;

import main.java.ru.alfabank.pegatojava.exceptions.CannotBeEmptyException;
import main.java.ru.alfabank.pegatojava.exceptions.FieldSizeException;

import java.lang.reflect.Field;

public class AnnotationsHandler<T> {
    public void checkAnnotations(T object) throws IllegalAccessException, CannotBeEmptyException, FieldSizeException {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(NotNull.class)) {
                if (field.get(object) == null) {
                    throw new NullPointerException("поле \"" + field.getName() + "\" не может быть пустым");
                }
            }

            if (field.isAnnotationPresent(NotEmpty.class)) {
                if (field.get(object).toString().trim().equals("")) {
                    throw new CannotBeEmptyException("поле \"" + field.getName() + "\" не может быть пустым");
                }
            }

            if (field.isAnnotationPresent(Size.class)) {
                int value = field.getAnnotation(Size.class).value();
                int currentLength = field.get(object).toString().trim().length();
                if (currentLength > value) {
                    throw new FieldSizeException("длина поля \"" + field.getName() + "\" не может превышать " + value + " символов (текущая: " + currentLength + ")");
                }
            }
        }
    }
}
