package com.sethtomy.nimbi.diablo2.service;

import com.sethtomy.nimbi.diablo2.db.TerrorZoneEntity;
import com.sethtomy.nimbi.diablo2.db.TerrorZoneRepository;
import com.sethtomy.nimbi.diablo2.domain.TerrorZone;
import com.sethtomy.nimbi.diablo2.external.d2runewizard.D2RuneWizardApiService;
import com.sethtomy.nimbi.diablo2.external.d2runewizard.dto.TerrorZoneReportDto;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class Diablo2WriteService {
    private final TerrorZoneRepository repository;
    private final D2RuneWizardApiService d2RuneWizardApiService;
    private final TerrorZoneMapper mapper;

    public Diablo2WriteService(
      TerrorZoneRepository repository,
      D2RuneWizardApiService d2RuneWizardApiService,
      TerrorZoneMapper mapper
    ) {
        this.repository = repository;
        this.d2RuneWizardApiService = d2RuneWizardApiService;
        this.mapper = mapper;
    }

    public TerrorZone getCurrentTerrorZoneExternally() {
        TerrorZone terrorZone = getFromD2RuneWizard().a;
        TerrorZoneEntity entity = mapper.domainToEntity(terrorZone);
        repository.save(entity);
        return terrorZone;
    }

    public TerrorZone getNextTerrorZoneExternally() {
        TerrorZone terrorZone = getFromD2RuneWizard().b;
        TerrorZoneEntity entity = mapper.domainToEntity(terrorZone);
        repository.save(entity);
        return terrorZone;
    }

    private Pair<TerrorZone, TerrorZone> getFromD2RuneWizard() {
        TerrorZoneReportDto dto = d2RuneWizardApiService.getTerrorZone();
        LocalDateTime currentHour = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        TerrorZone currentTerrorZone = mapper.dtoToDomain(dto.currentTerrorZone(), currentHour);
        LocalDateTime nextHour = currentHour.plusHours(1);
        TerrorZone nextTerrorZone = mapper.dtoToDomain(dto.nextTerrorZone(), nextHour);
        return new Pair<>(currentTerrorZone, nextTerrorZone);
    }
}
