package net.modanub.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileReader;

import static org.bukkit.Bukkit.getLogger;

public class StorageManager {
    // this class will manage storage of this plugin (JSON files)
    // available json files are perms.json (contains groups and permissions), and players.json (contains player data)
    // this class will also manage the loading & saving of these files
    // this class will also manage the creation of these files if they do not exist
    public StorageManager() {
        Load(StorageType.PLAYER);
        Load(StorageType.PERMISSION);
    }
    public void Cleanup() {
        Save(StorageType.PLAYER);
        Save(StorageType.PERMISSION);
    }
    public static void Load(StorageType type) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String filename = type == StorageType.PLAYER ? "players.json" : "perms.json";
            File file = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + "/" + filename);
            if (file.exists()) {
                Reader reader = new FileReader(file);
                switch (type) {
                    case PLAYER:
                        PlayerManager.Player[] players = gson.fromJson(reader, PlayerManager.Player[].class);
                        if (players == null) {
                            players = new PlayerManager.Player[]{};
                        }
                        Main.playerManager.players = new ArrayList<>(Arrays.asList(players));
                        getLogger().info("Loaded " + players.length + " players from " + filename);
                        break;
                    case PERMISSION:
                        PermissionManager.Rank[] perms = gson.fromJson(reader, PermissionManager.Rank[].class);
                        if (perms == null) {
                            perms = new PermissionManager.Rank[]{};
                        }
                        Main.permissionManager.importRank(perms);
                        getLogger().info("Loaded " + perms.length + " permissions from " + filename);
                        break;
                }
            }
        } catch (Exception e) {
            getLogger().info("An " + e.getClass().getName() + " occurred while loading " + type.toString() + " data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void Save(StorageType type) {
        try {
            File dataFolder = new File(Main.getPlugin().getDataFolder().getAbsolutePath());
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String filename = type == StorageType.PLAYER ? "players.json" : "perms.json";
            File file = new File(Main.getPlugin().getDataFolder().getAbsolutePath() + "\\" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            Writer writer = new FileWriter(file);
            switch (type) {
                case PLAYER:
                    gson.toJson(Main.playerManager.players, writer);
                    break;
                case PERMISSION:
                    gson.toJson(Main.permissionManager.Ranks, writer);
                    break;
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            getLogger().info("An " + e.getClass().getName() + " occurred while saving " + type.toString() + " data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public enum StorageType {
        PLAYER,
        PERMISSION
    }
}
