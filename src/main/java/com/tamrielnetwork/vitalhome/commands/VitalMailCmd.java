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

package com.tamrielnetwork.vitalhome.commands;

import com.tamrielnetwork.vitalhome.VitalMail;
import com.tamrielnetwork.vitalhome.utils.Chat;
import com.tamrielnetwork.vitalhome.utils.commands.Cmd;
import com.tamrielnetwork.vitalhome.utils.commands.CmdSpec;
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

public class VitalMailCmd implements TabExecutor {

	private final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

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
		@Deprecated OfflinePlayer receiverPlayer = Bukkit.getOfflinePlayer(args[1]);

		if (CmdSpec.isInvalidCmd(sender, receiverPlayer, "vitalmail.send", args, mailBuilder)) {
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
		String receiverUUID = senderPlayer.getUniqueId().toString();
		List<Map<String, String>> mailList = main.getMailStorage().loadMail(receiverUUID);

		CmdSpec.readMail(sender, receiverUUID, mailList);

	}

	private void doClear(@NotNull CommandSender sender) {

		if (CmdSpec.isInvalidCmd(sender, "vitalmail.clear")) {
			return;
		}
		Player senderPlayer = (Player) sender;
		String receiverUUID = senderPlayer.getUniqueId().toString();

		main.getMailStorage().clear(receiverUUID);
		Chat.sendMessage(sender, "mail-cleared");

	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

		@Nullable List<String> tabComplete = new ArrayList<>();

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
