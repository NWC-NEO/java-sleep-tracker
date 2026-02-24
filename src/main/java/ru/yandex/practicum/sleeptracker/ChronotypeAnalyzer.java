package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.stream.Collectors;

// Определяет тип пользователя
public class ChronotypeAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        List<SleepSession> nightSessions = sessions.stream()
                .filter(SleepSession::isNightSession).collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип", "Данные отсутствуют");
        }

        long owlCount = nightSessions.stream()
                .filter(s -> s.getStart().getHour() >= 23 && s.getEnd().getHour() >= 9).count();

        long larkCount = nightSessions.stream()
                .filter(s -> s.getStart().getHour() < 22 && s.getEnd().getHour() < 7).count();

        long pigeonCount = nightSessions.size() - owlCount - larkCount;

        String chronotype;
        if (owlCount > larkCount && owlCount > pigeonCount) {
            chronotype = "Сова";
        } else if (larkCount > owlCount && larkCount > pigeonCount) {
            chronotype = "Жаворонок";
        } else {
            chronotype = "Голубь";
        }

        return new SleepAnalysisResult("Хронотип", chronotype);
    }
}