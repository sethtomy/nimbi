package com.sethtomy.nimbi.diablo2.service;

import com.sethtomy.nimbi.diablo2.db.Diablo2InstalledChannelEntity;
import com.sethtomy.nimbi.diablo2.db.Diablo2InstalledChannelRepository;
import com.sethtomy.nimbi.diablo2.domain.TerrorZone;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Diablo2BotService {
    private final Logger logger = LoggerFactory.getLogger(Diablo2BotService.class);
    private final GatewayDiscordClient discordClient;
    private final Diablo2InstalledChannelRepository repository;
    private final Diablo2ReadService readService;

    public Diablo2BotService(
      GatewayDiscordClient discordClient,
      Diablo2InstalledChannelRepository repository,
      Diablo2ReadService readService
    ) {
        this.discordClient = discordClient;
        this.repository = repository;
        this.readService = readService;
    }

    @PostConstruct
    @Scheduled(cron = "0 */1 * * * *")
    private void sendCurrentTerrorZoneMessage() {
        Iterable<Diablo2InstalledChannelEntity> entities = repository.findAll();
        TerrorZone terrorZone = readService.getCurrentTerrorZone().orElseThrow();
        entities.forEach(entity -> {
            UUID id = entity.getLastCurrentTerrorZoneIdSent();
            if (id == null || !id.equals(terrorZone.id())) {
                logger.info("Sending latest current to Channel {}", entity.getChannelId());
                sendMessage(entity.getChannelId(), terrorZone, "Current Terror Zone");
                entity.setLastCurrentTerrorZoneIdSent(terrorZone.id());
                repository.save(entity);
            } else {
                logger.info("Channel {} already has latest current, skipping.", entity.getChannelId());
            }
        });
    }

    @PostConstruct
    @Scheduled(cron = "0 */1 * * * *")
    private void sendNextTerrorZoneMessage() {
        Iterable<Diablo2InstalledChannelEntity> entities = repository.findAll();
        TerrorZone terrorZone = readService.getNextTerrorZone().orElseThrow();
        entities.forEach(entity -> {
            UUID id = entity.getLastNextTerrorZoneIdSent();
            if (id == null || !id.equals(terrorZone.id())) {
                logger.info("Sending latest next to Channel {}", entity.getChannelId());
                sendMessage(entity.getChannelId(), terrorZone, "Next Terror Zone");
                entity.setLastNextTerrorZoneIdSent(terrorZone.id());
                repository.save(entity);
            } else {
                logger.info("Channel {} already has latest next, skipping.", entity.getChannelId());
            }
        });
    }

    private void sendMessage(String channelId, TerrorZone terrorZone, String title) {
        discordClient
          .getChannelById(Snowflake.of(channelId))
          .ofType(GuildMessageChannel.class)
          .flatMap(channel -> channel.createMessage(getEmbed(terrorZone, title)))
          .block();
    }

    private EmbedCreateSpec getEmbed(TerrorZone terrorZone, String title) {
        return EmbedCreateSpec
          .builder()
          .color(Color.CYAN)
          .title(title)
          .description(terrorZone.act() + " - " + terrorZone.zone())
          .build();
    }
}
