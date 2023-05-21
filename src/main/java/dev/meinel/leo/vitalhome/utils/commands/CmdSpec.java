/*
 * File: CmdSpec.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalhome.utils.commands;

import dev.meinel.leo.vitalhome.VitalMail;
import dev.meinel.leo.vitalhome.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CmdSpec {

    private static final VitalMail main = JavaPlugin.getPlugin(VitalMail.class);
    private static final HashMap<UUID, Long> cooldownMap = new HashMap<>();

    private CmdSpec() {
        throw new IllegalStateException("Utility class");
    }

    public static void sendMail(@NotNull String @NotNull [] args, OfflinePlayer receiverPlayer,
            Player senderPlayer) {
        StringBuilder mailBuilder = new StringBuilder();
        for (String arg : args) {
            if (arg.equals(args[0]) || arg.equals(args[1])) {
                continue;
            }
            mailBuilder.append(" ").append(arg);
        }
        if (mailBuilder.length() > 64) {
            Chat.sendMessage(senderPlayer, "invalid-mail");
            return;
        }
        long time = System.currentTimeMillis();
        String timeString = Long.toString(time);
        String mail = mailBuilder.toString();
        main.getMailStorage().saveMail(receiverPlayer, senderPlayer, timeString, mail);
    }

    public static void readMail(@NotNull CommandSender sender, String receiverUUID,
            List<Map<String, String>> mailList) {
        if (mailList == null || mailList.isEmpty()) {
            Chat.sendMessage(sender, "no-mail");
            return;
        }
        final String NOENTRYEXCEPTION = "Invalid Entry found inside: mail/" + receiverUUID + ".yml";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss (MM/dd/yy)");
        List<String> senders = new ArrayList<>();
        List<String> times = new ArrayList<>();
        List<String> mails = new ArrayList<>();
        for (Map<String, String> map : mailList) {
            for (Map.Entry<String, String> entrySet : map.entrySet()) {
                switch (entrySet.getKey()) {
                    case "senderUUID" -> senders.add(Bukkit
                            .getOfflinePlayer(UUID.fromString(entrySet.getValue())).getName());
                    case "time" -> times.add(
                            simpleDateFormat.format(new Date(Long.parseLong(entrySet.getValue()))));
                    case "mail" -> mails.add(entrySet.getValue());
                    default -> Bukkit.getLogger().warning(NOENTRYEXCEPTION);
                }
            }
        }
        for (int i = 0; i < senders.size(); i++) {
            sender.sendMessage(Chat.replaceColors("&b" + senders.get(i) + " &f@ &d" + times.get(i)
                    + "\n&f&l->&r" + mails.get(i)));
        }
    }

    public static boolean isInvalidCmd(@NotNull CommandSender sender, OfflinePlayer player,
            @NotNull String perm, @NotNull StringBuilder mailBuilder) {
        return Cmd.isInvalidSender(sender) || !Cmd.isPermitted(sender, perm)
                || Cmd.isInvalidPlayer(sender, player) || isInvalidMail(sender, mailBuilder)
                || isOnCooldown(sender);
    }

    public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm) {
        return Cmd.isInvalidSender(sender) || !Cmd.isPermitted(sender, perm);
    }

    private static boolean isInvalidMail(@NotNull CommandSender sender,
            @NotNull StringBuilder mailBuilder) {
        if (mailBuilder.length() > 64) {
            Chat.sendMessage(sender, "invalid-mail");
            return true;
        }
        return false;
    }

    private static void clearMap(@NotNull CommandSender sender) {
        Player senderPlayer = (Player) sender;
        cooldownMap.remove(senderPlayer.getUniqueId());
    }

    private static void doTiming(@NotNull CommandSender sender) {
        new BukkitRunnable() {

            @Override
            public void run() {
                clearMap(sender);
            }
        }.runTaskLaterAsynchronously(main, (main.getConfig().getLong("cooldown.time") * 20L));
    }

    private static boolean isOnCooldown(@NotNull CommandSender sender) {
        Player senderPlayer = (Player) sender;
        boolean isOnCooldown = main.getConfig().getBoolean("cooldown.enabled")
                && !sender.hasPermission("vitalskull.cooldown.bypass")
                && cooldownMap.containsKey(senderPlayer.getUniqueId());
        if (isOnCooldown) {
            String timeRemaining = String.valueOf(cooldownMap.get(senderPlayer.getUniqueId())
                    - System.currentTimeMillis() / 1000);
            Chat.sendMessage(sender, Map.of("%time-left%", timeRemaining), "cooldown-active");
            return true;
        }
        cooldownMap.put(senderPlayer.getUniqueId(),
                main.getConfig().getLong("cooldown.time") + System.currentTimeMillis() / 1000);
        doTiming(sender);
        return false;
    }
}
