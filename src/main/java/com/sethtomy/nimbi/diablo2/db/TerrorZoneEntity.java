package com.sethtomy.nimbi.diablo2.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Entity
@Table(name = "terror_zone")
public class TerrorZoneEntity {
    @Id
    private UUID id;

    @Setter
    private Date date;

    @Setter
    private String act;

    @Setter
    private String zone;

    protected TerrorZoneEntity() {}

    public TerrorZoneEntity(UUID id, Date date, String act, String zone) {
        this.id = id;
        this.date = date;
        this.act = act;
        this.zone = zone;
    }
}
