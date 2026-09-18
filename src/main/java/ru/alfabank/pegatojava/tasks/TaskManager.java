package main.java.ru.alfabank.pegatojava.tasks;

import main.java.ru.alfabank.pegatojava.annotations.AnnotationsHandler;
import main.java.ru.alfabank.pegatojava.exceptions.CancelException;
import main.java.ru.alfabank.pegatojava.exceptions.ColumnNotFoundException;
import main.java.ru.alfabank.pegatojava.exceptions.ReadOnlyFieldException;
import main.java.ru.alfabank.pegatojava.table.Table;

import java.lang.reflect.Field;
import java.util.*;

import static main.java.ru.alfabank.pegatojava.MainCommands.*;
import static main.java.ru.alfabank.pegatojava.tasks.TaskColumns.*;

public class TaskManager {
    private static final List<String> TASK_COLUMNS = Arrays.stream(TaskColumns.values()).map(TaskColumns::getColumnName).toList();
    private final Table<Task> tasks = new Table<>(TASK_COLUMNS);
    private static Scanner sc = new Scanner(System.in);
    private final AnnotationsHandler<Task> annotationsHandler = new AnnotationsHandler<>();

    public TaskManager(Scanner scanner) {
        sc = scanner;
    }

    // Команда 1: вывести все задачи
    // Описание и статус выводятся через команду 2
    public void listTasks() {
        System.out.println("Всего задач: " + tasks.size());
        try {
            for (Task current : tasks.rows()) {
                System.out.println(current.get(ID.toString()) + ": " + current.get(TITLE.toString()));
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // Команда 2: найти задачу по индексу или названию
    // Может найти задачу два раза, если индекс совпадает с названием
    public void findTask() {
        try {
            System.out.println("Введите индекс или часть названия задачи, или \"" + CANCEL.getCommand() + "\" для отмены:");
            String input = sc.nextLine();
            if (input.equals(CANCEL.getCommand())) {
                return;
            }
            StringBuilder output = new StringBuilder();
            String template = "Найдена задача \"%s\" с индексом %s\n\tОписание: %s\n\tСтатус: %s\n";

            for (Task task : tasks.findAllByField(input, ID.getColumnName(), TITLE.getColumnName())) {
                output.append(String.format(template, task.get(TITLE.toString()), task.get(ID.toString()), task.get(DESCRIPTION.toString()), task.get(STATUS.toString())));
            }

            if (!output.isEmpty()) {
                System.out.println(output.toString().trim());
            } else {
                System.out.println("Не найдено задач с названием или индексом \"" + input + "\"");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // Команда 3: добавить задачу
    public void addTask() {
        try {
            Task task = new Task(tasks.size() + 1);

            System.out.println("Доработка новой заявки:");
            task = editTask(task);
            tasks.add(task);
            System.out.println("Задача \"" + task.get(TITLE.toString()) + "\" добавлена");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            System.out.println("Добавление задачи отменено");
        }
    }

    // Команда 4: редактировать задачу
    public void editTask() {
        try {
            System.out.println("Введите индекс задачи или \"" + CANCEL.getCommand() + "\" для отмены:");
            String input = sc.nextLine();
            Task original = getTaskByIndex(input);
            if (original == null) {
                return;
            }
            Task newTask = editTask(original);
            tasks.remove(original);
            tasks.add(newTask);
            System.out.println("Задача " + newTask.id() + ": \"" + newTask.title() + "\" обновлена");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            System.out.println("Редактирование задачи отменено");
        }
    }

    public Task editTask(Task task) throws ColumnNotFoundException, IllegalAccessException, CancelException {
        boolean exit = false;
        Map<String, String> fieldsMap = new TreeMap<>();
        Field[] taskFields = Task.class.getDeclaredFields();
        for (Field field : taskFields) {
            field.setAccessible(true);
            String value = "";
            if (field.get(task) != null) {
                value = field.get(task).toString();
            }
            fieldsMap.put(field.getName(), value);
        }
        System.out.println("Введите название поля для редактирования, \"" + EXIT.getCommand() + "\", чтобы завершить редактирование, или \"" + CANCEL.getCommand() + "\" для отмены. Доступные поля:");
        tasks.listFields();
        while (!exit) {
            try {
                String key = sc.nextLine();
                if (key.equals(CANCEL.getCommand())) {
                    throw new CancelException("операция отменена пользователем");
                }
                if (key.equals(EXIT.getCommand())) {
                    exit = true;
                } else {
                    if (key.equals(ID.toString())) {
                        throw new ReadOnlyFieldException("идентификатор задачи нельзя изменить");
                    }
                    if (task.hasField(key)) {
                        System.out.println("Введите значение поля \"" + key + "\" (текущее \"" + task.get(key) + "\"):");
                        String value = sc.nextLine();
                        fieldsMap.put(key, value);
                        System.out.println("Поле \"" + key + "\" изменено");
                    } else {
                        throw new ColumnNotFoundException("поле с названием \"" + key + "\" не найдено");
                    }
                }
            } catch (CancelException e) {
                throw e;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
        int id = (int) task.get("id");
        String title = fieldsMap.get(TITLE.toString());
        String description = fieldsMap.get(DESCRIPTION.toString());
        String status = fieldsMap.get(STATUS.toString());
        task = new Task(id, title, description, status);

        try {
            annotationsHandler.checkAnnotations(task);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте снова");
            task = editTask(task);
        }
        return task;
    }

    // Команда 5: удалить задачу
    public void deleteTask() {
        System.out.println("Введите индекс задачи или \"" + CANCEL.getCommand() + "\" для отмены:");
        String input = sc.nextLine();
        Task task = getTaskByIndex(input);
        if (task == null) {
            System.out.println("Не найдена задача с индексом \"" + input + "\". Введите \"" + LIST.getCommand() + "\" для просмотра всех задач");
            return;
        }
        tasks.remove(task);
        System.out.println("Задача " + task.id() + ": \"" + task.title() + "\" удалена");
    }

    ///////// Утилиты /////////

    // Выпытать рабочий индекс задачи от пользователя
    private Task getTaskByIndex(String input) {
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
        try {
            return tasks.findFirstByField("id", index);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage() + ". Введите \"" + LIST.getCommand() + "\" для просмотра всех задач");
            return null;
        }
    }
}
