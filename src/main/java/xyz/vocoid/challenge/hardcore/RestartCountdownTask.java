package xyz.vocoid.challenge.hardcore;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class RestartCountdownTask extends BukkitRunnable {

    private final String playerName;

    private int countdown = 10;

    @Override
    public void run() {
        if (countdown == 10) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(p, Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);

                p.sendTitle(
                        ChatColor.DARK_RED + playerName + ChatColor.RED + " has died.",
                        ChatColor.YELLOW + "Restarting in " + countdown + "..."
                        , 10, 100, 0);
            }
        }

        if (countdown <= 5 && countdown != 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.5f);

                p.sendTitle(
                        ChatColor.DARK_RED + playerName + ChatColor.RED + " has died.",
                        ChatColor.YELLOW + "Restarting in " + countdown + "..."
                        , 0, 20, 0);
            }
        }

        if (countdown == 0) {
            this.cancel();
            Bukkit.broadcastMessage(ChatColor.RED + "Server shutting down..");
            Bukkit.shutdown();
        }

        countdown--;
    }
}
