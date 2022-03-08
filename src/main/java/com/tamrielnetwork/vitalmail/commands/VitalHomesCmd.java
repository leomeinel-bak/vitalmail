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

package com.tamrielnetwork.vitalmail.commands;

import com.tamrielnetwork.vitalmail.VitalMail;
import com.tamrielnetwork.vitalmail.utils.Chat;
import com.tamrielnetwork.vitalmail.utils.commands.Cmd;
import com.tamrielnetwork.vitalmail.utils.commands.CmdSpec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class VitalHomesCmd implements CommandExecutor {

	private final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (Cmd.isArgsLengthNotEqualTo(sender, args, 0)) {
			return true;
		}
		doHomes(sender);
		return true;

	}

	private void doHomes(@NotNull CommandSender sender) {

		if (CmdSpec.isInvalidCmd(sender, "vitalmail.list")) {
			return;
		}
		Player senderPlayer = (Player) sender;
		StringBuilder homesBuilder = new StringBuilder();
		Set<String> homesSet = main.getHomeStorage().listHome(senderPlayer);

		if (homesSet.isEmpty()) {
			Chat.sendMessage(sender, "no-homes");
			return;
		}

		List<String> homesList = main.getHomeStorage().listHome(senderPlayer).stream().toList();

		for (String home : homesList) {
			if (home.equals(homesList.get(0))) {
				homesBuilder.append("&b").append(home);
				continue;
			}
			homesBuilder.append("&f, &b").append(home);
		}
		String homes = homesBuilder.toString();

		sender.sendMessage(Chat.replaceColors(homes));

	}

}
