package net.modanub.manager;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatManager implements Listener {
    public ChatManager() {
    }
    @EventHandler
    public void onPlayerChat(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        if (player == null) return;
        PlayerManager.Player p = Main.playerManager.getPlayer(player.getUniqueId().toString());
        if (p == null) return;
        PermissionManager.Rank rank = Main.permissionManager.getRank(p.rank);
        if (rank == null) {
            p.rank = "default";
            rank = Main.permissionManager.getRank(p.rank);
        }
        event.setFormat(ChatColor.translateAlternateColorCodes('&', rank.chatPrefix) + " " + player.getName() + ChatColor.RESET + ": " + event.getMessage());
    }
}
