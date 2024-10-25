package com.sethtomy.nimbi.diablo2.service;

import com.sethtomy.nimbi.diablo2.db.TerrorZoneEntity;
import com.sethtomy.nimbi.diablo2.db.TerrorZoneRepository;
import com.sethtomy.nimbi.diablo2.domain.TerrorZone;
import com.sethtomy.nimbi.diablo2.external.d2runewizard.D2RuneWizardApiService;
import com.sethtomy.nimbi.diablo2.external.d2runewizard.dto.TerrorZoneReportDto;
import jakarta.annotation.PostConstruct;
import org.antlr.v4.runtime.misc.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class Diablo2WriteService {
    private final Logger logger = LoggerFactory.getLogger(Diablo2WriteService.class);
    private final TerrorZoneRepository repository;
    private final D2RuneWizardApiService d2RuneWizardApiService;
    private final TerrorZoneMapper mapper;
    private final Diablo2ReadService readService;

    public Diablo2WriteService(
      TerrorZoneRepository repository,
      D2RuneWizardApiService d2RuneWizardApiService,
      TerrorZoneMapper mapper,
      Diablo2ReadService readService
    ) {
        this.repository = repository;
        this.d2RuneWizardApiService = d2RuneWizardApiService;
        this.mapper = mapper;
        this.readService = readService;
    }

    @PostConstruct
    @Scheduled(cron = "0 */1 * * * *")
    public void updateDatabaseWithLatestTerrorZones() {
        Optional<TerrorZone> currentOptional = readService.getCurrentTerrorZone();
        Optional<TerrorZone> nextOptional = readService.getNextTerrorZone();
        if (currentOptional.isEmpty()) {
            setBoth();
        } else if (needsRefresh(currentOptional.get(), nextOptional)) {
            conditionallySetBoth(currentOptional.get(), nextOptional);
        } else {
            logger.info("Terror Zone already up to date, no need to query externally.");
        }
    }

    private void setBoth() {
        logger.info("No entries in database, bootstrapping from D2 Rune Wizard");
        Pair<TerrorZone, TerrorZone> terrorZones = getFromD2RuneWizard();
        persistCurrent(terrorZones.a);
        persistNext(terrorZones.b);
    }

    private void conditionallySetBoth(TerrorZone currentTerrorZone, Optional<TerrorZone> nextOptional) {
        logger.info("Terror Zones need a refresh from D2 Rune Wizard");
        Pair<TerrorZone, TerrorZone> terrorZones = getFromD2RuneWizard();
        if (isNewTerrorZone(Optional.of(currentTerrorZone), terrorZones.a)) {
            persistCurrent(terrorZones.a);
        } else if (isNewTerrorZone(nextOptional, terrorZones.b)) {
            persistNext(terrorZones.b);
        } else {
            logger.info("Terror Zones are the same, no need to update");
        }
    }

    private boolean needsRefresh(TerrorZone currentTerrorZone, Optional<TerrorZone> nextTerrorZone) {
        // This happens when we pre-send the next terror zone as the current
        if (nextTerrorZone.isEmpty()) {
            return true;
        }
        LocalDateTime dateTime = LocalDateTime.now();
        return currentTerrorZone.dateTime().plusHours(1).isBefore(dateTime);
    }

    private boolean isNewTerrorZone(Optional<TerrorZone> optional, TerrorZone b) {
        if (optional.isEmpty()) {
            return true;
        }
        boolean isSameZone = optional.get().zone().equals(b.zone());
        boolean isSameAct = optional.get().act() == b.act();
        return !(isSameAct && isSameZone);
    }

    private Pair<TerrorZone, TerrorZone> getFromD2RuneWizard() {
        TerrorZoneReportDto dto = d2RuneWizardApiService.getTerrorZone();
        LocalDateTime currentHour = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        TerrorZone currentTerrorZone = mapper.dtoToDomain(dto.currentTerrorZone(), currentHour);
        LocalDateTime nextHour = currentHour.plusHours(1);
        TerrorZone nextTerrorZone = mapper.dtoToDomain(dto.nextTerrorZone(), nextHour);
        return new Pair<>(currentTerrorZone, nextTerrorZone);
    }

    private void persistCurrent(TerrorZone terrorZone) {
        TerrorZoneEntity currentEntity = mapper.domainToEntity(terrorZone);
        repository.save(currentEntity);
        readService.setCurrentTerrorZone(terrorZone);
    }

    private void persistNext(TerrorZone terrorZone) {
        TerrorZoneEntity nextEntity = mapper.domainToEntity(terrorZone);
        repository.save(nextEntity);
        readService.setNextTerrorZone(Optional.of(terrorZone));
    }
}
