/*
 * VitalFly is a Spigot Plugin that gives players the ability to fly.
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
 * along with this program. If not, see https://github.com/TamrielNetwork/VitalFly/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalfly.commands;

import com.google.common.collect.ImmutableMap;
import com.tamrielnetwork.vitalfly.VitalFly;
import com.tamrielnetwork.vitalfly.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VitalFlyCmd implements TabExecutor {

	private final VitalFly main = JavaPlugin.getPlugin(VitalFly.class);

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		// Check args length
		if (args.length == 0) {
			Utils.sendMessage(sender, "no-args");
			return true;
		}
		// Check arg 0
		switch (args[0]) {
			case "fly" -> toggleFly(sender, args);
			case "flyspeed" -> toggleFlySpeed(sender, args);
			default -> Utils.sendMessage(sender, "invalid-option");
		}

		return true;
	}

	private void toggleFly(CommandSender sender, String[] args) {
		// Check if command sender is a player
		if (!(sender instanceof Player)) {
			Utils.sendMessage(sender, "player-only");
			return;
		}
		// Check args length
		if (args.length > 2) {
			Utils.sendMessage(sender, "invalid-option");
			return;
		}
		//Check args length
		if (args.length == 1) {
			if (!sender.hasPermission("vitalfly.fly")) {
				Utils.sendMessage(sender, "no-perms");
				return;
			}
			if (((Player) sender).getAllowFlight()) {
				((Player) sender).setAllowFlight(false);
				((Player) sender).setFlying(false);
				Utils.sendMessage(sender, "now-flying-disabled");
				return;
			}
			((Player) sender).setAllowFlight(true);
			Utils.sendMessage(sender, "now-flying");
			return;
		}
		// Check args length
		if (args.length == 2) {
			if (!sender.hasPermission("vitalfly.fly.others")) {
				Utils.sendMessage(sender, "no-perms");
				return;
			}
			if (Bukkit.getPlayer(args[1]) == null) {
				Utils.sendMessage(sender, "invalid-player");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			boolean isOnline = Objects.requireNonNull(player).isOnline();
			if (!isOnline) {
				Utils.sendMessage(sender, "not-online");
				return;
			}
			if (player.getAllowFlight()) {
				player.setAllowFlight(false);
				player.setFlying(false);
				Utils.sendMessage(sender, ImmutableMap.of("%player%", player.getName()), "player-now-flying-disabled");
				Utils.sendMessage(player, "now-flying-disabled");
				return;
			}
			player.setAllowFlight(true);
			Utils.sendMessage(sender, ImmutableMap.of("%player%", player.getName()), "player-now-flying");
			Utils.sendMessage(player, "now-flying");
		}
	}

	private void toggleFlySpeed(CommandSender sender, String[] args) {
		// Check if command sender is a player
		if (!(sender instanceof Player)) {
			Utils.sendMessage(sender, "player-only");
			return;
		}
		// Check args length
		if (args.length > 3) {
			Utils.sendMessage(sender, "invalid-option");
			return;
		}
		//Check args length
		if (args.length == 2) {
			if (!sender.hasPermission("vitalfly.flyspeed")) {
				Utils.sendMessage(sender, "no-perms");
				return;
			}
			try {
				if (!(Math.abs(Float.parseFloat(args[1])) <= Math.abs((float) main.getConfig().getInt("flyspeed.limit")))) {
					Utils.sendMessage(sender, "beyond-limit");
					return;
				}
				((Player) sender).setFlySpeed((Math.abs(Float.parseFloat(args[1]))) / 10);
				Utils.sendMessage(sender, ImmutableMap.of("%flyspeed%", String.valueOf(Math.abs(Float.parseFloat(args[1])))), "flyspeed-changed");
				return;
			} catch (NumberFormatException numberFormatException) {
				Utils.sendMessage(sender, "invalid-amount");
				return;
			}
		}
		// Check args length
		if (args.length == 3) {
			if (!sender.hasPermission("vitalfly.flyspeed.others")) {
				Utils.sendMessage(sender, "no-perms");
				return;
			}
			if (Bukkit.getPlayer(args[1]) == null) {
				Utils.sendMessage(sender, "invalid-player");
				return;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if (!Objects.requireNonNull(player).isOnline()) {
				Utils.sendMessage(sender, "not-online");
				return;
			}
			try {
				if (!(Math.abs(Float.parseFloat(args[2])) <= Math.abs((float) main.getConfig().getInt("flyspeed.limit")))) {
					Utils.sendMessage(sender, "beyond-limit");
					return;
				}
				player.setFlySpeed((Math.abs(Float.parseFloat(args[2]))) / 10);
				Utils.sendMessage(sender, ImmutableMap.of(
								"%player%", player.getName(),
								"%flyspeed%", String.valueOf(Math.abs(Float.parseFloat(args[2])))),
						"player-flyspeed-changed");
				Utils.sendMessage(player, ImmutableMap.of("%flyspeed%", String.valueOf(Math.abs(Float.parseFloat(args[2])))), "flyspeed-changed");
			} catch (NumberFormatException numberFormatException) {
				Utils.sendMessage(sender, "invalid-amount");
			}
		}

	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		@Nullable List<String> tabComplete = new ArrayList<>();
		switch (args.length) {
			case 1 -> {
				if (sender.hasPermission("vitalfly.fly")) {
					tabComplete.add("fly");
				}
				if (sender.hasPermission("vitalfly.flyspeed")) {
					tabComplete.add("flyspeed");

				}
			}
			case 2, 3 -> {
				if (sender.hasPermission("vitalfly.flyspeed") && args[0].equals("flyspeed")) {
					tabComplete.addAll(List.of(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"}));
				}
				if (sender.hasPermission("vitalfly.fly") && args[0].equals("fly")) {
					tabComplete = null;
				}
			}
			default -> tabComplete = null;
		}
		return tabComplete;
	}
}