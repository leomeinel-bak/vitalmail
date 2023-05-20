/*
 * File: VitalMail.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome;

import dev.meinel.leo.vitalhome.commands.VitalMailCmd;
import dev.meinel.leo.vitalhome.files.Messages;
import dev.meinel.leo.vitalhome.listeners.PlayerJoin;
import dev.meinel.leo.vitalhome.storage.MailStorage;
import dev.meinel.leo.vitalhome.storage.MailStorageSql;
import dev.meinel.leo.vitalhome.storage.MailStorageYaml;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VitalMail extends JavaPlugin {

    private MailStorage mailStorage;
    private Messages messages;

    @Override
    public void onEnable() {
        registerListeners();
        registerCommands();
        saveDefaultConfig();
        setupStorage();
        messages = new Messages();
        Bukkit.getLogger().info("VitalMail v" + this.getPluginMeta().getVersion() + " enabled");
        Bukkit.getLogger().info("Copyright (C) 2022 Leopold Meinel");
        Bukkit.getLogger().info("This program comes with ABSOLUTELY NO WARRANTY!");
        Bukkit.getLogger().info(
                "This is free software, and you are welcome to redistribute it under certain conditions.");
        Bukkit.getLogger()
                .info("See https://www.gnu.org/licenses/gpl-3.0-standalone.html for more details.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("VitalMail v" + this.getPluginMeta().getVersion() + " disabled");
    }

    private void setupStorage() {
        String storageSystem = getConfig().getString("storage-system");
        if (Objects.requireNonNull(storageSystem).equalsIgnoreCase("mysql")) {
            this.mailStorage = new MailStorageSql();
        } else {
            this.mailStorage = new MailStorageYaml();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("mail")).setExecutor(new VitalMailCmd());
        Objects.requireNonNull(getCommand("mail")).setTabCompleter(new VitalMailCmd());
    }

    public Messages getMessages() {
        return messages;
    }

    public MailStorage getMailStorage() {
        return mailStorage;
    }
}
