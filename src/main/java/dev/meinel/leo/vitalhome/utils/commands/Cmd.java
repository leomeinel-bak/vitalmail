/*
 * File: Cmd.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.utils.commands;

import dev.meinel.leo.vitalhome.utils.Chat;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Cmd {

    private Cmd() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isArgsLengthSmallerThan(@NotNull CommandSender sender,
            @NotNull String[] args, int length) {
        if (args.length < length) {
            Chat.sendMessage(sender, "cmd");
            return true;
        }
        return false;
    }

    public static boolean isNotPermitted(@NotNull CommandSender sender, @NotNull String perm) {
        if (!sender.hasPermission(perm)) {
            Chat.sendMessage(sender, "no-perms");
            return true;
        }
        return false;
    }

    public static boolean isInvalidSender(@NotNull CommandSender sender) {
        if (!(sender instanceof Player)) {
            Chat.sendMessage(sender, "player-only");
            return true;
        }
        return false;
    }

    public static boolean isInvalidPlayer(@NotNull CommandSender sender, OfflinePlayer player) {
        if (player == null || !player.hasPlayedBefore()) {
            Chat.sendMessage(sender, "invalid-player");
            return true;
        }
        if (player == sender) {
            Chat.sendMessage(sender, "same-player");
            return true;
        }
        return false;
    }
}
