package com.sethtomy.nimbi.diablo2.external.d2runewizard;

import com.sethtomy.nimbi.diablo2.external.d2runewizard.dto.TerrorZoneReportDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class D2RuneWizardApiService {

    private final RestClient restClient;

    private final String token;

    public D2RuneWizardApiService(RestClient restClient, @Value("${d2.runewizard.token}") String token) {
        this.restClient = restClient;
        this.token = token;
    }

    public TerrorZoneReportDto getTerrorZone() {
        return restClient
          .get()
          .uri(uriBuilder -> uriBuilder
            .path("/terror-zone")
            .queryParam("token", token)
            .build())
          .retrieve()
          .body(TerrorZoneReportDto.class);
    }
}
