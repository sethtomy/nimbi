package com.sethtomy.nimbi.diablo2.db;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface TerrorZoneRepository extends CrudRepository<TerrorZoneEntity, Long> {
    TerrorZoneEntity[] findAllByStartDateGreaterThanEqualAndEndDateBefore(Date startDate, Date endDate);
}
