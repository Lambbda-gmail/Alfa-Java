package test.java.ru.alfabank.pegatojava.tasks;

import main.java.ru.alfabank.pegatojava.tasks.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static main.java.ru.alfabank.pegatojava.MainCommands.CANCEL;
import static main.java.ru.alfabank.pegatojava.MainCommands.EXIT;
import static main.java.ru.alfabank.pegatojava.tasks.TaskColumns.*;
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

    // Команда 1: вывести все задачи & команда 3: добавить задачу
    @Test
    void addAndListTest() {
        setInput(
                TITLE.getColumnName(),
                "Первая",
                DESCRIPTION.getColumnName(),
                "Описание 1",
                STATUS.getColumnName(),
                "Статус 1",
                EXIT.getCommand(),

                TITLE.getColumnName(),
                "Вторая",
                DESCRIPTION.getColumnName(),
                "Описание 2",
                STATUS.getColumnName(),
                "Статус 2",
                EXIT.getCommand());

        taskManager.listTasks();
        taskManager.addTask();
        taskManager.addTask();
        taskManager.listTasks();

        assertEquals("Всего задач: 0", getOutput(0));
        assertEquals("Всего задач: 2", getOutput(-3));
        assertEquals("1: Первая", getOutput(-2));
        assertEquals("2: Вторая", getOutput(-1));
    }

    // Команда 2: найти задачу по индексу или названию
    @Test
    void findTest() {
        setInput(
                TITLE.getColumnName(),
                "Название первое",
                DESCRIPTION.getColumnName(),
                "Описание первое",
                STATUS.getColumnName(),
                "Статус п",
                EXIT.getCommand(),

                TITLE.getColumnName(),
                "1",
                DESCRIPTION.getColumnName(),
                "Описание второе",
                STATUS.getColumnName(),
                "Статус в",
                EXIT.getCommand(),

                TITLE.getColumnName(),
                "Третья",
                DESCRIPTION.getColumnName(),
                "1",
                STATUS.getColumnName(),
                "-1",
                EXIT.getCommand(),

                "-1",

                "1");

        taskManager.addTask();
        taskManager.addTask();
        taskManager.addTask();
        taskManager.findTask(); // Ищем задачу с индексом "-1", не находим
        taskManager.findTask(); // Ищем задачи с "1", находим по первую индексу и вторую по названию

        assertEquals("Не найдено задач с названием или индексом \"-1\"", getOutput(-3));
        assertEquals("""
                Найдена задача "Название первое" с индексом 1
                	Описание: Описание первое
                	Статус: Статус п
                Найдена задача "1" с индексом 2
                	Описание: Описание второе
                	Статус: Статус в""", getOutput(-1));
    }

    // Команда 4: редактировать задачу
    @Test
    void editTest() {
        setInput(
                TITLE.getColumnName(),
                "Название оригинальное",
                DESCRIPTION.getColumnName(),
                "Описание оригинальное",
                STATUS.getColumnName(),
                "Статус о",
                EXIT.getCommand(),

                "1",
                TITLE.getColumnName(),
                "Новое название",
                DESCRIPTION.getColumnName(),
                "Новое описание",
                "Неверное название колонки",
                ID.getColumnName(),
                EXIT.getCommand(),

                "1",
                STATUS.getColumnName(),
                "Новый с",
                CANCEL.getCommand(),

                "1"
        );

        taskManager.addTask();
        taskManager.editTask(); // Меняем описание и статус у задачи с индексом 1. Также пытаемся менять недоступные поля
        taskManager.editTask(); // Начинаем менять статус и отменяем
        taskManager.findTask(); // Выводим изменённые значения

        assertEquals("Ошибка: поле с названием \"Неверное название колонки\" не найдено", getOutput(23));
        assertEquals("Ошибка: идентификатор задачи нельзя изменить", getOutput(24));
        assertEquals("Задача 1: \"Новое название\" обновлена", getOutput(25));
        assertEquals("Редактирование задачи отменено", getOutput(-3));
        assertEquals("""
                Найдена задача "Новое название" с индексом 1
                \tОписание: Новое описание
                \tСтатус: Статус о""", getOutput(-1));
    }

    // Команда 5: удалить задачу
    @Test
    void removeTest() {
        setInput(
                TITLE.getColumnName(),
                "Первая",
                DESCRIPTION.getColumnName(),
                "Описание 1",
                STATUS.getColumnName(),
                "Статус 1",
                EXIT.getCommand(),

                TITLE.getColumnName(),
                "Вторая",
                DESCRIPTION.getColumnName(),
                "Описание 2",
                STATUS.getColumnName(),
                "Статус 2",
                EXIT.getCommand(),

                "1",

                "-1");

        taskManager.addTask();
        taskManager.addTask();
        taskManager.deleteTask();   // Удаляем задачу с индексом 1
        taskManager.listTasks();    // Выводим оставшиеся
        taskManager.deleteTask();   // Пробуем удалить задачу с невалидным индексом

        assertEquals("Всего задач: 1", getOutput(-4));
        assertEquals("2: Вторая", getOutput(-3));
        assertEquals("Не найдена задача с индексом \"-1\". Введите \"1\" для просмотра всех задач", getOutput(-1));
    }

    ///////// Утилиты /////////

    // Построчный ввод данных для теста
    private void setInput(String... input) {
        String joined = "";
        for (String line : input) {
            joined += line + System.lineSeparator();
        }
        ByteArrayInputStream inStream = new ByteArrayInputStream((joined).getBytes());
        taskManager = new TaskManager(new Scanner(inStream));
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
