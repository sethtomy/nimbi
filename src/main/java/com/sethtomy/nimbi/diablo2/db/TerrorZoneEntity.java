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
    private Date startDate;

    @Setter
    private Date endDate;

    @Setter
    private String act;

    @Setter
    private String zone;

    protected TerrorZoneEntity() {}

    public TerrorZoneEntity(UUID id, Date startDate, Date endDate, String act, String zone) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.act = act;
        this.zone = zone;
    }
}
