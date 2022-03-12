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
 * along with this program. If not, see https://github.com/TamrielNetwork/VitalMail/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalhome.storage.mysql;

import com.tamrielnetwork.vitalhome.VitalMail;
import com.tamrielnetwork.vitalhome.utils.sql.Sql;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlManager {

	private static final String SQLEXCEPTION = "VitalMail encountered an SQLException while executing task";
	private static Connection connection;
	private final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);
	private final int port;
	private final String host;
	private final String database;
	private final String username;
	private final String password;

	public SqlManager() {
		this.host = main.getConfig()
		                .getString("mysql.host");
		this.port = main.getConfig()
		                .getInt("mysql.port");
		this.database = main.getConfig()
		                    .getString("mysql.database");
		this.username = main.getConfig()
		                    .getString("mysql.username");
		this.password = main.getConfig()
		                    .getString("mysql.password");
		enableConnection();
		try (PreparedStatement statementSpawnTable = SqlManager.getConnection()
		                                                       .prepareStatement("CREATE TABLE IF NOT EXISTS " + Sql.getPrefix()
		                                                                         + "Mail (`ReceiverUUID` TEXT, `SenderUUID` TEXT, `Time` TEXT, `Mail` TEXT)")) {
			statementSpawnTable.executeUpdate();
		}
		catch (SQLException ignored) {
			Bukkit.getLogger()
			      .warning(SQLEXCEPTION);
		}
	}

	public static Connection getConnection() {
		return connection;
	}

	private static void setConnection(@NotNull Connection connection) {
		SqlManager.connection = connection;
	}

	private void enableConnection() {
		try {
			if (getConnection() != null && !getConnection().isClosed()) {
				return;
			}
			setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username,
			                                          password));
			main.getLogger()
			    .info("Connected successfully with the database!");
		}
		catch (SQLException ignored) {
			Bukkit.getLogger()
			      .warning(SQLEXCEPTION);
		}
	}
}