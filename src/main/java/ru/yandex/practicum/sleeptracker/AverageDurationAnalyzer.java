package ru.yandex.practicum.sleeptracker;

import java.util.List;

// Считает среднее время сна, округляя результат до двух знаков после запятой
public class AverageDurationAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        double avg = sessions.stream().mapToLong(SleepSession::getDurationMinutes).average().orElse(0.0);

        return new SleepAnalysisResult("Средняя продолжительность сессии (мин)", Math.round(avg * 100.0) / 100.0);
    }
}
