/*
 * VitalFly is a Spigot Plugin that gives players the ability to fly.
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
 * along with this program. If not, see https://github.com/TamrielNetwork/VitalFly/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalfly.listeners;

import com.tamrielnetwork.vitalfly.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangeWorld implements Listener {
	@EventHandler
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		if (!event.getPlayer().hasPermission("vitalfly.fly") || !event.getPlayer().hasPermission("vitalfly.fly.worldchange")) {
			return;
		}
		event.getPlayer().setAllowFlight(true);
		Utils.sendMessage(event.getPlayer(), "now-flying");

	}

}
