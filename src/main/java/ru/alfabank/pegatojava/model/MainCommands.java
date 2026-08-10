package main.java.ru.alfabank.pegatojava.model;

public enum MainCommands {
    LIST("1", "Просмотреть все задачи"),
    FIND("2", "Найти задачу"),
    ADD("3", "Добавить задачу"),
    EDIT("4", "Изменить задачу"),
    DELETE("5", "Удалить задачу"),
    CANCEL("x", "Отмена"),
    EXIT("0", "Выход"),
    DEFAULT("Иная", "Команда не опознана");

    private final String command;
    private final String description;

    MainCommands(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return this.command;
    }

    public String getDescription() {
        return this.description;
    }

    public static MainCommands find(String input) {
        for (MainCommands command : MainCommands.values())
            if (command.getCommand().equals(input))
                return command;
        return DEFAULT;
    }

    public static void list() {
        for (MainCommands command : MainCommands.values()) {
            if (command != DEFAULT && command != CANCEL)
                System.out.println(command.getCommand() + " - " + command.getDescription());
        }
    }
}