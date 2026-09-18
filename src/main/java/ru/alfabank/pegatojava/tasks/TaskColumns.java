package main.java.ru.alfabank.pegatojava.tasks;

import main.java.ru.alfabank.pegatojava.exceptions.ColumnNotFoundException;

public enum TaskColumns {
    ID("id"),
    TITLE("title"),
    DESCRIPTION("description"),
    STATUS("status");

    private final String columnName;

    TaskColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public static TaskColumns find(String input) throws ColumnNotFoundException {
        for (TaskColumns command : TaskColumns.values())
            if (command.getColumnName().equals(input)) {
                return command;
            }
        throw new ColumnNotFoundException("Поле с названием \"" + input + "\" не найдено");
    }

    public static void list() {
        for (TaskColumns command : TaskColumns.values()) {
            System.out.println(command.getColumnName());
        }
    }

    @Override
    public String toString() {
        return this.columnName;
    }
}