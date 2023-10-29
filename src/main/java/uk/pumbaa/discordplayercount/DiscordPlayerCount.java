package uk.pumbaa.discordplayercount;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public final class DiscordPlayerCount extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        DiscordPlayerCount plugin = this;

        saveDefaultConfig();

        String discordbottoken = this.getConfig().getString("discordbottoken");

        String discordbotstatustype = this.getConfig().getString("discordbotstatustype");

        String discordbotstatusonline = this.getConfig().getString("discordbotstatusonline");

        String discordbotstatus = this.getConfig().getString("discordbotstatus");

        JDA jda = JDABuilder.createDefault(discordbottoken).build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get all online players

        Collection<Player> onlinePlayers = (Collection<Player>) Bukkit.getServer().getOnlinePlayers();

        // Get the number of online players
        int onlinePlayerCount = onlinePlayers.size();

        // Set the status message
        discordbotstatus = discordbotstatus.replace("%onlineplayercount%", String.valueOf(onlinePlayerCount));
        discordbotstatus = discordbotstatus.replace("%maxplayercount%", String.valueOf(Bukkit.getServer().getMaxPlayers()));


        if (onlinePlayerCount == 1){
            discordbotstatus = String.valueOf(onlinePlayerCount) + " Player";
        }else{
            discordbotstatus = String.valueOf(onlinePlayerCount) + " Players";
        }

        OnlineStatus onlineStatus;

        if (discordbotstatusonline.equalsIgnoreCase("ONLINE")) {
            onlineStatus = OnlineStatus.ONLINE;
        } else if (discordbotstatusonline.equalsIgnoreCase("IDLE")) {
            onlineStatus = OnlineStatus.IDLE;
        } else if (discordbotstatusonline.equalsIgnoreCase("DO_NOT_DISTURB")) {
            onlineStatus = OnlineStatus.DO_NOT_DISTURB;
        } else if (discordbotstatusonline.equalsIgnoreCase("INVISIBLE")) {
            onlineStatus = OnlineStatus.INVISIBLE;
        } else {
            onlineStatus = OnlineStatus.ONLINE;
        }

        Presence presence = jda.getPresence();

        if (discordbotstatustype.equalsIgnoreCase("PLAYING")) {
            presence.setPresence(onlineStatus, Activity.playing(discordbotstatus), true);
        } else if (discordbotstatustype.equalsIgnoreCase("WATCHING")) {
            presence.setPresence(onlineStatus, Activity.watching(discordbotstatus), true);
        } else if (discordbotstatustype.equalsIgnoreCase("LISTENING")) {
            presence.setPresence(onlineStatus, Activity.listening(discordbotstatus), true);
        } else if (discordbotstatustype.equalsIgnoreCase("COMPETING")) {
            presence.setPresence(onlineStatus, Activity.competing(discordbotstatus), true);
        } else if (discordbotstatustype.equalsIgnoreCase("STREAMING")) {
            presence.setPresence(onlineStatus, Activity.streaming(discordbotstatus, "https://www.twitch.tv/"), true);
        } else if (discordbotstatustype.equalsIgnoreCase("DEFAULT")) {
            presence.setPresence(onlineStatus, Activity.watching(discordbotstatus), true);
        } else {
            presence.setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.watching(discordbotstatus), true);
        }

        String finalDiscordbotstatus = discordbotstatus;
        new BukkitRunnable() {

            @Override
            public void run() {
                if (discordbotstatustype.equalsIgnoreCase("PLAYING")) {
                    presence.setPresence(onlineStatus, Activity.playing(finalDiscordbotstatus), true);
                } else if (discordbotstatustype.equalsIgnoreCase("WATCHING")) {
                    presence.setPresence(onlineStatus, Activity.watching(finalDiscordbotstatus), true);
                } else if (discordbotstatustype.equalsIgnoreCase("LISTENING")) {
                    presence.setPresence(onlineStatus, Activity.listening(finalDiscordbotstatus), true);
                } else if (discordbotstatustype.equalsIgnoreCase("COMPETING")) {
                    presence.setPresence(onlineStatus, Activity.competing(finalDiscordbotstatus), true);
                } else if (discordbotstatustype.equalsIgnoreCase("STREAMING")) {
                    presence.setPresence(onlineStatus, Activity.streaming(finalDiscordbotstatus, "https://www.twitch.tv/"), true);
                } else if (discordbotstatustype.equalsIgnoreCase("DEFAULT")) {
                    presence.setPresence(onlineStatus, Activity.watching(finalDiscordbotstatus), true);
                } else {
                    presence.setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.watching(finalDiscordbotstatus), true);
                }
            }
        }.runTaskTimerAsynchronously(this, 0, 60 * 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
