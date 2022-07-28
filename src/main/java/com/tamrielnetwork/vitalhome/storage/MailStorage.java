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

package com.tamrielnetwork.vitalhome.storage;

import com.tamrielnetwork.vitalhome.VitalMail;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public abstract class MailStorage {

	protected final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);

	public abstract List<Map<String, String>> loadMail(@NotNull String receiverUUID);

	public abstract void saveMail(@NotNull OfflinePlayer receiverPlayer, @NotNull Player senderPlayer, String time,
	                              @NotNull String mail);

	public abstract void clear(@NotNull String receiverUUID);

	public boolean hasMail(@NotNull String receiverUUID) {
		return loadMail(receiverUUID) != null && !loadMail(receiverUUID).isEmpty();
	}
}