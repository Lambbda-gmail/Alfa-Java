import java.util.ArrayList;
import java.util.Scanner;

// Локализация консоли: https://superuser.com/questions/269818/change-default-code-page-of-windows-console-to-utf-8 - мне помогло поменять настройки винды

public class Main {
    private static final ArrayList<String> tasks = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);
    private static final String cancelCommand = "x";

    // Выпытать рабочий индекс задачи от пользователя
    private static int getIndex(String input) {
        int index = -1;
        if (input.equals(cancelCommand))
            return index;
        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Пожалуйста, введите индекс задачи (целое число):");
            index = getIndex(sc.next());
            return index;
        }
        if (index > tasks.size()-1) {
            System.out.println("Не найдена задача с индексом \"" + input +"\". Введите \"1\" для просмотра всех задач");
            return -1;
        }
        else
            return index;
    }

    // Команда 1: вывести все задачи
    private static void listTasks() {
        System.out.println("Всего задач: " + tasks.size());
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(i + ": " + tasks.get(i));
        }
    }

    // Команда 2: найти задачу по индексу или названию
    private static void findTask(String input) {
        if (input.equals(cancelCommand))
            return;
        StringBuilder output = new StringBuilder();
        String template = "Найдена задача \"%s\" под индексом %s";

        // Опциональный поиск по индексу
        try {
            output.append(String.format(template, tasks.get(Integer.parseInt(input)), input));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            // Игнорим и продолжаем
        }

        // Поиск точных совпадений
        if (tasks.contains(input))
            output.append(String.format(template, input, tasks.indexOf(input)));

        // Поиск по похожим
        for (String task:tasks) {
            if (task.contains(input))
                output.append(String.format(template, task, tasks.indexOf(task)));
        }

        if (!output.isEmpty())
            System.out.println(output);
        else
            System.out.println("Не найдено задач с названием или индексом \""+input+"\"");
    }

    // Команда 3: добавить задачу
    private static void addTask(String input) {
        if (input.equals(cancelCommand))
            return;
        tasks.add(input);
        System.out.println("Задача \""+input+"\" добавлена под индексом "+(tasks.size()-1));
    }

    // Команда 4: редактировать задачу
    private static void editTask(String input) {
        int index = getIndex(input);
        if (index < 0)
            return;
        System.out.println("Введите новое название для задачи \"" + tasks.get(index) + "\":");
        input = sc.next();
        tasks.set(index, input);
        System.out.println("Задача "+index+": \""+input+"\" обновлена");
    }

    // Команда 5: удалить задачу
    private static void deleteTask(String input) {
        int index = getIndex(input);
        if (index < 0)
            return;
        System.out.println("Задача "+index+": \"" + tasks.get(index) + "\" удалена");
        tasks.remove(index);
    }

    public static void main(String[] args) {
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
                    System.out.println("Введите индекс или часть названия задачи, или \""+cancelCommand+"\" для отмены:");
                    findTask(sc.next());
                    break;
                case "3":
                    System.out.println("Введите название новой задачи или \""+cancelCommand+"\" для отмены:");
                    addTask(sc.next());
                    break;
                case "4":
                    System.out.println("Введите индекс задачи или \""+cancelCommand+"\" для отмены:");
                    editTask(sc.next());
                    break;
                case "5":
                    System.out.println("Введите индекс задачи или \""+cancelCommand+"\" для отмены:");
                    deleteTask(sc.next());
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
}
