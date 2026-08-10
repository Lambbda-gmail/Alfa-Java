package main.java.ru.alfabank.pegatojava.model;

public enum EditCommands {
    TITLE("1", "Название"),
    DESCRIPTION("2", "Описание"),
    STATUS("3", "Статус"),
    CONTINUE("0", "Продолжить"),
    DEFAULT("any", "Команда не опознана");

    private final String command;
    private final String description;

    EditCommands(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return this.command;
    }

    public String getDescription() {
        return this.description;
    }

    public static EditCommands find(String input) {
        for (EditCommands command : EditCommands.values())
            if (command.getCommand().equals(input))
                return command;
        return DEFAULT;
    }

    public static void list() {
        for (EditCommands command : EditCommands.values()) {
            if (command != DEFAULT)
                System.out.println(command.getCommand() + " - " + command.getDescription());
        }
    }
}