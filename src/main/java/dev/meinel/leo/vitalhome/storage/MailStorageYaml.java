/*
 * File: MailStorageYaml.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.storage;

import dev.meinel.leo.vitalhome.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class MailStorageYaml extends MailStorage {

    private static final String IOEXCEPTION =
            "VitalMail encountered an IOException while executing task";
    private final File mailFolder;
    private File mailFile;
    private FileConfiguration mailConf;

    public MailStorageYaml() {
        mailFolder = new File(main.getDataFolder(), "mail");
        if (!mailFolder.exists() && !mailFolder.mkdirs()) {
            Bukkit.getLogger().warning(IOEXCEPTION);
        }
    }

    @Override
    public List<Map<String, String>> loadMail(@NotNull String receiverUUID) {
        mailFile = new File(mailFolder, receiverUUID + ".yml");
        mailConf = YamlConfiguration.loadConfiguration(mailFile);
        return (List<Map<String, String>>) mailConf.getList("mail");
    }

    @Override
    public void saveMail(@NotNull OfflinePlayer receiverPlayer, @NotNull Player senderPlayer,
            String time, @NotNull String mail) {
        List<Map<String, String>> mailList = new ArrayList<>();
        String receiverUUID = receiverPlayer.getUniqueId().toString();
        String senderUUID = senderPlayer.getUniqueId().toString();
        mailFile = new File(mailFolder, receiverUUID + ".yml");
        final String CREATEFILEEXCEPTION =
                "VitalMail is not able to create: mail/" + receiverUUID + ".yml";
        if (!mailFile.exists()) {
            try {
                if (!mailFile.createNewFile()) {
                    Bukkit.getLogger().warning(CREATEFILEEXCEPTION);
                }
            } catch (IOException ignored) {
                Bukkit.getLogger().warning(IOEXCEPTION);
            }
        }
        mailConf = YamlConfiguration.loadConfiguration(mailFile);
        mailList.add(Map.of("senderUUID", senderUUID));
        mailList.add(Map.of("time", time));
        mailList.add(Map.of("mail", mail));
        if (mailFile.length() > 0) {
            mailList.addAll(loadMail(receiverUUID));
            if (loadMail(receiverUUID).size() >= 6 * 3) {
                Chat.sendMessage(senderPlayer, "inbox-full");
                return;
            }
        }
        mailConf.set("mail", mailList);
        Chat.sendMessage(senderPlayer, "mail-sent");
        save(mailFile, mailConf);
        if (receiverPlayer.isOnline()) {
            Chat.sendMessage(Objects.requireNonNull(receiverPlayer.getPlayer()),
                    Map.of("%player%", senderPlayer.getName()), "mail-received");
        }
    }

    @Override
    public void clear(@NotNull String receiverUUID) {
        mailFile = new File(mailFolder, receiverUUID + ".yml");
        if (mailFile.exists()) {
            try {
                Files.delete(mailFile.toPath());
            } catch (IOException ignored) {
                Bukkit.getLogger().warning(IOEXCEPTION);
            }
        }
    }

    private void save(File mailFile, FileConfiguration mailConf) {
        try {
            mailConf.save(mailFile);
        } catch (IOException ignored) {
            Bukkit.getLogger().info(IOEXCEPTION);
        }
    }
}
