package com.sethtomy.nimbi.diablo2.service;

import com.sethtomy.nimbi.diablo2.db.TerrorZoneEntity;
import com.sethtomy.nimbi.diablo2.db.TerrorZoneRepository;
import com.sethtomy.nimbi.diablo2.domain.TerrorZone;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Diablo2ReadService {
    private final TerrorZoneRepository repository;

    public Optional<TerrorZone> currentTerrorZone;

    @Setter
    public Optional<TerrorZone> nextTerrorZone;

    public TerrorZoneMapper mapper;

    public Diablo2ReadService(TerrorZoneRepository repository, TerrorZoneMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        currentTerrorZone = Optional.empty();
        nextTerrorZone = Optional.empty();
    }

    public Optional<TerrorZone> getCurrentTerrorZone() {
        if (currentTerrorZone.isEmpty()) {
            setFromDatabase();
        }
        return currentTerrorZone;
    }

    public void setCurrentTerrorZone(TerrorZone terrorZone) {
        currentTerrorZone = Optional.of(terrorZone);
    }

    public Optional<TerrorZone> getNextTerrorZone() {
        if (nextTerrorZone.isEmpty()) {
            setFromDatabase();
        }
        return nextTerrorZone;
    }

    private void setFromDatabase() {
        TerrorZoneEntity[] terrorZones = repository.getLastTwoTerrorZones();
        assert terrorZones.length == 2 || terrorZones.length == 0;
        if (terrorZones.length == 2) {
            TerrorZone currentTerrorZone = mapper.entityToDomain(terrorZones[0]);
            setCurrentTerrorZone(currentTerrorZone);
            TerrorZone nextTerrorZone = mapper.entityToDomain(terrorZones[1]);
            setNextTerrorZone(Optional.of(nextTerrorZone));
        }
    }
}
