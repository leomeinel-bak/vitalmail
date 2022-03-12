/*
 * VitalMail is a Spigot Plugin that gives players the ability to write mail to offline players.
 * Copyright © 2022 Leopold Meinel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://github.com/TamrielNetwork/VitalMail/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalhome.utils.commands;

import com.tamrielnetwork.vitalhome.utils.Chat;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Cmd {

	private Cmd() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean isArgsLengthSmallerThan(@NotNull CommandSender sender, @NotNull String[] args, int length) {
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
		if (player == null) {
			Chat.sendMessage(sender, "invalid-player");
			return true;
		}
		if (!player.hasPlayedBefore()) {
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
