package com.sethtomy.nimbi.diablo2.external.d2runewizard;

import com.sethtomy.nimbi.common.http.LoggerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class D2RuneWizardHttpConfig {
    private final RestClient.Builder clientBuilder;

    public D2RuneWizardHttpConfig(RestClient.Builder restClientBuilder) {
        this.clientBuilder = restClientBuilder;
    }

    @Bean
    public RestClient restClient() {
        return clientBuilder
          .baseUrl("https://d2runewizard.com/api")
          .defaultHeader("D2R-Contact", "sethtomy@gmail.com")
          .defaultHeader("D2R-Platform", "Discord")
          .defaultHeader("D2R-Repo", "https://github.com/sethtomy")
          .requestInterceptor(new LoggerInterceptor())
          .build();
    }
}
