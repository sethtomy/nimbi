package com.sethtomy.nimbi.diablo2.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record TerrorZone (
  UUID id,
  String zone,
  Act act,
  LocalDateTime dateTime
) {}
