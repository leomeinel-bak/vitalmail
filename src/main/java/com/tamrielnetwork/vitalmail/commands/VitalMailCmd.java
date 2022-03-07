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

package com.tamrielnetwork.vitalmail.commands;

import com.tamrielnetwork.vitalmail.VitalMail;
import com.tamrielnetwork.vitalmail.utils.Chat;
import com.tamrielnetwork.vitalmail.utils.commands.Cmd;
import com.tamrielnetwork.vitalmail.utils.commands.CmdSpec;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class VitalMailCmd implements CommandExecutor {

	private final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (Cmd.isArgsLengthNotEqualTo(sender, args, 2)) {
			return true;
		}
		if (args[0].equals("send")) {
			doSend(sender, args);
			return true;
		}
		if (args[0].equals("read")) {
			doMail(sender, args);
			return true;
		}
		Chat.sendMessage(sender, "cmd");
		return true;

	}

	private void doMail(@NotNull CommandSender sender, @NotNull String[] args) {

		if (CmdSpec.isInvalidCmd(sender, "vitalmail.read")) {
			return;
		}
		Player senderPlayer = (Player) sender;
		Location location = main.getHomeStorage().loadMail(senderPlayer, arg.toLowerCase());
		if (CmdSpec.isInvalidLocation(sender, location)) {
			return;
		}

		senderPlayer.teleport(location);

	}

	private void doSend(@NotNull CommandSender sender, @NotNull String[] args) {

		@Deprecated OfflinePlayer mailReceiver = Bukkit.getOfflinePlayer(args[1]);

		if (CmdSpec.isInvalidCmd(sender, mailReceiver, "vitalmail.send")) {
			return;
		}

		StringBuilder mail = new StringBuilder();
		for (String arg : args) {
			if (arg.equals(args[0]) || arg.equals(args[1])) {
				continue;
			}
			if (arg.equals(args[2])) {
				mail.append(arg);
				continue;
			}
			mail.append(" ").append(arg);
		}

		Player mailSender = (Player) sender;
		String time = String.valueOf(System.currentTimeMillis());

		main.getHomeStorage().saveMail(mailSender, mailReceiver, time, mail.toString());

	}

	/*
	Mail send ->
				Mail gets stored in database of receiver with sender and message
				Receiver gets a message on next login
	 */

}