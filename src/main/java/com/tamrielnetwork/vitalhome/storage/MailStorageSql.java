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

import com.tamrielnetwork.vitalhome.storage.mysql.SqlManager;
import com.tamrielnetwork.vitalhome.utils.Chat;
import com.tamrielnetwork.vitalhome.utils.sql.Sql;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MailStorageSql extends MailStorage {

	private static final String SQLEXCEPTION = "VitalMail encountered an SQLException while executing task";

	public MailStorageSql() {

		new SqlManager();
	}

	@Override
	public List<Map<String, Map<String, String>>> loadMail(@NotNull String receiverUUID) {

		List<Map<String, Map<String, String>>> mailList = new ArrayList<>();
		Map<String, Map<String, String>> mailMap = new HashMap<>();

		try (PreparedStatement selectStatement = SqlManager.getConnection().prepareStatement("SELECT * FROM " + Sql.getPrefix() + "Mail")) {
			try (ResultSet rs = selectStatement.executeQuery()) {
				while (rs.next()) {
					if (!Objects.equals(rs.getString(1), receiverUUID) || rs.getString(1) == null) {
						continue;
					}
					mailMap.put(rs.getString(4), Map.of(rs.getString(3), rs.getString(2)));
				}
			}
		} catch (SQLException ignored) {
			Bukkit.getLogger().warning(SQLEXCEPTION);
			return Collections.emptyList();
		}
		mailList.add(mailMap);
		return mailList;
	}

	@Override
	public void saveMail(@NotNull OfflinePlayer receiverPlayer, @NotNull Player senderPlayer, String time, @NotNull String mail) {

		String senderUUID = senderPlayer.getUniqueId().toString();
		String receiverUUID = receiverPlayer.getUniqueId().toString();
/*		int mails = 0;

		try (PreparedStatement selectStatement = SqlManager.getConnection().prepareStatement("SELECT COUNT(*) FROM " + Sql.getPrefix() + "Mail WHERE `ReceiverUUID`=" + "'" + receiverUUID + "'")) {
			try (ResultSet rs = selectStatement.executeQuery()) {
				rs.next();
				mails = rs.getInt(1);
			}
		} catch (SQLException ignored) {
			Bukkit.getLogger().warning(SQLEXCEPTION);
		}

		if (mails >= CmdSpec.getAllowedMails(receiverPlayer, 1)) {
			Chat.sendMessage(senderPlayer, "max-mails");
			return;
		}

 */
		Chat.sendMessage(senderPlayer, "mail-sent");

		try (PreparedStatement insertStatement = SqlManager.getConnection().prepareStatement("INSERT INTO " + Sql.getPrefix() + "Mail (`ReceiverUUID`, `SenderUUID`, `Time`, `Mail`) VALUES (?, ?, ?, ?)")) {
			insertStatement.setString(1, receiverUUID);
			insertStatement.setString(2, senderUUID);
			insertStatement.setString(3, time);
			insertStatement.setString(4, mail);
			insertStatement.executeUpdate();
		} catch (SQLException ignored) {
			Bukkit.getLogger().warning(SQLEXCEPTION);
		}
	}

	@Override
	public void clear(@NotNull String receiverUUID) {

		try (PreparedStatement deleteStatement = SqlManager.getConnection().prepareStatement("DELETE FROM " + Sql.getPrefix() + "Mail WHERE `ReceiverUUID`=" + "'" + receiverUUID + "'")) {
			deleteStatement.executeUpdate();
		} catch (SQLException ignored) {
			Bukkit.getLogger().warning(SQLEXCEPTION);
		}
	}

}
