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

package com.tamrielnetwork.vitalmail.storage;

import com.tamrielnetwork.vitalmail.utils.Chat;
import com.tamrielnetwork.vitalmail.utils.commands.CmdSpec;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class MailStorageYaml extends MailStorage {

	private final File mailFile;
	private final FileConfiguration mailConf;

	public MailStorageYaml() {

		mailFile = new File(main.getDataFolder(), "mail.yml");
		mailConf = YamlConfiguration.loadConfiguration(mailFile);
		save();
	}

	@Override
	public String loadMail(@NotNull Player mailReceiver, @NotNull OfflinePlayer mailSender, @NotNull String time, @NotNull String mail) {

		String mailReceiverUUID = mailReceiver.getUniqueId().toString();
		String mailSenderUUID = mailSender.getUniqueId().toString();

		if (mailConf.getString("mail." + mailReceiverUUID + ".inbox") == null) {
			Bukkit.getLogger().severe("VitalMail cannot find mail in mail.yml");
			return null;
		}

		return mailConf.getString("mail." + mailReceiverUUID + ".inbox");
	}

	@Override
	public void saveMail(@NotNull Player mailSender, @NotNull OfflinePlayer mailReceiver, @NotNull String time, @NotNull String mail) {

		String mailSenderUUID = mailSender.getUniqueId().toString();
		String mailReceiverUUID = mailReceiver.getUniqueId().toString();

		if (mailConf.getConfigurationSection("mail." + mailSenderUUID + ".outbox") != null) {
			@NotNull Set<String> keys = Objects.requireNonNull(mailConf.getConfigurationSection("mail." + mailSenderUUID + ".outbox")).getKeys(false);

			if (keys.size() >= CmdSpec.getAllowedMails(mailSender, 2)) {
				Chat.sendMessage(mailSender, "max-mails");
				return;
			}
		}
		Chat.sendMessage(mailSender, "mail-sent");
		Chat.sendMessage(mailReceiver, "mail-received");

		clear(mailSenderUUID, mailReceiverUUID, mail, time);

		mailConf.set("mail." + mailSenderUUID + ".outbox" + time, mail);
		mailConf.set("mail." + mailReceiverUUID + ".inbox." + time, mail);

		save();
	}

	@Override
	public void clear(@NotNull String mailSenderUUID, @NotNull String mailReceiverUUID, @NotNull String time, @NotNull String mail) {

		String inbox = "mail." + mailReceiverUUID + ".inbox" + time;
		String outbox = "mail." + mailSenderUUID + ".outbox" + time;


		if (!Objects.equals(mailConf.getConfigurationSection(inbox), null) || !Objects.equals(mailConf.getConfigurationSection(outbox), null)) {
			return;
		}

		ConfigurationSection inboxSection = mailConf.getConfigurationSection(inbox);
		ConfigurationSection outboxSection = mailConf.getConfigurationSection(outbox);
		assert inboxSection != null;
		assert outboxSection != null;

		for (String key : inboxSection.getKeys(false)) {
			if (Objects.equals(key, inbox)) {
				mailConf.set(key, null);
			}
		}

		for (String key : outboxSection.getKeys(false)) {
			if (Objects.equals(key, outbox)) {
				mailConf.set(key, null);
			}
		}
	}

	public void save() {

		try {
			mailConf.save(mailFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}