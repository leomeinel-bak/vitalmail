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

import com.tamrielnetwork.vitalfly.VitalFly;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.GameMode.SURVIVAL;

public class PlayerGamemodeChange implements Listener {

	private final VitalFly main = JavaPlugin.getPlugin(VitalFly.class);

	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!event.getPlayer().hasPermission("vitalfly.fly") || !event.getPlayer().isOnline() || !event.getPlayer().hasPermission("vitalfly.fly.gamemodechange")) {
					return;
				}

				if (event.getNewGameMode() == SURVIVAL) {
					event.getPlayer().setAllowFlight(true);
					event.getPlayer().setFlying(true);
				}
			}
		}.runTaskLater(main, 1);
	}

}
