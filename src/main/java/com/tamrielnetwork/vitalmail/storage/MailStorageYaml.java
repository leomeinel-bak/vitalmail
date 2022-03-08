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

package com.tamrielnetwork.vitalmail.storage;

import com.tamrielnetwork.vitalmail.utils.Chat;
import com.tamrielnetwork.vitalmail.utils.commands.CmdSpec;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class MailStorageYaml extends MailStorage {

	private static final String IOEXCEPTION = "VitalMail encountered an IOException while executing task";
	private static final String HOME = "home.";
	private static final String WORLD = ".world";
	private final File homeFile;
	private final FileConfiguration homeConf;

	public MailStorageYaml() {

		homeFile = new File(main.getDataFolder(), "home.yml");
		homeConf = YamlConfiguration.loadConfiguration(homeFile);
		save();
	}

	@Override
	public Location loadHome(@NotNull Player player, @NotNull String arg) {

		String playerUUID = player.getUniqueId().toString();

		if (homeConf.getString(HOME + playerUUID + "." + arg + WORLD) == null) {
			return null;
		}
		World world = Bukkit.getWorld(Objects.requireNonNull(homeConf.getString(HOME + playerUUID + "." + arg + WORLD)));
		int x = homeConf.getInt(HOME + playerUUID + "." + arg + ".x");
		int y = homeConf.getInt(HOME + playerUUID + "." + arg + ".y");
		int z = homeConf.getInt(HOME + playerUUID + "." + arg + ".z");
		int yaw = homeConf.getInt(HOME + playerUUID + "." + arg + ".yaw");
		int pitch = homeConf.getInt(HOME + playerUUID + "." + arg + ".pitch");

		return new Location(world, x, y, z, yaw, pitch);
	}

	@Override
	public Set<String> listHome(@NotNull Player player) {

		String playerUUID = player.getUniqueId().toString();
		Set<String> homes;

		if (homeConf.getString(HOME + playerUUID) == null) {
			return Collections.emptySet();
		}
		homes = Objects.requireNonNull(homeConf.getConfigurationSection(HOME + playerUUID)).getKeys(false);

		return homes;
	}

	@Override
	public void saveHome(@NotNull Player player, @NotNull String arg) {

		String playerUUID = player.getUniqueId().toString();
		Location location = player.getLocation();

		if (homeConf.getConfigurationSection(HOME + playerUUID) != null) {
			@NotNull Set<String> keys = Objects.requireNonNull(homeConf.getConfigurationSection(HOME + playerUUID)).getKeys(false);

			if (keys.size() >= CmdSpec.getAllowedHomes(player, 1) && !keys.contains(arg)) {
				Chat.sendMessage(player, "max-homes");
				return;
			}
		}
		Chat.sendMessage(player, "home-set");

		clear(playerUUID, arg);

		homeConf.set(HOME + playerUUID + "." + arg + WORLD, location.getWorld().getName());
		homeConf.set(HOME + playerUUID + "." + arg + ".x", (int) location.getX());
		homeConf.set(HOME + playerUUID + "." + arg + ".y", (int) location.getY());
		homeConf.set(HOME + playerUUID + "." + arg + ".z", (int) location.getZ());
		homeConf.set(HOME + playerUUID + "." + arg + ".yaw", (int) location.getYaw());
		homeConf.set(HOME + playerUUID + "." + arg + ".pitch", (int) location.getPitch());

		save();
	}

	@Override
	public void clear(@NotNull String playerUUID, @NotNull String arg) {

		if (homeConf.getConfigurationSection(HOME + playerUUID) == null) {
			return;
		}
		for (String key : Objects.requireNonNull(homeConf.getConfigurationSection(HOME + playerUUID)).getKeys(false)) {
			if (Objects.equals(key, arg)) {
				homeConf.set(HOME + playerUUID + "." + key, null);
			}
		}
		save();
	}

	public void save() {

		try {
			homeConf.save(homeFile);
		} catch (IOException ignored) {
			Bukkit.getLogger().info(IOEXCEPTION);
		}
	}

}