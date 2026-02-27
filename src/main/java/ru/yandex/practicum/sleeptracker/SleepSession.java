package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.Duration;

public class SleepSession {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Quality quality;

    public SleepSession(LocalDateTime start, LocalDateTime end, Quality quality) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Время начала не может быть позже времени окончания.");
        }
        this.start = start;
        this.end = end;
        this.quality = quality;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Quality getQuality() {
        return quality;
    }

    public long getDurationMinutes() {
        return Duration.between(start, end).toMinutes();
    }

    public boolean isNightSession() {
        // Ночь для дня начала сессии
        LocalDateTime nightStart = start.toLocalDate().atStartOfDay();
        LocalDateTime nightEnd = start.toLocalDate().atTime(6, 0);

        // Ночь для следующего дня, если сессия началась поздно вечером
        LocalDateTime nextNightStart = start.toLocalDate().plusDays(1).atStartOfDay();
        LocalDateTime nextNightEnd = start.toLocalDate().plusDays(1).atTime(6, 0);

        boolean intersectsCurrentNight = start.isBefore(nightEnd) && end.isAfter(nightStart);
        boolean intersectsNextNight = start.isBefore(nextNightEnd) && end.isAfter(nextNightStart);

        return intersectsCurrentNight || intersectsNextNight;
    }

    @Override
    public String toString() {
        return String.format("Сон: %s - %s (%s)", start, end, quality);
    }
}