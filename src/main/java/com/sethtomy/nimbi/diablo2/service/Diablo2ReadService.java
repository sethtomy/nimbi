package com.sethtomy.nimbi.diablo2.service;

import com.sethtomy.nimbi.diablo2.db.TerrorZoneEntity;
import com.sethtomy.nimbi.diablo2.db.TerrorZoneRepository;
import com.sethtomy.nimbi.diablo2.domain.TerrorZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class Diablo2ReadService {
    public Optional<TerrorZone> currentTerrorZone;
    public Optional<TerrorZone> nextTerrorZone;
    private final Logger logger = LoggerFactory.getLogger(Diablo2ReadService.class);
    private final TerrorZoneRepository repository;
    private final TerrorZoneMapper mapper;
    private final Diablo2WriteService writeService;

    public Diablo2ReadService(TerrorZoneRepository repository, TerrorZoneMapper mapper, Diablo2WriteService writeService) {
        this.mapper = mapper;
        this.repository = repository;
        this.writeService = writeService;
        currentTerrorZone = Optional.empty();
        nextTerrorZone = Optional.empty();
    }

    public Optional<TerrorZone> getCurrentTerrorZone() {
        LocalDateTime now = LocalDateTime.now();
        if (currentTerrorZone.isPresent() && currentTerrorZone.get().isCurrent(now)) {
            return currentTerrorZone;
        }
        setCurrentFromDatabase(now);
        return currentTerrorZone;
    }

    public Optional<TerrorZone> getNextTerrorZone() {
        LocalDateTime now = LocalDateTime.now();
        if (nextTerrorZone.isPresent() && nextTerrorZone.get().isNext(now)) {
            return nextTerrorZone;
        }
        setNextFromDatabase(now);
        return nextTerrorZone;
    }

    private void setCurrentFromDatabase(LocalDateTime now) {
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        TerrorZoneEntity[] terrorZoneEntities = repository.findAllByStartDateGreaterThanEqualAndEndDateBefore(date, date);
        if (terrorZoneEntities.length == 0) {
            if (ignoreExternal(now)) {
                setCurrentTerrorZone(Optional.empty());
            } else {
                TerrorZone terrorZone = writeService.getCurrentTerrorZoneExternally();
                setCurrentTerrorZone(Optional.of(terrorZone));
            }
        } else if (terrorZoneEntities.length == 1) {
            TerrorZone terrorZone = mapper.entityToDomain(terrorZoneEntities[0]);
            setCurrentTerrorZone(Optional.of(terrorZone));
        } else {
            throw new RuntimeException();
        }
    }

    private void setNextFromDatabase(LocalDateTime now) {
        Date date = Date.from(now.plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        TerrorZoneEntity[] terrorZoneEntities = repository.findAllByStartDateGreaterThanEqualAndEndDateBefore(date, date);
        if (terrorZoneEntities.length == 0) {
            if (ignoreExternal(now)) {
                setNextTerrorZone(Optional.empty());
            } else {
                TerrorZone terrorZone = writeService.getNextTerrorZoneExternally();
                setNextTerrorZone(Optional.of(terrorZone));
            }
        } else if (terrorZoneEntities.length == 1) {
            TerrorZone terrorZone = mapper.entityToDomain(terrorZoneEntities[0]);
            setNextTerrorZone(Optional.of(terrorZone));
        } else {
            throw new RuntimeException();
        }
    }

    // Ignore External API for the first 2 minutes as it gets de-synchronized
    private boolean ignoreExternal(LocalDateTime now) {
        return now.getMinute() < 3;
    }

    private void setCurrentTerrorZone(Optional<TerrorZone> optional) {
        currentTerrorZone = optional;
        logger.info("Set current Terror Zone to {}", currentTerrorZone);
    }

    private void setNextTerrorZone(Optional<TerrorZone> optional) {
        nextTerrorZone = optional;
        logger.info("Set next Terror Zone to {}", nextTerrorZone);
    }
}
