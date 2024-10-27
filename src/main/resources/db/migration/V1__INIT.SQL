CREATE TABLE terror_zone
(
    id   UUID PRIMARY KEY,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    act  TEXT NOT NULL,
    zone TEXT NOT NULL
);

CREATE TABLE d2_installed_channel
(
    channel_id TEXT NOT NULL,
    last_current_terror_zone_id_sent UUID,
    last_next_terror_zone_id_sent UUID
);
