/*
 * File: MailStorageSql.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.storage;

import dev.meinel.leo.vitalhome.storage.mysql.SqlManager;
import dev.meinel.leo.vitalhome.utils.Chat;
import dev.meinel.leo.vitalhome.utils.sql.Sql;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MailStorageSql
		extends MailStorage {

	private static final String SQLEXCEPTION = "VitalMail encountered an SQLException while executing task";

	public MailStorageSql() {
		new SqlManager();
	}

	@Override
	public List<Map<String, String>> loadMail(@NotNull String receiverUUID) {
		List<Map<String, String>> mailList = new ArrayList<>();
		try (PreparedStatement selectStatement = SqlManager.getConnection()
				.prepareStatement(
						"SELECT * FROM " + Sql.getPrefix() + "Mail")) {
			try (ResultSet rs = selectStatement.executeQuery()) {
				while (rs.next()) {
					if (!Objects.equals(rs.getString(1), receiverUUID) || rs.getString(1) == null) {
						continue;
					}
					mailList.add(Map.of("senderUUID", rs.getString(2)));
					mailList.add(Map.of("time", rs.getString(3)));
					mailList.add(Map.of("mail", rs.getString(4)));
				}
			}
		} catch (SQLException ignored) {
			Bukkit.getLogger()
					.warning(SQLEXCEPTION);
			return Collections.emptyList();
		}
		return mailList;
	}

	@Override
	public void saveMail(@NotNull OfflinePlayer receiverPlayer, @NotNull Player senderPlayer, String time,
			@NotNull String mail) {
		String senderUUID = senderPlayer.getUniqueId()
				.toString();
		String receiverUUID = receiverPlayer.getUniqueId()
				.toString();
		if (loadMail(receiverUUID).size() >= 6 * 3) {
			Chat.sendMessage(senderPlayer, "inbox-full");
			return;
		}
		try (PreparedStatement insertStatement = SqlManager.getConnection()
				.prepareStatement("INSERT INTO " + Sql.getPrefix()
						+ "Mail (`ReceiverUUID`, `SenderUUID`, `Time`, `Mail`) VALUES (?, ?, ?, ?)")) {
			insertStatement.setString(1, receiverUUID);
			insertStatement.setString(2, senderUUID);
			insertStatement.setString(3, time);
			insertStatement.setString(4, mail);
			insertStatement.executeUpdate();
		} catch (SQLException ignored) {
			Bukkit.getLogger()
					.warning(SQLEXCEPTION);
		}
		Chat.sendMessage(senderPlayer, "mail-sent");
		if (receiverPlayer.isOnline()) {
			Chat.sendMessage(Objects.requireNonNull(receiverPlayer.getPlayer()),
					Map.of("%player%", senderPlayer.getName()), "mail-received");
		}
	}

	@Override
	public void clear(@NotNull String receiverUUID) {
		try (PreparedStatement deleteStatement = SqlManager.getConnection()
				.prepareStatement("DELETE FROM " + Sql.getPrefix()
						+ "Mail WHERE `ReceiverUUID`=?")) {
			deleteStatement.setString(1, receiverUUID);
			deleteStatement.executeUpdate();
		} catch (SQLException ignored) {
			Bukkit.getLogger()
					.warning(SQLEXCEPTION);
		}
	}
}
