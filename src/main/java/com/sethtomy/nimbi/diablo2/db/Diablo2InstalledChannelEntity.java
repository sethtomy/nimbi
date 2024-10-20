package com.sethtomy.nimbi.diablo2.db;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "d2_installed_channel")
public class Diablo2InstalledChannelEntity {
    @Id
    private String channelId;

    @Setter
    @Column
    @Nullable
    private UUID lastCurrentTerrorZoneIdSent;

    @Setter
    @Column
    @Nullable
    private UUID lastNextTerrorZoneIdSent;

    protected Diablo2InstalledChannelEntity() {}
}
