package main.java.ru.alfabank.pegatojava.tasks;

import main.java.ru.alfabank.pegatojava.annotations.NotEmpty;
import main.java.ru.alfabank.pegatojava.annotations.NotNull;
import main.java.ru.alfabank.pegatojava.annotations.Size;
import main.java.ru.alfabank.pegatojava.exceptions.ColumnNotFoundException;

import java.lang.reflect.Field;

public record Task(@NotNull
                   @NotEmpty
                   int id, @NotNull
                   @NotEmpty
                   String title, @NotNull
                   @NotEmpty
                   String description,
                   @NotNull
                   @NotEmpty
                   @Size(value = 8)
                   String status) {

    public Task(int id) {
        this(id, null, null, null);
    }

    public Object get(String inputField) throws ColumnNotFoundException, IllegalAccessException {
        Field field = getField(inputField);
        field.setAccessible(true);
        return field.get(this);
    }

    public boolean hasField(String field) {
        try {
            getField(field);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Field getField(String name) throws ColumnNotFoundException {
        Field[] allFields = Task.class.getDeclaredFields();
        for (Field field : allFields) {
            field.setAccessible(true);
            if (field.getName().equals(name)) {
                return field;
            }
        }
        throw new ColumnNotFoundException("поле с названием \"" + name + "\" не найдено");
    }
}
