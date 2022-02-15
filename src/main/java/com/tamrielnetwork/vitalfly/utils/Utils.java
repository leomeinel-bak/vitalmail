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

package com.tamrielnetwork.vitalfly.utils;

import com.tamrielnetwork.vitalfly.VitalFly;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Utils {

	private static final VitalFly main = JavaPlugin.getPlugin(VitalFly.class);

	public static void sendMessage(CommandSender player, Map<String, String> placeholders, String message) {
		List<String> messages = getMessages(message);
		for (String string : messages) {
			for (Map.Entry<String, String> entry : placeholders.entrySet()) {
				if (string.contains(entry.getKey())) {
					string = string.replace(entry.getKey(), entry.getValue());
				}
			}

			player.sendMessage(replaceColors(string));
		}
	}

	public static void sendMessage(CommandSender player, String message) {
		player.sendMessage(replaceColors(Objects.requireNonNull(main.getMessages().getMessagesConf().getString(message))));
	}

	private static List<String> getMessages(String message) {
		List<String> messages;
		if (main.getMessages().getMessagesConf().isList(message)) {
			messages = Objects.requireNonNull(main.getMessages().getMessagesConf().getStringList(message));
		} else {
			messages = new ArrayList<>();
			messages.add(main.getMessages().getMessagesConf().getString(message));
		}
		return messages;
	}

	public static String replaceColors(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

}