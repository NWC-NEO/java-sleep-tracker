package ru.yandex.practicum.sleeptracker;

import java.util.List;

// Находит самую длинную сессию сна
public class MaxDurationAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        long max = sessions.stream().mapToLong(SleepSession::getDurationMinutes).max().orElse(0);

        return new SleepAnalysisResult("Максимальная продолжительность сессии (мин)", max);
    }
}