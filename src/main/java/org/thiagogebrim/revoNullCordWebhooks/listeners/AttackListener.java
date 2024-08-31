package org.thiagogebrim.revoNullCordWebhooks.listeners;


import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.shieldcommunity.nullcordx.api.events.*;
import org.thiagogebrim.revoNullCordWebhooks.DiscordWebhook;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class AttackListener implements Listener {

    private final DiscordWebhook discordWebhook;

    public AttackListener(String webhookUrl) {
        this.discordWebhook = new DiscordWebhook(webhookUrl);
    }

    @EventHandler
    public void onBotAttackDetected(BotAttackDetectedEvent event) {
        sendWebhookAsync("Ataque de bot detectado.");
    }

    @EventHandler
    public void onBotAttackEnded(BotAttackEndedEvent event) {
        sendWebhookAsync("Ataque de bot finalizado.");
    }

    @EventHandler
    public void onPingAttackDetected(PingAttackDetectedEvent event) {
        sendWebhookAsync("Ataque de ping detectado.");
    }

    @EventHandler
    public void onPingAttackEnded(PingAttackEndedEvent event) {
        sendWebhookAsync("Ataque de ping finalizado.");
    }

    @EventHandler
    public void onSpamAttackDetected(SpamAttackDetectedEvent event) {
        sendWebhookAsync("Ataque de spam detectado.");
    }

    @EventHandler
    public void onSpamAttackEnded(SpamAttackEndedEvent event) {
        sendWebhookAsync("Ataque de spam finalizado.");
    }

    private void sendWebhookAsync(String message) {
        CompletableFuture.runAsync(() -> {
            discordWebhook.setContent(message);
            discordWebhook.setUsername("Revonildo");
            discordWebhook.setTts(false);

            try {
                discordWebhook.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
