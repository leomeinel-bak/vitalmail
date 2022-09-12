/*
 * File: VitalMailCmd.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.commands;

import dev.meinel.leo.vitalhome.VitalMail;
import dev.meinel.leo.vitalhome.utils.Chat;
import dev.meinel.leo.vitalhome.utils.commands.Cmd;
import dev.meinel.leo.vitalhome.utils.commands.CmdSpec;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VitalMailCmd
		implements TabExecutor {

	private final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		if (Cmd.isArgsLengthSmallerThan(sender, args, 1)) {
			return false;
		}
		switch (args[0].toLowerCase()) {
			case "send" -> doSend(sender, args);
			case "read" -> doRead(sender);
			case "clear" -> doClear(sender);
			default -> {
				Chat.sendMessage(sender, "cmd");
				return false;
			}
		}
		return true;
	}

	private void doSend(@NotNull CommandSender sender, @NotNull String[] args) {
		if (Cmd.isArgsLengthSmallerThan(sender, args, 3)) {
			return;
		}
		StringBuilder mailBuilder = new StringBuilder();
		@Deprecated
		OfflinePlayer receiverPlayer = Bukkit.getOfflinePlayer(args[1]);
		if (CmdSpec.isInvalidCmd(sender, receiverPlayer, "vitalmail.send", mailBuilder)) {
			return;
		}
		Player senderPlayer = (Player) sender;
		CmdSpec.sendMail(args, receiverPlayer, senderPlayer);
	}

	private void doRead(@NotNull CommandSender sender) {
		if (CmdSpec.isInvalidCmd(sender, "vitalmail.read")) {
			return;
		}
		Player senderPlayer = (Player) sender;
		String receiverUUID = senderPlayer.getUniqueId()
				.toString();
		List<Map<String, String>> mailList = main.getMailStorage()
				.loadMail(receiverUUID);
		CmdSpec.readMail(sender, receiverUUID, mailList);
	}

	private void doClear(@NotNull CommandSender sender) {
		if (CmdSpec.isInvalidCmd(sender, "vitalmail.clear")) {
			return;
		}
		Player senderPlayer = (Player) sender;
		String receiverUUID = senderPlayer.getUniqueId()
				.toString();
		main.getMailStorage()
				.clear(receiverUUID);
		Chat.sendMessage(sender, "mail-cleared");
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
			@NotNull String alias, @NotNull String[] args) {
		@Nullable
		List<String> tabComplete = new ArrayList<>();
		if (args.length == 1) {
			if (sender.hasPermission("vitalmail.send")) {
				tabComplete.add("send");
			}
			if (sender.hasPermission("vitalmail.read")) {
				tabComplete.add("read");
			}
			if (sender.hasPermission("vitalmail.clear")) {
				tabComplete.add("clear");
			}
		} else {
			return null;
		}
		return tabComplete;
	}
}
