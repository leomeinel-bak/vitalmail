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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings ("unchecked")
public class MailStorageYaml extends MailStorage {

	private static final String IOEXCEPTION = "VitalMail encountered an IOException while executing task";
	private static final String CLASSNOTFOUNDEXCEPTION = "VitalCondense encountered a ClassNotFoundException while executing task";
	private final File mailFolder;
	private File mailFile;

	public MailStorageYaml() {

		mailFolder = new File(main.getDataFolder(), "mail");
		if (!mailFolder.exists() && !mailFolder.mkdirs()) {
			Bukkit.getLogger().warning(IOEXCEPTION);
		}
	}

	@Override
	public List<Map<String, Map<String, String>>> loadMail(@NotNull String receiverUUID) {

		List<Map<String, Map<String, String>>> mail = new ArrayList<>();
		mailFile = new File(mailFolder, receiverUUID + ".yml");

		try (FileInputStream fileIn = new FileInputStream(mailFile); ObjectInputStream in = new ObjectInputStream(fileIn)) {
			mail = (List<Map<String, Map<String, String>>>) in.readObject();

		} catch (IOException | ClassNotFoundException ignored) {
			Bukkit.getLogger().warning(IOEXCEPTION);
			Bukkit.getLogger().warning(CLASSNOTFOUNDEXCEPTION);
		}
		return mail;
	}

	@Override
	public void saveMail(@NotNull OfflinePlayer receiverPlayer, @NotNull Player senderPlayer, String time, @NotNull String mail) {

		String receiverUUID = receiverPlayer.getUniqueId().toString();
		String senderUUID = senderPlayer.getUniqueId().toString();
		mailFile = new File(mailFolder, receiverUUID + ".yml");
/*
		if (loadMail(receiverUUID).size() >= CmdSpec.getAllowedMails(receiverPlayer, 1)) {
			Chat.sendMessage(senderPlayer, "max-mails");
			return;
		}

 */

		List<Map<String, Map<String, String>>> mailList = new ArrayList<>();
		Map<String, Map<String, String>> mailMap = new HashMap<>();

		mailMap.put(senderUUID, Map.of(time, mail));

		if (!loadMail(receiverUUID).isEmpty()) {
			mailList.addAll(loadMail(receiverUUID));
		}

		mailList.add(mailMap);

		try (FileOutputStream fileOut = new FileOutputStream(mailFile); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(mailList);
		} catch (IOException ignored) {
			Bukkit.getLogger().warning(IOEXCEPTION);
		}

		Chat.sendMessage(senderPlayer, "mail-sent");

	}

	@Override
	public void clear(@NotNull String receiverUUID) {

		mailFile = new File(mailFolder, receiverUUID + ".yml");

		try {
			Files.delete(mailFile.toPath());
		} catch (IOException ignored) {
			Bukkit.getLogger().warning(IOEXCEPTION);
		}
	}

}