package com.sethtomy.nimbi.diablo2.external.d2runewizard.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "terror_zone")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TerrorZoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private Date date;

    private int act;

    private String zone;
}
