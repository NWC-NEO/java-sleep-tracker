package ru.yandex.practicum.sleeptracker;

import java.util.List;

// Считает количество сессий сна
public class TotalSessionsAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        return new SleepAnalysisResult("Общее количество сессий сна", (long) sessions.size());
    }
}