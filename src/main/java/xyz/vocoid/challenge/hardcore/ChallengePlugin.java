package xyz.vocoid.challenge.hardcore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class ChallengePlugin extends JavaPlugin {

    @Getter private static ChallengePlugin instance;
    @Getter private boolean isScheduled;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.isScheduled = this.getConfig().getBoolean("scheduled");
        if (isScheduled) deleteWorldFolder();

        this.getServer().getPluginManager().registerEvents(new ChallengeListener(), this);
        this.getCommand("deathtop").setExecutor(new DeathTopCommand());
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void scheduleRestart(String deadPlayerName) {
        isScheduled = true;
        getConfig().set("scheduled", isScheduled);
        saveConfig();

        new RestartCountdownTask(deadPlayerName).runTaskTimer(this, 1L, 20L);
    }

    private void deleteWorldFolder() {
        File serverDir = getServer().getWorldContainer();
        File worldFolder = new File(serverDir, "world");

        if (!worldFolder.exists()) {
            this.getLogger().info("Unable to find world, skipping...");
            return;
        }

        try {
            Path path = worldFolder.toPath();
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException e) {
                            getLogger().warning("Failed to delete: " + p);
                        }
                    });
            getLogger().info("Successfully deleted the world folder!");
        } catch (IOException e) {
            getLogger().warning("Failed to delete world folder!");
            e.printStackTrace();
            return;
        }

        this.isScheduled = false;
        getConfig().set("scheduled", isScheduled);
        saveConfig();
    }
}

