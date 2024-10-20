package com.sethtomy.nimbi.diablo2;

import com.sethtomy.nimbi.diablo2.db.TerrorZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class Diablo2WriteCronService {
    private final Logger log = LoggerFactory.getLogger(Diablo2WriteCronService.class);
    private final TerrorZoneRepository repository;

    public Diablo2WriteCronService(TerrorZoneRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void updateDatabaseWithLatestTerrorZone() {
        log.info("The time is now {}", new Date());
    }
}
