package main.java.ru.alfabank.pegatojava;

import main.java.ru.alfabank.pegatojava.tasks.TaskManager;
import main.java.ru.alfabank.pegatojava.tasks.TaskColumns;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<String> TASK_COLUMNS = List.of(Arrays.toString(TaskColumns.values()));

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        TaskManager taskManager = new TaskManager(sc);

        System.out.println("Добро пожаловать! Выберите действие:");
        MainCommands.list();

        boolean exit = false;
        while (!exit) {
            var input = MainCommands.find(sc.nextLine());
            switch (input) {
                case LIST:
                    taskManager.listTasks();
                    break;
                case FIND:
                    taskManager.findTask();
                    break;
                case ADD:
                    taskManager.addTask();
                    break;
                case EDIT:
                    taskManager.editTask();
                    break;
                case DELETE:
                    taskManager.deleteTask();
                    break;
                case EXIT:
                    exit = true;
                    break;
                default:
                    System.out.println("Команда не опознана. Доступные команды:");
                    MainCommands.list();
            }
        }
    }
}
