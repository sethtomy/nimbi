package com.sethtomy.nimbi.diablo2.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface Diablo2InstalledChannelRepository extends CrudRepository<TerrorZoneEntity, Long> {
    @Query(value = "SELECT * FROM terror_zone ORDER BY id DESC LIMIT 2", nativeQuery = true)
    TerrorZoneEntity[] getLastTwoTerrorZones();
}
