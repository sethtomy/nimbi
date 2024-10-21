package com.sethtomy.nimbi.diablo2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

// D2 Rune Wizard takes a minute or two to update, pre-send the next zone as the current.
@Service
public class Diablo2EagerNextTerrorZoneService {
    private final Logger logger = LoggerFactory.getLogger(Diablo2EagerNextTerrorZoneService.class);
    private final Diablo2ReadService readService;

    public Diablo2EagerNextTerrorZoneService(Diablo2ReadService readService) {
        this.readService = readService;
    }

    @Scheduled(cron = "30 59 * * * *")
    public void presetNextTerrorZone() {
        readService.setCurrentTerrorZone(readService.getNextTerrorZone().orElseThrow());
        readService.setNextTerrorZone(Optional.empty());
        logger.info("Preset next Terror Zone as current.");
    }
}
