package ru.yandex.practicum.sleeptracker;

import java.util.List;

// Находит самую короткую сессию сна
public class MinDurationAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        long min = sessions.stream().mapToLong(SleepSession::getDurationMinutes).min().orElse(0);

        return new SleepAnalysisResult("Минимальная продолжительность сессии (мин)", min);
    }
}