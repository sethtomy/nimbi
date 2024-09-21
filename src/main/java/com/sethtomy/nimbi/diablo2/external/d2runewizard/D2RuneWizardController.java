package com.sethtomy.nimbi.diablo2.external.d2runewizard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class D2RuneWizardController {
    private final D2RuneWizardApiService d2RunewizardApiService;

    public D2RuneWizardController(D2RuneWizardApiService d2RunewizardApiService) {
        this.d2RunewizardApiService = d2RunewizardApiService;
    }

    @GetMapping("/terror-zone")
    public String getTerrorZone() {
        return d2RunewizardApiService
          .getTerrorZone()
          .getBody();
    }
}
