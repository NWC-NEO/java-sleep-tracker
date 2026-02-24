package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SleepTrackerApp {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final List<SleepAnalyzer> analyzers; // список аналитических функций

    public SleepTrackerApp() {
        this.analyzers = List.of(
                new TotalSessionsAnalyzer(),
                new MinDurationAnalyzer(),
                new MaxDurationAnalyzer(),
                new AverageDurationAnalyzer(),
                new BadQualityAnalyzer(),
                new SleeplessNightsAnalyzer(),
                new ChronotypeAnalyzer()
        );
    }

    public static void main(String[] args) {
        // Проверка наличия пути к файлу с логами сна
        if (args.length == 0) {
            System.err.println("Ошибка: укажите путь к файлу с логом сна.");
            return;
        }

        String filePath = args[0];
        SleepTrackerApp app = new SleepTrackerApp();

        try {
            List<SleepSession> sessions = app.loadSleepData(filePath);

            if (sessions.isEmpty()) {
                System.out.println("Файл пуст");
                return;
            }

            System.out.println("Файл успешно загружен. Найдено сессий: " + sessions.size());
            System.out.println();
            System.out.println("|Результаты анализа сна|");

            // Прогоняет через все аналитики и выводит результат
            app.analyzers.stream().map(analyzer -> analyzer.apply(sessions)).forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Критическая ошибка при чтении файла: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    // Считывает файл построчно
    private List<SleepSession> loadSleepData(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .filter(line -> !line.isBlank()).map(this::parseLineToSession).collect(Collectors.toList());
    }

    // Парсит строку лога в SleepSession
    private SleepSession parseLineToSession(String line) {
        try {
            String[] parts = line.split(";");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Неверный формат колонок");
            }

            LocalDateTime start = LocalDateTime.parse(parts[0].trim(), DATE_TIME_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(parts[1].trim(), DATE_TIME_FORMATTER);
            Quality quality = Quality.valueOf(parts[2].trim().toUpperCase());

            return new SleepSession(start, end, quality);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка парсинга строки [" + line + "]: " + e.getMessage());
        }
    }
}