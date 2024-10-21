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
    public void updateDatabaseWithLatestTerrorZone() {
        Optional<TerrorZone> optional = readService.getCurrentTerrorZone();
        if (optional.isEmpty()) {
            logger.info("No entries in database, bootstrapping from D2 Rune Wizard");
            Pair<TerrorZone, TerrorZone> terrorZones = getFromD2RuneWizard();
            saveToDatabase(terrorZones.a, terrorZones.b);
            updateReadService(terrorZones.a, terrorZones.b);
        } else if (needsRefresh(optional.get())) {
            logger.info("Terror Zone needs a refresh from D2 Rune Wizard");
            Pair<TerrorZone, TerrorZone> terrorZones = getFromD2RuneWizard();
            if (isNewTerrorZone(optional.get(), terrorZones.a)) {
                saveToDatabase(terrorZones.a, terrorZones.b);
                updateReadService(terrorZones.a, terrorZones.b);
                logger.info("Terror Zones updated in database.");
            } else {
                logger.info("Terror Zone is the same, no need to update");
            }
        } else {
            logger.info("Terror Zone already up to date, no need to query externally.");
        }
    }

    @Scheduled(cron = "58 59 * * * *")
    public void preSendNextTerrorZone() {
        readService.setCurrentTerrorZone(readService.getNextTerrorZone().orElseThrow());
        readService.setNextTerrorZone(Optional.empty());
    }

    private boolean needsRefresh(TerrorZone currentTerrorZone) {
        LocalDateTime dateTime = LocalDateTime.now();
        return currentTerrorZone.dateTime().plusHours(1).isBefore(dateTime);
    }

    private boolean isNewTerrorZone(TerrorZone a, TerrorZone b) {
        boolean isSameZone = a.zone().equals(b.zone());
        boolean isSameAct = a.act() == b.act();
        return !(isSameAct && isSameZone);
    }

    private void updateReadService(TerrorZone currentTerrorZone, TerrorZone nextTerrorZone) {
        readService.setCurrentTerrorZone(currentTerrorZone);
        readService.setNextTerrorZone(Optional.of(nextTerrorZone));
    }

    private Pair<TerrorZone, TerrorZone> getFromD2RuneWizard() {
        TerrorZoneReportDto dto = d2RuneWizardApiService.getTerrorZone();
        LocalDateTime currentHour = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        TerrorZone currentTerrorZone = mapper.dtoToDomain(dto.currentTerrorZone(), currentHour);
        LocalDateTime nextHour = currentHour.plusHours(1);
        TerrorZone nextTerrorZone = mapper.dtoToDomain(dto.nextTerrorZone(), nextHour);
        return new Pair<>(currentTerrorZone, nextTerrorZone);
    }

    private void saveToDatabase(TerrorZone currentTerrorZone, TerrorZone nextTerrorZone) {
        TerrorZoneEntity currentEntity = mapper.domainToEntity(currentTerrorZone);
        TerrorZoneEntity nextEntity = mapper.domainToEntity(nextTerrorZone);
        repository.save(currentEntity);
        repository.save(nextEntity);
    }
}
