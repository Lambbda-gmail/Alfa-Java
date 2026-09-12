package main.java.ru.alfabank.pegatojava.app;

import main.java.ru.alfabank.pegatojava.model.EditCommands;
import main.java.ru.alfabank.pegatojava.model.Task;

import java.util.List;
import java.util.Scanner;

import static main.java.ru.alfabank.pegatojava.model.MainCommands.CANCEL;
import static main.java.ru.alfabank.pegatojava.model.MainCommands.LIST;

public class TaskManager {
    private static Scanner sc;
    private static List<Task> tasks;

    public TaskManager(Scanner scanner, List<Task> taskList) {
        sc = scanner;
        tasks = taskList;
    }

    // Команда 1: вывести все задачи
    // Описание и статус выводятся через команду 2
    public void listTasks() {
        System.out.println("Всего задач: " + tasks.size());
        for (Task current : tasks) {
            System.out.println(current.id() + ": " + current.title());
        }
    }

    // Команда 2: найти задачу по индексу или названию
    // Может найти задачу два раза, если индекс совпадает с названием
    public void findTask() {
        System.out.println("Введите индекс или часть названия задачи, или \"" + CANCEL.getCommand() + "\" для отмены:");
        String input = sc.nextLine();
        if (input.equals(CANCEL.getCommand())) {
            return;
        }
        StringBuilder output = new StringBuilder();
        String template = "Найдена задача \"%s\" с индексом %s\n\tОписание: %s\n\tСтатус: %s\n";

        for (Task task : tasks) {
            if (task.title().contains(input) || String.valueOf(task.id()).contains(input)) {
                output.append(String.format(template, task.title(), task.id(), task.description(), task.status()));
            }
        }

        if (!output.isEmpty()) {
            System.out.println(output.toString().trim());
        } else {
            System.out.println("Не найдено задач с названием или индексом \"" + input + "\"");
        }
    }

    // Команда 3: добавить задачу
    public void addTask() {
        System.out.println("Введите название новой задачи или \"" + CANCEL.getCommand() + "\" для отмены:");
        String title = sc.nextLine();
        if (title.equals(CANCEL.getCommand())) {
            return;
        }
        Task task = new Task(tasks.size(), title, "", "OPEN");
        System.out.println("Доработка новой заявки:");

        task = editTask(task);

        tasks.add(task);
        System.out.println("Задача \"" + task.title() + "\" добавлена под индексом " + task.id());
    }

    // Команда 4: редактировать задачу
    public void editTask() {
        System.out.println("Введите индекс задачи или \"" + CANCEL.getCommand() + "\" для отмены:");
        String input = sc.nextLine();
        Task task = getTaskByIndex(input);
        if (task == null) {
            return;
        }
        tasks.remove(task);

        task = editTask(task);

        tasks.add(task);
        System.out.println("Задача " + task.id() + ": \"" + task.title() + "\" обновлена");
    }

    public Task editTask(Task task) {
        int id = task.id();
        String title = task.title();
        String description = task.description();
        String status = task.status();
        String next;
        boolean exit = false;
        while (!exit) {
            System.out.println("Выберите поле для изменения или введите \"" + EditCommands.CONTINUE.getCommand() + "\", чтобы продолжить:");
            EditCommands.list();
            var input = EditCommands.find(sc.nextLine());
            switch (input) {
                case TITLE:
                    System.out.println("Текущее название: \"" + task.title() + "\". Введите новое название или \"" + CANCEL.getCommand() + "\" для отмены:");
                    next = sc.nextLine();
                    if (!next.equals(CANCEL.getCommand())) {
                        title = next;
                    }
                    break;
                case DESCRIPTION:
                    System.out.println("Текущее описание: \"" + task.description() + "\". Введите новое описание или \"" + CANCEL.getCommand() + "\" для отмены:");
                    next = sc.nextLine();
                    if (!next.equals(CANCEL.getCommand())) {
                        description = next;
                    }
                    break;
                case STATUS:
                    System.out.println("Текущий статус: \"" + task.status() + "\". Введите новый статус или \"" + CANCEL.getCommand() + "\" для отмены:");
                    next = sc.nextLine();
                    if (!next.equals(CANCEL.getCommand())) {
                        status = next;
                    }
                    break;
                case CONTINUE:
                    exit = true;
                    break;
                default:
                    System.out.println("Команда не опознана.");
            }
        }

        return new Task(id, title, description, status);
    }

    // Команда 5: удалить задачу
    public void deleteTask() {
        System.out.println("Введите индекс задачи или \"" + CANCEL.getCommand() + "\" для отмены:");
        String input = sc.nextLine();
        Task task = getTaskByIndex(input);
        if (task == null) {
            return;
        }
        tasks.remove(task);
        System.out.println("Задача " + task.id() + ": \"" + task.title() + "\" удалена");
    }

    ///////// Утилиты /////////

    // Выпытать рабочий индекс задачи от пользователя
    private static Task getTaskByIndex(String input) {
        int index;
        if (input.equals(CANCEL.getCommand())) {
            return null;
        }
        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Пожалуйста, введите индекс задачи (целое число):");
            return getTaskByIndex(sc.next());
        }
        for (Task task : tasks) {
            if (task.id() == index) {
                return task;
            }
        }
        System.out.println("Не найдена задача с индексом \"" + input + "\". Введите \"" + LIST.getCommand() + "\" для просмотра всех задач");
        return null;
    }
}
