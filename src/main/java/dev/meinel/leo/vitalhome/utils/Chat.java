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
 * along with this program. If not, see https://github.com/LeoMeinel/VitalMail/blob/main/LICENSE
 */

package dev.meinel.leo.vitalhome.utils;

import dev.meinel.leo.vitalhome.VitalMail;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Chat {

	private static final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);

	private Chat() {
		throw new IllegalStateException("Utility class");
	}

	public static void sendMessage(@NotNull CommandSender player, @NotNull Map<String, String> placeholders,
	                               @NotNull String message) {
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

	public static void sendMessage(@NotNull CommandSender player, @NotNull String message) {
		player.sendMessage(replaceColors(Objects.requireNonNull(main.getMessages()
		                                                            .getMessagesConf()
		                                                            .getString(message))));
	}

	private static List<String> getMessages(@NotNull String message) {
		List<String> messages;
		if (main.getMessages()
		        .getMessagesConf()
		        .isList(message)) {
			messages = Objects.requireNonNull(main.getMessages()
			                                      .getMessagesConf()
			                                      .getStringList(message));
		}
		else {
			messages = new ArrayList<>();
			messages.add(main.getMessages()
			                 .getMessagesConf()
			                 .getString(message));
		}
		return messages;
	}

	public static String replaceColors(@NotNull String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
}