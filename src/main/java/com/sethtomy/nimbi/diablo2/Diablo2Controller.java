package com.sethtomy.nimbi.diablo2;

import com.sethtomy.nimbi.diablo2.domain.TerrorZone;
import com.sethtomy.nimbi.diablo2.service.Diablo2ReadService;
import com.sethtomy.nimbi.diablo2.service.Diablo2WriteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class Diablo2Controller {
    private final Diablo2WriteService writeService;

    private final Diablo2ReadService readService;

    public Diablo2Controller(Diablo2WriteService writeService, Diablo2ReadService readService) {
        this.writeService = writeService;
        this.readService = readService;
    }

    @GetMapping("/diablo-2/terror-zone")
    public TerrorZone getTerrorZone() {
        writeService.updateDatabaseWithLatestTerrorZones();
        Optional<TerrorZone> optional = readService.getCurrentTerrorZone();
        return optional.orElseThrow();
    }
}
