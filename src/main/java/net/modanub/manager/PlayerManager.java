package net.modanub.manager;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getLogger;

public class PlayerManager implements Listener {
    public ArrayList<Player> players = new ArrayList<>();
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        getLogger().info("Player joined: " + event.getPlayer().getName());
        org.bukkit.entity.Player player = event.getPlayer();
        if (player == null) return;
        boolean found = false;
        for (Player p : players) {
            if (p.uuid.equalsIgnoreCase(player.getUniqueId().toString())) {
                found = true;
                break;
            }
        }
        if (!found) {
            players.add(new Player(player.getName(), player.getUniqueId().toString(), "default", new String[]{}));
            StorageManager.Save(StorageManager.StorageType.PLAYER);
        }
        Main.permissionManager.initialPlayer(player);
        event.setJoinMessage(ChatColor.GRAY + "Welcome to the server, " + player.getDisplayName() + "!");
    }
    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        getLogger().info("Player quit: " + event.getPlayer().getName());
        org.bukkit.entity.Player player = event.getPlayer();
        if (player == null) return;
        Main.permissionManager.perms.remove(player.getUniqueId());
        event.setQuitMessage(ChatColor.GRAY + "Goodbye, " + player.getDisplayName() + "!");
    }
    public Player getPlayer(String uuid) {
        for (Player p : players) {
            if (p.uuid.equalsIgnoreCase(uuid)) {
                return p;
            }
        }
        return null;
    }
    public class Player {
        public String name;
        public String uuid;
        public String rank;
        public String[] permissions;
        public Player(String name, String uuid, String rank, String[] permissions) {
            this.name = name;
            this.uuid = uuid;
            this.rank = rank;
            this.permissions = permissions;
        }
    }
}
