/*
 * File: Messages.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.files;

import dev.meinel.leo.vitalhome.VitalMail;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Messages {

    private final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);
    private final File messagesFile;
    private final FileConfiguration messagesConf;

    public Messages() {
        messagesFile = new File(main.getDataFolder(), "messages.yml");
        saveMessagesFile();
        messagesConf = YamlConfiguration.loadConfiguration(messagesFile);
    }

    private void saveMessagesFile() {
        if (!messagesFile.exists()) {
            main.saveResource("messages.yml", false);
        }
    }

    public FileConfiguration getMessagesConf() {
        return messagesConf;
    }
}
