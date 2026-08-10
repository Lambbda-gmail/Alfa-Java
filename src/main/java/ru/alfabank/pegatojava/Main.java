package main.java.ru.alfabank.pegatojava;

import main.java.ru.alfabank.pegatojava.app.TaskManager;
import main.java.ru.alfabank.pegatojava.model.MainCommands;
import main.java.ru.alfabank.pegatojava.model.Task;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        TaskManager taskManager = new TaskManager(sc, new ArrayList<Task>());

        System.out.println("Добро пожаловать! Выберите действие:");
        MainCommands.list();

        boolean exit = false;
        while(!exit) {
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
