package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Считает количество ночей без сна
public class SleeplessNightsAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0L);
        }

        LocalDate startDate = sessions.get(0).getStart().getHour() >= 12
                ? sessions.get(0).getStart().toLocalDate()
                : sessions.get(0).getStart().toLocalDate().minusDays(1);

        LocalDate endDate = sessions.get(sessions.size() - 1).getEnd().getHour() < 12
                ? sessions.get(sessions.size() - 1).getEnd().toLocalDate().minusDays(1)
                : sessions.get(sessions.size() - 1).getEnd().toLocalDate();

        long totalNights = startDate.datesUntil(endDate.plusDays(1)).count();

        long nightsWithSleep = sessions.stream().flatMap(s -> {
                    LocalDate startDay = s.getStart().toLocalDate();
                    LocalDate endDay = s.getEnd().toLocalDate();

                    return startDay.datesUntil(endDay.plusDays(1))
                            .filter(date -> {
                                LocalDateTime nightStart = date.atStartOfDay();
                                LocalDateTime nightEnd = date.atTime(6, 0);

                                return s.getStart().isBefore(nightEnd) && s.getEnd().isAfter(nightStart);
                            })
                            .map(date -> date.minusDays(1));
                })
                .filter(nightDate -> !nightDate.isBefore(startDate) && !nightDate.isAfter(endDate))
                .distinct().count();

        return new SleepAnalysisResult("Количество бессонных ночей", totalNights - nightsWithSleep);
    }
}