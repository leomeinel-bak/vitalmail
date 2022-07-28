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

package com.tamrielnetwork.vitalhome;

import com.tamrielnetwork.vitalhome.commands.VitalMailCmd;
import com.tamrielnetwork.vitalhome.files.Messages;
import com.tamrielnetwork.vitalhome.listeners.PlayerJoin;
import com.tamrielnetwork.vitalhome.storage.MailStorage;
import com.tamrielnetwork.vitalhome.storage.MailStorageSql;
import com.tamrielnetwork.vitalhome.storage.MailStorageYaml;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VitalMail
		extends JavaPlugin {

	private MailStorage mailStorage;
	private Messages messages;

	@Override
	public void onEnable() {
		registerListeners();
		registerCommands();
		saveDefaultConfig();
		setupStorage();
		messages = new Messages();
		Bukkit.getLogger()
		      .info("VitalMail v" + this.getDescription()
		                                .getVersion() + " enabled");
		Bukkit.getLogger()
		      .info("Copyright (C) 2022 Leopold Meinel");
		Bukkit.getLogger()
		      .info("This program comes with ABSOLUTELY NO WARRANTY!");
		Bukkit.getLogger()
		      .info("This is free software, and you are welcome to redistribute it under certain conditions.");
		Bukkit.getLogger()
		      .info("See https://github.com/LeoMeinel/VitalHome/blob/main/LICENSE for more details.");
	}

	@Override
	public void onDisable() {
		Bukkit.getLogger()
		      .info("VitalMail v" + this.getDescription()
		                                .getVersion() + " disabled");
	}

	private void setupStorage() {
		String storageSystem = getConfig().getString("storage-system");
		if (Objects.requireNonNull(storageSystem)
		           .equalsIgnoreCase("mysql")) {
			this.mailStorage = new MailStorageSql();
		}
		else {
			this.mailStorage = new MailStorageYaml();
		}
	}

	private void registerListeners() {
		getServer().getPluginManager()
		           .registerEvents(new PlayerJoin(), this);
	}

	private void registerCommands() {
		Objects.requireNonNull(getCommand("mail"))
		       .setExecutor(new VitalMailCmd());
		Objects.requireNonNull(getCommand("mail"))
		       .setTabCompleter(new VitalMailCmd());
	}

	public Messages getMessages() {
		return messages;
	}

	public MailStorage getMailStorage() {
		return mailStorage;
	}
}

