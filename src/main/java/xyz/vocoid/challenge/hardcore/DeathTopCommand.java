package xyz.vocoid.challenge.hardcore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DeathTopCommand implements CommandExecutor {

    private final ChallengePlugin plugin = ChallengePlugin.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("deathtop")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!ChallengePlugin.getInstance().getConfig().contains("deaths")) {
            player.sendMessage(ChatColor.RED + "No death records found yet. Maybe try killing yourself?");
            return true;
        }

        Map<String, Integer> deathCounts = new LinkedHashMap<>();
        for (String playerName : ChallengePlugin.getInstance().getConfig().getConfigurationSection("deaths").getKeys(false)) {
            int deaths = plugin.getConfig().getInt("deaths." + playerName, 0);
            deathCounts.put(playerName, deaths);
        }

        if (deathCounts.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No death records found yet. Maybe try killing yourself?");
            return true;
        }

        LinkedHashMap<String, Integer> sortedDeaths = deathCounts.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        int totalDeaths = sortedDeaths.values().stream().mapToInt(Integer::intValue).sum();

        player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Most Deaths Leaderboard " + ChatColor.GRAY + "(" + totalDeaths + " total deaths)");
        int i = 1;
        for (Map.Entry<String, Integer> entry : sortedDeaths.entrySet()) {
            ChatColor color = i == 1 ? ChatColor.YELLOW : i == 2 ? ChatColor.GREEN : i == 3 ? ChatColor.BLUE : ChatColor.GRAY;
            player.sendMessage(color.toString() + i + ". " + entry.getKey() + " - " + ChatColor.WHITE + entry.getValue() + " deaths");
            i++;
        }

        return true;
    }
}

