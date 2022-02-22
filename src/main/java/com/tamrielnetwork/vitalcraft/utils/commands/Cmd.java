/*
 * VitalCraft is a Spigot Plugin that gives players the ability to open a crafting interface.
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
 * along with this program. If not, see https://github.com/TamrielNetwork/VitalCraft/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalcraft.utils.commands;

import com.tamrielnetwork.vitalcraft.utils.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Cmd {
	public static boolean isArgsLengthNotEqualTo(@NotNull CommandSender sender, @NotNull String[] args, int length) {
		if (args.length != length) {
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
}
