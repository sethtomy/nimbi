package com.sethtomy.nimbi.diablo2.external.d2runewizard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TerrorZoneReportDto(
  @JsonProperty("currentTerrorZone") TerrorZoneDto currentTerrorZone,
  @JsonProperty("nextTerrorZone") TerrorZoneDto nextTerrorZone
) {}
