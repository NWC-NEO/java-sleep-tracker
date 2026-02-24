package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {

    private List<SleepSession> sessions;

    @BeforeEach
    void setUp() {
        sessions = new ArrayList<>();
    }

    // Тесты для TotalSessionsAnalyzer
    @Test
    void totalSessions_shouldReturnCorrectCount() {
        sessions.add(createSession("01.10.25 22:00", "02.10.25 06:00", Quality.GOOD));
        sessions.add(createSession("02.10.25 23:00", "03.10.25 07:00", Quality.NORMAL));

        SleepAnalyzer analyzer = new TotalSessionsAnalyzer();
        assertEquals(2L, analyzer.apply(sessions).getValue(), "Должно быть 2 сессии");
    }

    // Тесты для Min/Max Duration
    @Test
    void durationAnalyzers_shouldReturnCorrectLimits() {
        sessions.add(createSession("01.10.25 12:00", "01.10.25 13:00", Quality.NORMAL)); // 60 мин
        sessions.add(createSession("01.10.25 22:00", "02.10.25 06:00", Quality.GOOD));   // 480 мин

        assertEquals(60L, new MinDurationAnalyzer().apply(sessions).getValue());
        assertEquals(480L, new MaxDurationAnalyzer().apply(sessions).getValue());
    }

    // Тесты для AverageDurationAnalyzer
    @Test
    void averageDuration_shouldHandleRounding() {
        sessions.add(createSession("01.10.25 22:00", "02.10.25 04:00", Quality.GOOD)); // 360 мин
        sessions.add(createSession("02.10.25 22:00", "03.10.25 04:15", Quality.GOOD)); // 375 мин

        Object result = new AverageDurationAnalyzer().apply(sessions).getValue();
        assertEquals(367.5, (Double) result, 0.001);
    }

    // Тесты для BadQualityAnalyzer
    @Test
    void badQuality_shouldCountOnlyBad() {
        sessions.add(createSession("01.10.25 22:00", "02.10.25 06:00", Quality.BAD));
        sessions.add(createSession("02.10.25 22:00", "03.10.25 06:00", Quality.GOOD));

        assertEquals(1L, new BadQualityAnalyzer().apply(sessions).getValue());
    }

    // Тесты для SleeplessNightsAnalyzer
    @Test
    void sleeplessNights_longSession_shouldCoverMultipleNights() {
        // Сессия пересекает интервал 00-06 дважды
        sessions.add(createSession("02.10.25 01:00", "03.10.25 05:00", Quality.GOOD));

        assertEquals(0L, new SleeplessNightsAnalyzer().apply(sessions).getValue(),
                "Одна длинная сессия должна закрыть две ночи");
    }

    @Test
    void sleeplessNights_morningSleep_shouldBeSleepless() {
        sessions.add(createSession("02.10.25 07:00", "02.10.25 11:00", Quality.NORMAL));

        assertEquals(1L, new SleeplessNightsAnalyzer().apply(sessions).getValue(),
                "Сон после 6 утра не считается ночным");
    }

    @Test
    void sleeplessNights_eveningSleep_shouldBeSleepless() {
        sessions.add(createSession("01.10.25 17:00", "01.10.25 23:00", Quality.NORMAL));

        assertEquals(1L, new SleeplessNightsAnalyzer().apply(sessions).getValue());
    }

    @Test
    void sleeplessNights_gapBetweenSessions_shouldFindSleeplessNight() {
        sessions.add(createSession("01.10.25 22:00", "02.10.25 06:00", Quality.GOOD));
        sessions.add(createSession("03.10.25 22:00", "04.10.25 06:00", Quality.GOOD));

        assertEquals(1L, new SleeplessNightsAnalyzer().apply(sessions).getValue(),
                "Должна быть одна бессонная ночь между сессиями");
    }

    // Тесты для ChronotypeAnalyzer
    @Test
    void chronotype_shouldIdentifyOwl() {
        sessions.add(createSession("01.10.25 23:30", "02.10.25 09:30", Quality.GOOD));
        sessions.add(createSession("02.10.25 23:45", "03.10.25 10:00", Quality.GOOD));

        assertEquals("Сова", new ChronotypeAnalyzer().apply(sessions).getValue());
    }

    @Test
    void chronotype_tie_shouldReturnPigeon() {
        sessions.add(createSession("01.10.25 23:30", "02.10.25 09:30", Quality.GOOD)); // Сова
        sessions.add(createSession("02.10.25 21:00", "03.10.25 06:00", Quality.GOOD)); // Жаворонок

        assertEquals("Голубь", new ChronotypeAnalyzer().apply(sessions).getValue());
    }

    // Преобразует строковые даты в объекты SleepSession для удобного создания тестовых данных
    private SleepSession createSession(String start, String end, Quality quality) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return new SleepSession(
                LocalDateTime.parse(start, formatter),
                LocalDateTime.parse(end, formatter),
                quality
        );
    }
}