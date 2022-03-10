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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CmdSpec {

	private static final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);
	private static final HashMap<UUID, Long> cooldownMap = new HashMap<>();

	private CmdSpec() {

		throw new IllegalStateException("Utility class");
	}

	public static void sendMail(@NotNull String @NotNull [] args, OfflinePlayer receiverPlayer, Player senderPlayer) {

		StringBuilder mailBuilder = new StringBuilder();
		for (String arg : args) {
			if (arg.equals(args[0]) || arg.equals(args[1])) continue;
			mailBuilder.append(" ").append(arg);
		}
		if (mailBuilder.length() > 64) {
			Chat.sendMessage(senderPlayer, "invalid-mail");
			return;
		}

		long time = System.currentTimeMillis();
		String timeString = Long.toString(time);
		String mail = mailBuilder.toString();

		main.getMailStorage().saveMail(receiverPlayer, senderPlayer, timeString, mail);
	}

	public static void readMail(@NotNull CommandSender sender, String receiverUUID, List<Map<String, String>> mailList) {

		if (mailList == null || mailList.isEmpty()) {
			Chat.sendMessage(sender, "no-mail");
			return;
		}

		final String NOENTRYEXCEPTION = "Invalid Entry found inside: mail/" + receiverUUID + ".yml";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss (MM/dd/yy)");
		List<String> senders = new ArrayList<>();
		List<String> times = new ArrayList<>();
		List<String> mails = new ArrayList<>();

		for (Map<String, String> map : mailList) {
			for (Map.Entry<String, String> entrySet : map.entrySet()) {
				switch (entrySet.getKey()) {
					case "senderUUID" -> senders.add(Bukkit.getOfflinePlayer(UUID.fromString(entrySet.getValue())).getName());
					case "time" -> times.add(simpleDateFormat.format(new Date(Long.parseLong(entrySet.getValue()))));
					case "mail" -> mails.add(entrySet.getValue());
					default -> Bukkit.getLogger().warning(NOENTRYEXCEPTION);
				}
			}
		}

		for (int i = 0; i < senders.size(); i++) {
			sender.sendMessage(Chat.replaceColors("&b" + senders.get(i) + " &f@ &d" + times.get(i) + "\n&f&l->&r" + mails.get(i)));
		}
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, OfflinePlayer player, @NotNull String perm, @NotNull String[] args, @NotNull StringBuilder mailBuilder) {

		if (Cmd.isInvalidSender(sender)) {
			return true;
		}

		if (Cmd.isNotPermitted(sender, perm)) {
			return true;
		}

		if (Cmd.isInvalidPlayer(sender, player)) {
			return true;
		}

		if (isInvalidMail(sender, args, mailBuilder)) {
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

	private static boolean isInvalidMail(@NotNull CommandSender sender, @NotNull String[] args, @NotNull StringBuilder mailBuilder) {

		for (String arg : args) {
			if (!arg.toLowerCase().matches("[a-z0-9.,!?;]{1,16}")) {
				Chat.sendMessage(sender, "invalid-word");
				return true;
			}
		}

		if (mailBuilder.length() > 64) {
			Chat.sendMessage(sender, "invalid-mail");
			return true;
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
