/*
 * VitalCraft is a Spigot Plugin that gives players the ability to open a crafting interface.
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
 * along with this program. If not, see https://github.com/TamrielNetwork/VitalCraft/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalcraft;

import com.tamrielnetwork.vitalcraft.commands.VitalCraftCmd;
import com.tamrielnetwork.vitalcraft.files.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VitalCraft extends JavaPlugin {

	private Messages messages;

	@Override
	public void onEnable() {

		Objects.requireNonNull(getCommand("vitalcraft")).setExecutor(new VitalCraftCmd());

		saveDefaultConfig();

		messages = new Messages();

		Bukkit.getLogger().info("VitalCraft v" + this.getDescription().getVersion() + " enabled");
		Bukkit.getLogger().info("Copyright (C) 2022 Leopold Meinel");
		Bukkit.getLogger().info("This program comes with ABSOLUTELY NO WARRANTY!");
		Bukkit.getLogger().info("This is free software, and you are welcome to redistribute it under certain conditions.");
		Bukkit.getLogger().info("See https://github.com/TamrielNetwork/VitalCraft/blob/main/LICENSE for more details.");
	}

	@Override
	public void onDisable() {

		Bukkit.getLogger().info("VitalCraft v" + this.getDescription().getVersion() + " disabled");
	}

	public Messages getMessages() {
		return messages;
	}

}

