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
import com.tamrielnetwork.vitalmail.utils.commands.Cmd;
import com.tamrielnetwork.vitalmail.utils.commands.CmdSpec;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VitalHomeCmd implements TabExecutor {

	private final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (Cmd.isArgsLengthNotEqualTo(sender, args, 1)) {
			return true;
		}
		doHome(sender, args[0]);
		return true;

	}

	private void doHome(@NotNull CommandSender sender, String arg) {

		if (CmdSpec.isInvalidCmd(sender, "vitalmail.home", arg)) {
			return;
		}
		Player senderPlayer = (Player) sender;
		Location location = main.getHomeStorage().loadHome(senderPlayer, arg.toLowerCase());

		if (CmdSpec.isInvalidLocation(location)) {
			return;
		}

		CmdSpec.doDelay(sender, location);

	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

		Player senderPlayer = (Player) sender;

		if (main.getHomeStorage().listHome(senderPlayer).isEmpty()) {
			return null;
		}
		return new ArrayList<>(main.getHomeStorage().listHome(senderPlayer));
	}

}