/*
 * VitalMail is a Spigot Plugin that gives players the ability to set homes and teleport to them.
 * Copyright © 2022 Leopold Meinel & contributors
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

import com.google.common.collect.ImmutableMap;
import com.tamrielnetwork.vitalmail.VitalMail;
import com.tamrielnetwork.vitalmail.utils.Chat;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CmdSpec {

	private static final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);
	private static final List<UUID> onActiveDelay = new ArrayList<>();

	public static void doDelay(@NotNull CommandSender sender, Location location) {

		Player senderPlayer = (Player) sender;

		if (!senderPlayer.hasPermission("vitalspawn.delay.bypass")) {
			if (onActiveDelay.contains(senderPlayer.getUniqueId())) {
				Chat.sendMessage(sender, "active-delay");
				return;
			}
			onActiveDelay.add(senderPlayer.getUniqueId());
			String timeRemaining = String.valueOf(main.getConfig().getLong("delay.time"));
			Chat.sendMessage(senderPlayer, ImmutableMap.of("%countdown%", timeRemaining), "countdown");
			new BukkitRunnable() {

				@Override
				public void run() {

					if (Cmd.isInvalidPlayer(senderPlayer)) {
						onActiveDelay.remove(senderPlayer.getUniqueId());
						return;
					}

					senderPlayer.teleport(location);
					onActiveDelay.remove(senderPlayer.getUniqueId());
				}
			}.runTaskLater(main, (main.getConfig().getLong("delay.time") * 20L));
		} else {
			senderPlayer.teleport(location);
		}
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm, @NotNull String arg) {

		if (Cmd.isInvalidSender(sender)) {
			return true;
		}

		if (Cmd.isNotPermitted(sender, perm)) {
			return true;
		}

		return isInvalidName(sender, arg);
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm) {

		if (Cmd.isInvalidSender(sender)) {
			return true;
		}

		return Cmd.isNotPermitted(sender, perm);
	}

	public static boolean isInvalidLocation(Location location) {

		if (location == null) {
			return true;
		}
		return location.getWorld() == null;
	}

	public static int getAllowedHomes(@NotNull Player player, int defaultValue) {

		List<Integer> values = new ArrayList<>();
		values.add(defaultValue);

		String permissionPrefix = "vitalmail.homes.";

		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			if (attachmentInfo.getPermission().startsWith(permissionPrefix)) {
				String permission = attachmentInfo.getPermission();
				values.add(Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1)));
			}
		}
		return Collections.max(values);
	}

	private static boolean isInvalidName(@NotNull CommandSender sender, @NotNull String arg) {

		if (!arg.toLowerCase().matches("[a-z0-9]{1,16}")) {
			Chat.sendMessage(sender, "invalid-name");
			return true;
		}
		return false;
	}

}
