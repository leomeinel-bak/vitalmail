/*
 * File: PlayerJoin.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.listeners;

import dev.meinel.leo.vitalhome.VitalMail;
import dev.meinel.leo.vitalhome.utils.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class PlayerJoin implements Listener {

    private final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String receiverUUID = player.getUniqueId().toString();
        new BukkitRunnable() {

            @Override
            public void run() {
                if (main.getMailStorage().hasMail(receiverUUID)) {
                    Chat.sendMessage(player, "new-mail");
                }
            }
        }.runTaskLaterAsynchronously(main, 20);
    }
}
