package ru.yandex.practicum.sleeptracker;

import java.util.List;

// Подсчитывает количество BAD сессий
public class BadQualityAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        long count = sessions.stream().filter(s -> s.getQuality() == Quality.BAD).count();

        return new SleepAnalysisResult("Количество сессий с плохим качеством сна", count);
    }
}