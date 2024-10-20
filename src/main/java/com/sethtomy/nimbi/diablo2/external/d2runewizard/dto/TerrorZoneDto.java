package com.sethtomy.nimbi.diablo2.external.d2runewizard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TerrorZoneDto(
  @JsonProperty("zone") String zone,
  @JsonProperty("act") String act
) {}
