/*
 * File: MailStorage.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.storage;

import dev.meinel.leo.vitalhome.VitalMail;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public abstract class MailStorage {

    protected final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);

    public abstract List<Map<String, String>> loadMail(@NotNull String receiverUUID);

    public abstract void saveMail(@NotNull OfflinePlayer receiverPlayer,
            @NotNull Player senderPlayer, String time, @NotNull String mail);

    public abstract void clear(@NotNull String receiverUUID);

    public boolean hasMail(@NotNull String receiverUUID) {
        return loadMail(receiverUUID) != null && !loadMail(receiverUUID).isEmpty();
    }
}
