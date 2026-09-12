package test.java.ru.alfabank.pegatojava.app;

import main.java.ru.alfabank.pegatojava.app.TaskManager;
import main.java.ru.alfabank.pegatojava.model.Task;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static main.java.ru.alfabank.pegatojava.model.EditCommands.*;
import static main.java.ru.alfabank.pegatojava.model.MainCommands.CANCEL;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskManagerTest {
    private TaskManager taskManager;
    private ByteArrayOutputStream outContent;
    private final PrintStream sysOutBackup = System.out;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(sysOutBackup);
    }

    // Команда 1: вывести все задачи
    @Test
    void listTest() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(0, "Первая", "", ""));
        tasks.add(new Task(3, "Вторая", "", ""));
        taskManager = new TaskManager(new Scanner(System.in), tasks);

        taskManager.listTasks();

        assertEquals("Всего задач: 2", getOutput(-3));
        assertEquals("0: Первая", getOutput(-2));
        assertEquals("3: Вторая", getOutput(-1));
    }

    // Команда 2: найти задачу по индексу или названию
    @Test
    void findTest() {
        setInput("2", "3", CANCEL.getCommand());
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(0, "33", "Описание 0", "CLOSED"));
        tasks.add(new Task(333, "0", "", "OPEN"));
        tasks.add(new Task(4, "4", "Описание 2", "222"));
        taskManager = new TaskManager(new Scanner(System.in), tasks);

        taskManager.findTask(); // Ищем задачу с индексом "2", не находим
        taskManager.findTask(); // Ищем задачи с "3", находим по индексу и по названию
        taskManager.findTask(); // Отменяем поиск

        assertEquals("Не найдено задач с названием или индексом \"2\"", getOutput(-4));
        assertEquals("""
                Найдена задача "33" с индексом 0
                	Описание: Описание 0
                	Статус: CLOSED
                Найдена задача "0" с индексом 333
                	Описание:\s
                	Статус: OPEN""", getOutput(-2));
        assertEquals("Введите индекс или часть названия задачи, или \"" + CANCEL.getCommand() + "\" для отмены:", getOutput(-1));
    }

    // Команда 3: добавить задачу
    @Test
    void addTest() {
        setInput(
                "Новая задача",
                TITLE.getCommand(),
                "Новая новая задача",
                TITLE.getCommand(),
                CANCEL.getCommand(),
                DESCRIPTION.getCommand(),
                "Новое описание " + CANCEL.getCommand(),
                "Неправильная команда",
                CONTINUE.getCommand(),
                CANCEL.getCommand()
        );
        taskManager = new TaskManager(new Scanner(System.in), new ArrayList<Task>());

        taskManager.addTask();  // Добавляем задачу, в процессе меняем название и вводим невалидную команду
        taskManager.addTask();  // Отменяем добавление задачи

        assertEquals("Задача \"Новая новая задача\" добавлена под индексом 0", getOutput(-2));
        assertEquals("Введите название новой задачи или \"" + CANCEL.getCommand() + "\" для отмены:", getOutput(-1));
    }

    // Команда 4: редактировать задачу
    @Test
    void editTest() {
        setInput(
                "0",
                TITLE.getCommand(),
                "Новое название",
                DESCRIPTION.getCommand(),
                "Новое описание",
                STATUS.getCommand(),
                "Новый статус",
                "Неправильная команда",
                CONTINUE.getCommand(),
                "0"
        );
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(0, "Индекс 0", "", "CLOSED"));
        tasks.add(new Task(333, "0", "Описание 333", "OPEN"));
        taskManager = new TaskManager(new Scanner(System.in), tasks);

        taskManager.editTask(); // Меняем описание и статус у задачи с индексом 0
        taskManager.findTask(); // Выводим изменённые значения

        assertEquals("""
                Найдена задача "0" с индексом 333
                	Описание: Описание 333
                	Статус: OPEN
                Найдена задача "Новое название" с индексом 0
                	Описание: Новое описание
                	Статус: Новый статус""", getOutput(-1));
    }

    // Команда 5: удалить задачу
    @Test
    void removeTest() {
        setInput("0", "Неправильный индекс", "-1");
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(0, "Индекс 0", "", "CLOSED"));
        tasks.add(new Task(333, "Название 0", "Описание 333", "OPEN"));
        taskManager = new TaskManager(new Scanner(System.in), tasks);

        taskManager.deleteTask();   // Удаляем задачу с индексом 0
        taskManager.listTasks();    // Выводим оставшиеся
        taskManager.deleteTask();   // Пробуем удалить задачу с невалидным индексом

        assertEquals("Всего задач: 1", getOutput(-5));
        assertEquals("333: Название 0", getOutput(-4));
        assertEquals("Пожалуйста, введите индекс задачи (целое число):", getOutput(-2));
        assertEquals("Не найдена задача с индексом \"-1\". Введите \"1\" для просмотра всех задач", getOutput(-1));
    }

    ///////// Утилиты /////////

    // Построчный ввод данных для теста
    private void setInput(String... input) {
        String joined = "";
        for (String line : input) {
            joined += line + System.lineSeparator();
        }
        ByteArrayInputStream in = new ByteArrayInputStream((joined).getBytes());
        System.setIn(in);
    }

    // Получение указанной строки вывода; если с минусом, ищем с конца
    private String getOutput(int line) {
        String[] split = outContent.toString().split(System.lineSeparator());
        if (line < 0) {
            return split[split.length + line].trim();
        } else {
            return split[line].trim();
        }
    }
}
