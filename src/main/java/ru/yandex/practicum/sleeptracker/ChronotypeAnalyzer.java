package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.stream.Collectors;

// Определяет тип пользователя
public class ChronotypeAnalyzer implements SleepAnalyzer {

    private static final int OWL_START_HOUR = 23;
    private static final int OWL_END_HOUR = 9;
    private static final int LARK_START_HOUR = 22;
    private static final int LARK_END_HOUR = 7;

    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        List<SleepSession> nightSessions = sessions.stream()
                .filter(SleepSession::isNightSession).collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип", Chronotype.NO_DATA);
        }

        long owlCount = nightSessions.stream()
                .filter(s -> s.getStart().getHour() >= OWL_START_HOUR
                        && s.getEnd().getHour() >= OWL_END_HOUR).count();

        long larkCount = nightSessions.stream()
                .filter(s -> s.getStart().getHour() < LARK_START_HOUR
                        && s.getEnd().getHour() < LARK_END_HOUR).count();

        long pigeonCount = nightSessions.size() - owlCount - larkCount;

        Chronotype chronotype;
        if (owlCount > larkCount && owlCount > pigeonCount) {
            chronotype = Chronotype.OWL;
        } else if (larkCount > owlCount && larkCount > pigeonCount) {
            chronotype = Chronotype.LARK;
        } else {
            chronotype = Chronotype.PIGEON;
        }

        return new SleepAnalysisResult("Хронотип", chronotype);
    }
}