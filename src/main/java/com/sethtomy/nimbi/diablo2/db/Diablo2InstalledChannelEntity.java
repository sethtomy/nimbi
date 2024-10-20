package com.sethtomy.nimbi.diablo2.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "terror_zone")
public class InstalledChannelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Getter
    @Setter
    private Date date;

    @Getter
    @Setter
    private String act;

    @Getter
    @Setter
    private String zone;

    protected InstalledChannelEntity() {}

    public InstalledChannelEntity(Date date, String act, String zone) {
        this.date = date;
        this.act = act;
        this.zone = zone;
    }
}
