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

import com.tamrielnetwork.vitalmail.storage.mysql.SqlManager;
import com.tamrielnetwork.vitalmail.utils.Chat;
import com.tamrielnetwork.vitalmail.utils.commands.CmdSpec;
import com.tamrielnetwork.vitalmail.utils.sql.Sql;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MailStorageSql extends MailStorage {

	private static final String SQLEXCEPTION = "VitalMail encountered an SQLException while executing task";

	public MailStorageSql() {

		new SqlManager();
	}

	@Override
	public Location loadHome(@NotNull Player player, @NotNull String arg) {

		String playerUUID = player.getUniqueId().toString();

		World world = null;
		int x = 0;
		int y = 0;
		int z = 0;
		int yaw = 0;
		int pitch = 0;

		try (PreparedStatement selectStatement = SqlManager.getConnection().prepareStatement("SELECT * FROM " + Sql.getPrefix() + "Home")) {
			try (ResultSet rs = selectStatement.executeQuery()) {
				while (rs.next()) {
					if (!Objects.equals(rs.getString(1), playerUUID) || rs.getString(1) == null || !Objects.equals(rs.getString(2), arg) || rs.getString(3) == null) {
						continue;
					}
					world = Bukkit.getWorld(Objects.requireNonNull(rs.getString(3)));
					x = rs.getInt(4);
					y = rs.getInt(5);
					z = rs.getInt(6);
					yaw = rs.getInt(7);
					pitch = rs.getInt(8);
				}
			}
		} catch (SQLException ignored) {
			Bukkit.getLogger().warning(SQLEXCEPTION);
			return null;
		}
		return new Location(world, x, y, z, yaw, pitch);
	}

	@Override
	public Set<String> listHome(@NotNull Player player) {

		String playerUUID = player.getUniqueId().toString();
		Set<String> homes = new HashSet<>();

		try (PreparedStatement selectStatement = SqlManager.getConnection().prepareStatement("SELECT * FROM " + Sql.getPrefix() + "Home")) {
			try (ResultSet rs = selectStatement.executeQuery()) {
				while (rs.next()) {
					if (!Objects.equals(rs.getString(1), playerUUID) || rs.getString(1) == null) {
						continue;
					}

					homes.add(rs.getString(2));
				}
			}
		} catch (SQLException ignored) {
			Bukkit.getLogger().warning(SQLEXCEPTION);
			return Collections.emptySet();
		}
		return homes;
	}

	@Override
	public void saveHome(@NotNull Player player, @NotNull String arg) {

		String playerUUID = player.getUniqueId().toString();
		Location location = player.getLocation();
		int homes = 0;

		try (PreparedStatement selectStatement = SqlManager.getConnection().prepareStatement("SELECT COUNT(*) FROM " + Sql.getPrefix() + "Home WHERE `UUID`=" + "'" + playerUUID + "'")) {
			try (ResultSet rs = selectStatement.executeQuery()) {
				rs.next();
				homes = rs.getInt(1);
			}
		} catch (SQLException ignored) {
			Bukkit.getLogger().warning(SQLEXCEPTION);
		}

		if (homes >= CmdSpec.getAllowedHomes(player, 1)) {
			Chat.sendMessage(player, "max-homes");
			return;
		}
		Chat.sendMessage(player, "home-set");

		clear(playerUUID, arg);

		try (PreparedStatement insertStatement = SqlManager.getConnection().prepareStatement("INSERT INTO " + Sql.getPrefix() + "Home (`UUID`, `Home`, `World`, `X`, `Y`, `Z`, `Yaw`, `Pitch`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
			insertStatement.setString(1, playerUUID);
			insertStatement.setString(2, arg);
			insertStatement.setString(3, location.getWorld().getName());
			insertStatement.setInt(4, (int) location.getX());
			insertStatement.setInt(5, (int) location.getY());
			insertStatement.setInt(6, (int) location.getZ());
			insertStatement.setInt(7, (int) location.getYaw());
			insertStatement.setInt(8, (int) location.getPitch());
			insertStatement.executeUpdate();
		} catch (SQLException ignored) {
			Bukkit.getLogger().warning(SQLEXCEPTION);
		}
	}

	@Override
	public void clear(@NotNull String playerUUID, @NotNull String arg) {

		try (PreparedStatement deleteStatement = SqlManager.getConnection().prepareStatement("DELETE FROM " + Sql.getPrefix() + "Home WHERE `UUID`=" + "'" + playerUUID + "' AND `Home`=" + "'" + arg + "'")) {
			deleteStatement.executeUpdate();
		} catch (SQLException ignored) {
			Bukkit.getLogger().warning(SQLEXCEPTION);
		}
	}

}
