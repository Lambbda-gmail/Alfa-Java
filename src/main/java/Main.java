package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Локализация консоли: https://superuser.com/questions/269818/change-default-code-page-of-windows-console-to-utf-8 - мне помогло поменять настройки винды

public class Main {
    private static Scanner sc;
    private static List<String> tasks;
    private static final String CANCEL_COMMAND = "x";

    public static void main() {
        sc = new Scanner(System.in);
        tasks = new ArrayList<>();

        String commands = """
        1 - Просмотреть все задачи
        2 - Найти задачу
        3 - Добавить задачу
        4 - Изменить задачу
        5 - Удалить задачу
        0 - Выход""";
        System.out.println("Добро пожаловать! Выберите действие:");
        System.out.println(commands);

        boolean exit = false;
        while(!exit) {
            String input = sc.next();
            switch (input) {
                case "1":
                    listTasks();
                    break;
                case "2":
                    findTask();
                    break;
                case "3":
                    addTask();
                    break;
                case "4":
                    editTask();
                    break;
                case "5":
                    deleteTask();
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Команда не опознана. Доступные команды:");
                    System.out.println(commands);
            }
        }
    }

    ///////// Команды /////////

    // Команда 1: вывести все задачи
    private static void listTasks() {
        System.out.println("Всего задач: " + tasks.size());
        for (int i = 0; i < tasks.size(); i ++ ) {
            System.out.println(i + ": " + tasks.get(i));
        }
    }

    // Команда 2: найти задачу по индексу или названию
    // Может найти задачу два раза, если индекс совпадает с названием (e.g. первая задача называется "0") - чинить не буду, сценарий нереальный
    private static void findTask() {
        System.out.println("Введите индекс или часть названия задачи, или \"" + CANCEL_COMMAND + "\" для отмены:");
        String input = sc.next();
        if (input.equals(CANCEL_COMMAND))
            return;
        StringBuilder output = new StringBuilder();
        String template = "Найдена задача \"%s\" под индексом %s\n";

        // Опциональный поиск по индексу
        try {
            output.append(String.format(template, tasks.get(Integer.parseInt(input)), input));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            // Игнорим и продолжаем
        }

        // Поиск по похожим
        for (String task:tasks) {
            if (task.contains(input))
                output.append(String.format(template, task, tasks.indexOf(task)));
        }

        if (!output.isEmpty())
            System.out.println(output.toString().trim());
        else
            System.out.println("Не найдено задач с названием или индексом \"" + input + "\"");
    }

    // Команда 3: добавить задачу
    private static void addTask() {
        System.out.println("Введите название новой задачи или \"" + CANCEL_COMMAND + "\" для отмены:");
        String input = sc.next();
        if (input.equals(CANCEL_COMMAND))
            return;
        tasks.add(input);
        System.out.println("Задача \"" + input + "\" добавлена под индексом " + (tasks.size()-1));
    }

    // Команда 4: редактировать задачу
    private static void editTask() {
        System.out.println("Введите индекс задачи или \"" + CANCEL_COMMAND + "\" для отмены:");
        String input = sc.next();
        int index = getIndex(input);
        if (index < 0)
            return;
        System.out.println("Введите новое название для задачи \"" + tasks.get(index) + "\":");
        input = sc.next();
        tasks.set(index, input);
        System.out.println("Задача " + index + ": \"" + input + "\" обновлена");
    }

    // Команда 5: удалить задачу
    private static void deleteTask() {
        System.out.println("Введите индекс задачи или \"" + CANCEL_COMMAND + "\" для отмены:");
        String input = sc.next();
        int index = getIndex(input);
        if (index < 0)
            return;
        System.out.println("Задача " + index + ": \"" + tasks.get(index) + "\" удалена");
        tasks.remove(index);
    }

    ///////// Утилиты /////////

    // Выпытать рабочий индекс задачи от пользователя
    private static int getIndex(String input) {
        int index = -1;
        if (input.equals(CANCEL_COMMAND))
            return index;
        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Пожалуйста, введите индекс задачи (целое число):");
            index = getIndex(sc.next());
            return index;
        }
        if (index > tasks.size()-1) {
            System.out.println("Не найдена задача с индексом \"" + input  + "\". Введите \"1\" для просмотра всех задач");
            return -1;
        }
        else
            return index;
    }
}
