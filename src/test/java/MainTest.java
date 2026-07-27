package test.java;

import main.java.Main;
import org.junit.jupiter.api.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    private ByteArrayOutputStream outContent;
    private final InputStream sysInBackup = System.in;
    private final PrintStream sysOutBackup = System.out;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setIn(sysInBackup);
        System.setOut(sysOutBackup);
    }

    // Общая проверка команд
    @Test
    void mainAndCancelTest() {
        String CANCEL_COMMAND = "x";
        // При неправильной команде выводим ошибку, при отмене отменяем, при выходе выходим, иначе не выходим
        setInput("wrong", "", "2", CANCEL_COMMAND, "3", CANCEL_COMMAND, "4", CANCEL_COMMAND, "5", CANCEL_COMMAND);

        Main.main();

        assertEquals("Добро пожаловать! Выберите действие:", getOutput(0));
        assertEquals("Команда не опознана. Доступные команды:", getOutput(2));
        assertEquals("Введите индекс или часть названия задачи, или \"x\" для отмены:", getOutput(4));
        assertEquals("Введите название новой задачи или \"x\" для отмены:", getOutput(5));
        assertEquals("Введите индекс задачи или \"x\" для отмены:", getOutput(6));
        assertEquals("Введите индекс задачи или \"x\" для отмены:", getOutput(7));
    }

    // Команда 1: вывести все задачи & Команда 3: добавить задачу
    @Test
    void addAndListTest() {
        // Нет задач, выводим ничего
        setInput("1");

        Main.main();

        assertEquals("Всего задач: 0", getOutput(-1));

        // Добавляем задачи "alpha", "omega", выводим обе
        setInput("3", "alpha", "3", "omega", "1");

        Main.main();

        assertEquals("Всего задач: 2", getOutput(-3));
        assertEquals("0: alpha", getOutput(-2));
        assertEquals("1: omega", getOutput(-1));
    }

    // Команда 2: найти задачу по индексу или названию
    @Test
    void findTest() {
        // Поиск по тексту: добавляем задачи beta, theta, gamma, ищем "ta"
        setInput("3", "beta", "3", "theta", "3", "gamma", "2", "ta");

        Main.main();

        assertEquals("""
                Найдена задача "beta" под индексом 0
                Найдена задача "theta" под индексом 1""", getOutput(-1));

        // Поиск по тексту и индексу: добавляем задачи 1, 0, 2, ищем 0
        setInput("3", "1", "3", "0", "3", "2", "2", "0");

        Main.main();

        assertEquals("""
                Найдена задача "1" под индексом 0
                Найдена задача "0" под индексом 1""", getOutput(-1));
    }

    // Команда 4: редактировать задачу
    @Test
    void editTest() {
        // Добавляем задачу alpha, выводим, меняем 0 на beta, выводим
        setInput("3", "alpha", "1", "4", "0", "beta", "1");

        Main.main();

        assertEquals("0: alpha", getOutput(5));
        assertEquals("0: beta", getOutput(-1));
    }

    // Команда 5: удалить задачу
    @Test
    void removeTest() {
        // Добавляем задачи alpha, beta, выводим, удаляем 0 (alpha), выводим
        setInput("3", "alpha", "3", "beta", "1", "5", "0", "1");

        Main.main();

        assertEquals("Всего задач: 2", getOutput(6));
        assertEquals("Всего задач: 1", getOutput(-2));
    }

    // Ввод неправильных индексов
    @Test
    void invalidInputTest() {
        // Вводим невалидные индексы
        setInput("4", "alpha", "1", "5", "2");

        Main.main();

        assertEquals("Пожалуйста, введите индекс задачи (целое число):", getOutput(3));
        assertEquals("Не найдена задача с индексом \"1\". Введите \"1\" для просмотра всех задач", getOutput(4));
        assertEquals("Не найдена задача с индексом \"2\". Введите \"1\" для просмотра всех задач", getOutput(6));
    }

    ///////// Утилиты /////////

    // Построчный ввод данных для теста
    private void setInput(String ... input) {
        String joined = "";
        for (String line : input) {
            joined += line + System.lineSeparator();
        }
        // Авто выход
        joined += "0";
        ByteArrayInputStream in = new ByteArrayInputStream((joined).getBytes());
        System.setIn(in);
    }

    // Получение указанной строки вывода; если с минусом, ищем с конца
    private String getOutput(int line) {
        String[] split = outContent.toString().split(System.lineSeparator());
        if (line < 0)
            return split[split.length + line].trim();
        else
            return split[line].trim();
    }
}
