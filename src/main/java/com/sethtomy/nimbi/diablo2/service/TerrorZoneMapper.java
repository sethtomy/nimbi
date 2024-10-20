package com.sethtomy.nimbi.diablo2.service;

import com.sethtomy.nimbi.diablo2.db.TerrorZoneEntity;
import com.sethtomy.nimbi.diablo2.domain.Act;
import com.sethtomy.nimbi.diablo2.domain.TerrorZone;
import com.sethtomy.nimbi.diablo2.external.d2runewizard.dto.TerrorZoneDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class TerrorZoneMapper {
    public TerrorZone dtoToDomain(TerrorZoneDto terrorZoneDto, LocalDateTime dateTime) {
        return new TerrorZone(UUID.randomUUID(), terrorZoneDto.zone(), translateAct(terrorZoneDto.act()), dateTime);
    }

    public TerrorZoneEntity domainToEntity(TerrorZone terrorZone) {
        Date date = Date.from(terrorZone.dateTime().atZone(ZoneId.systemDefault()).toInstant());
        return new TerrorZoneEntity(terrorZone.id(), date, terrorZone.act().toString(), terrorZone.zone());
    }

    public TerrorZone entityToDomain(TerrorZoneEntity entity) {
        return new TerrorZone(
          entity.getId(),
          entity.getZone(),
          translateAct(entity.getAct()),
          entity.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    private Act translateAct(String act) {
        return switch (act.toLowerCase()) {
            case "act1", "act i" -> Act.I;
            case "act2", "act ii" -> Act.II;
            case "act3", "act iii" -> Act.III;
            case "act4", "act iv" -> Act.IV;
            case "act5", "act v" -> Act.V;
            default -> throw new RuntimeException();
        };
    }
}
