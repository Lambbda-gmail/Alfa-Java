package main.java.ru.alfabank.pegatojava.table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Table<T>(List<String> columnNames, ArrayList<T> rows) {
    public Table(List<String> columnNames) {
        this(columnNames, new ArrayList<>());
    }

    public int size() {
        return rows.size();
    }

    public void add(T row) {
        rows.add(row);
    }

    public void remove(T row) {
        rows.remove(row);
    }

    public <E> T findFirstByField(String key, E value) throws IllegalAccessException {
        for (T row : rows) {
            Field[] allFields = row.getClass().getDeclaredFields();
            for (Field field : allFields) {
                field.setAccessible(true);
                if (field.getName().equals(key)) {
                    if (field.get(row).equals(value)) {
                        return row;
                    }
                }
            }
        }
        return null;
    }

    public List<T> findAllByField(String value, String ... fields) throws IllegalAccessException {
        List<T> results = new ArrayList<>();
        for (T row : rows) {
            Field[] allFields = row.getClass().getDeclaredFields();
            for (Field field : allFields) {
                field.setAccessible(true);
                if (Arrays.stream(fields).anyMatch(name -> name.equals(field.getName()))) {
                    if (field.get(row).toString().contains(value)) {
                        results.add(row);
                    }
                }
            }
        }
        return results;
    }

    public void listFields() {
        for (String name : columnNames) {
            System.out.println(name);
        }
    }
}
