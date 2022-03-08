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

package com.tamrielnetwork.vitalhome.utils.commands;

import com.tamrielnetwork.vitalhome.VitalMail;
import com.tamrielnetwork.vitalhome.utils.Chat;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CmdSpec {

	private static final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);
	private static final HashMap<UUID, Long> cooldownMap = new HashMap<>();

	private CmdSpec() {

		throw new IllegalStateException("Utility class");
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, OfflinePlayer player, @NotNull String perm, @NotNull String[] args) {

		if (Cmd.isInvalidSender(sender)) {
			return true;
		}

		if (Cmd.isNotPermitted(sender, perm)) {
			return true;
		}

		if (Cmd.isInvalidPlayer(sender, player)) {
			return true;
		}

		if (isInvalidName(sender, args)) {
			return true;
		}

		return isOnCooldown(sender);
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm) {

		if (Cmd.isInvalidSender(sender)) {
			return true;
		}

		return Cmd.isNotPermitted(sender, perm);
	}
/*
	public static int getAllowedMails(@NotNull OfflinePlayer player, int defaultValue) {

		List<Integer> values = new ArrayList<>();
		values.add(defaultValue);

		String permissionPrefix = "vitalhome.homes.";

		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			if (attachmentInfo.getPermission().startsWith(permissionPrefix)) {
				String permission = attachmentInfo.getPermission();
				values.add(Integer.parseInt(permission.substring(permission.lastIndexOf(".") + 1)));
			}
		}
		return Collections.max(values);
	}

 */

	private static boolean isInvalidName(@NotNull CommandSender sender, @NotNull String[] args) {

		for (String arg : args) {
			if (!arg.toLowerCase().matches("[a-z0-9]{1,16}")) {
				Chat.sendMessage(sender, "invalid-mail");
				return true;
			}
		}

		return false;
	}

	private static void clearMap(@NotNull CommandSender sender) {

		Player senderPlayer = (Player) sender;
		cooldownMap.remove(senderPlayer.getUniqueId());
	}

	private static void doTiming(@NotNull CommandSender sender) {

		new BukkitRunnable() {

			@Override
			public void run() {

				clearMap(sender);
			}
		}.runTaskLaterAsynchronously(main, (main.getConfig().getLong("cooldown.time") * 20L));
	}

	private static boolean isOnCooldown(@NotNull CommandSender sender) {

		Player senderPlayer = (Player) sender;

		boolean isOnCooldown = main.getConfig().getBoolean("cooldown.enabled") && !sender.hasPermission("vitalskull.cooldown.bypass") && cooldownMap.containsKey(senderPlayer.getUniqueId());

		if (isOnCooldown) {
			String timeRemaining = String.valueOf(cooldownMap.get(senderPlayer.getUniqueId()) - System.currentTimeMillis() / 1000);
			Chat.sendMessage(sender, Map.of("%time-left%", timeRemaining), "cooldown-active");
			return true;
		}
		cooldownMap.put(senderPlayer.getUniqueId(), main.getConfig().getLong("cooldown.time") + System.currentTimeMillis() / 1000);
		doTiming(sender);
		return false;
	}

}
