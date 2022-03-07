/*
 * VitalMail is a Spigot Plugin that gives players the ability to set homes and teleport to them.
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
 * along with this program. If not, see https://github.com/TamrielNetwork/VitalHome/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalmail.utils.commands;

import com.tamrielnetwork.vitalmail.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

public class CmdSpec {

	public static boolean isInvalidLocation(@NotNull CommandSender sender, Location location) {

		if (location == null) {
			Bukkit.getLogger().severe("VitalMail cannot find homelocation in database");
			Chat.sendMessage(sender, "invalid-home");
			return true;
		}
		if (location.getWorld() == null) {
			Bukkit.getLogger().severe("VitalMail cannot find world in database");
			Chat.sendMessage(sender, "invalid-home");
			return true;
		}
		return false;
	}

	public static int getAllowedMails(Player player, int defaultValue) {

		String permissionPrefix = "vitalmail.mails.";

		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			if (attachmentInfo.getPermission().startsWith(permissionPrefix)) {
				String permission = attachmentInfo.getPermission();
				return Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1));
			}
		}

		player.sendMessage(String.valueOf(defaultValue));
		return defaultValue;
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, OfflinePlayer player, @NotNull String perm) {

		if (Cmd.isInvalidSender(sender)) {
			return true;
		}
		if (Cmd.isNotPermitted(sender, perm)) {
			return true;
		}
		return Cmd.isInvalidPlayer(sender, player);
	}

}
