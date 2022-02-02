package com.loafofbread.toasterapi.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public abstract class CustomEvent {

    public final String id;
    public final String name;
    public final int length;

    protected final JavaPlugin plugin;

    public CustomEvent(JavaPlugin plugin, String id, String name, int length) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.plugin = plugin;
        displayBar = Bukkit.createBossBar(name, BarColor.YELLOW, BarStyle.SOLID);
        delay = getDelay();
        new DelayTask().runTaskTimer(plugin, 0, 20);
    }

    public static int delay = 0;
    protected abstract int getDelay();
    protected int timeLeft;

    protected abstract void startEvent();
    protected abstract void endEvent();
    protected abstract String getTutorialMessage(int message);

    private static boolean active = false;
    public static boolean isActive() { return active; }

    private BossBar displayBar;

    private void updateDisplayBar() {
        int totalSecs = timeLeft / 20;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        displayBar.setTitle(name + " (" + minutes + "m, " + seconds + "s)");
        double progress = timeLeft / length;
        displayBar.setProgress(progress);
        syncPlayers();
    }
    private void syncPlayers() {
        Collection<? extends Player> playersUnadded = Bukkit.getOnlinePlayers();
        if (!playersUnadded.isEmpty()) {
            for (Player p : playersUnadded) {
                if(!displayBar.getPlayers().contains(p))
                    displayBar.addPlayer(p);
            }
        }
    }

    private class IntroCutscene extends BukkitRunnable {
        private int messageID = 0;
        @Override
        public void run() {
            if(messageID == 0) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatColor.AQUA + name + ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD + " is about to start.");
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Float.MAX_VALUE, 1);
                }
            } else {
                String message = getTutorialMessage(messageID);
                if(message == null) {
                    active = true;
                    startEvent();
                    cancel();
                    timeLeft = length;
                    new EventTask().runTaskTimer(plugin, 0, 20);
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(message);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Float.MAX_VALUE, 1);
                }
            }
            messageID++;
        }
    }
    private class EventTask extends BukkitRunnable {
        @Override
        public void run() {
            if(timeLeft <= 0) {
                cancel();
                endEvent();
                delay = getDelay();
                displayBar.removeAll();
                active = false;
                new DelayTask().runTaskTimer(plugin, 0, 20);
                return;
            }
            updateDisplayBar();
            timeLeft--;
        }
    }
    private class DelayTask extends BukkitRunnable {
        @Override
        public void run() {
            if(delay <= 0) {
                cancel();
                new IntroCutscene().runTaskTimer(plugin, 0, 20);
                return;
            }
            delay--;
        }
    }
}
