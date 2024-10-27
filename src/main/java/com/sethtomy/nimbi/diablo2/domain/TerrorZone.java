package com.sethtomy.nimbi.diablo2.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record TerrorZone(UUID id, String zone, Act act, LocalDateTime startDate, LocalDateTime endDate) {
    public boolean isCurrent(LocalDateTime now) {
        return isBetween(now);
    }

    public boolean isNext(LocalDateTime now) {
        LocalDateTime nextHour = now.plusHours(1);
        return isBetween(nextHour);
    }

    private boolean isBetween(LocalDateTime time) {
        if (time.isEqual(startDate)) {
            return true;
        }
        return time.isAfter(startDate) && time.isBefore(endDate);
    }
}
