package com.sethtomy.nimbi.diablo2.external.d2runewizard;

import com.sethtomy.nimbi.util.http.LoggerInterceptor;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class D2RuneWizardHttpConfig {
    private final ObservationRegistry observationRegistry;

    public D2RuneWizardHttpConfig(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
          .baseUrl("https://d2runewizard.com/api")
          .defaultHeader("D2R-Contact", "sethtomy@gmail.com")
          .defaultHeader("D2R-Platform", "Discord")
          .defaultHeader("D2R-Repo", "https://github.com/sethtomy")
          .observationRegistry(observationRegistry)
          .requestInterceptor(new LoggerInterceptor())
          .build();
    }
}
