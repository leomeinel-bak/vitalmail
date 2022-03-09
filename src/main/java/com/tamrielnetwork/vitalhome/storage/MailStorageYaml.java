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

package com.tamrielnetwork.vitalhome.storage;

import com.tamrielnetwork.vitalhome.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings ("unchecked")
public class MailStorageYaml extends MailStorage {

	private static final String IOEXCEPTION = "VitalMail encountered an IOException while executing task";
	private final File mailFolder;
	private File mailFile;
	private FileConfiguration mailConf;

	public MailStorageYaml() {

		mailFolder = new File(main.getDataFolder(), "mail");
		if (!mailFolder.exists() && !mailFolder.mkdirs()) {
			Bukkit.getLogger().warning(IOEXCEPTION);
		}
	}

	@Override
	public List<Map<String, String>> loadMail(@NotNull String receiverUUID) {

		mailFile = new File(mailFolder, receiverUUID + ".yml");
		mailConf = YamlConfiguration.loadConfiguration(mailFile);

		return (List<Map<String, String>>) mailConf.getList("mail");
	}

	@Override
	public void saveMail(@NotNull OfflinePlayer receiverPlayer, @NotNull Player senderPlayer, String time, @NotNull String mail) {

		List<Map<String, String>> mailList = new ArrayList<>();
		String receiverUUID = receiverPlayer.getUniqueId().toString();
		String senderUUID = senderPlayer.getUniqueId().toString();
		mailFile = new File(mailFolder, receiverUUID + ".yml");
		final String CREATEFILEEXCEPTION = "VitalMail is not able to create: mail/" + receiverUUID + ".yml";

		if (!mailFile.exists()) {
			try {
				if (!mailFile.createNewFile()) {
					Bukkit.getLogger().warning(CREATEFILEEXCEPTION);
				}
			} catch (IOException ignored) {
				Bukkit.getLogger().warning(IOEXCEPTION);
			}
		}

		mailConf = YamlConfiguration.loadConfiguration(mailFile);

		mailList.add(Map.of("senderUUID", senderUUID));
		mailList.add(Map.of("time", time));
		mailList.add(Map.of("mail", mail));

		if (mailFile.length() > 0) {
			mailList.addAll(loadMail(receiverUUID));
			if (loadMail(receiverUUID).size() >= 6 * 3) {
				Chat.sendMessage(senderPlayer, "inbox-full");
				return;
			}
		}

		mailConf.set("mail", mailList);
		Chat.sendMessage(senderPlayer, "mail-sent");

		save(mailFile, mailConf);

	}

	@Override
	public void clear(@NotNull String receiverUUID) {

		mailFile = new File(mailFolder, receiverUUID + ".yml");

		if (mailFile.exists()) {
			try {
				Files.delete(mailFile.toPath());
			} catch (IOException ignored) {
				Bukkit.getLogger().warning(IOEXCEPTION);
			}
		}
	}

	public void save(File mailFile, FileConfiguration mailConf) {

		try {
			mailConf.save(mailFile);
		} catch (IOException ignored) {
			Bukkit.getLogger().info(IOEXCEPTION);
		}
	}

}