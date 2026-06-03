package xyz.vocoid.challenge.hardcore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ChallengeListener implements Listener {

    private final ChallengePlugin plugin = ChallengePlugin.getInstance();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String deadPlayerName = event.getEntity().getName();

        String deathPath = "deaths." + deadPlayerName;
        int currentDeaths = plugin.getConfig().getInt(deathPath, 0);
        plugin.getConfig().set(deathPath, currentDeaths + 1);
        plugin.saveConfig();
        plugin.reloadConfig();

        if (plugin.isScheduled()) return;
        plugin.scheduleRestart(deadPlayerName);
    }
}
