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

package com.tamrielnetwork.vitalhome.listeners;

import com.tamrielnetwork.vitalhome.VitalMail;
import com.tamrielnetwork.vitalhome.utils.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerJoin implements Listener {

	private final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);

	@EventHandler
	public void onPlayerJoin(@NotNull PlayerJoinEvent event) {

		Player player = event.getPlayer();
		String receiverUUID = player.getUniqueId().toString();

		if(main.getMailStorage().hasMail(receiverUUID)) {
			Chat.sendMessage(player, "new-mail");
		}

	}

}
