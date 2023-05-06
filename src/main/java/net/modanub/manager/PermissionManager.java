package net.modanub.manager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

public class PermissionManager {
    public HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();
    public ArrayList<Rank> Ranks = new ArrayList<>();
    public PermissionManager() {

    }
    public void initialPlayer(Player player) {
        PermissionAttachment attachment = player.addAttachment(Main.getPlugin());
        if (perms.containsKey(player.getUniqueId())) {
            perms.get(player.getUniqueId()).remove();
        }
        perms.put(player.getUniqueId(), attachment);
        for (String perm : getPermissions(player)) {
            attachment.setPermission(perm, true);
        }
        Rank rank = getRank(player);
        player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', rank.chatPrefix) + " " + player.getName());
        player.setDisplayName(ChatColor.translateAlternateColorCodes('&', rank.chatPrefix) + " " + player.getName());
    }
    public void importRank(Rank[] ranks) {
        Ranks = new ArrayList<>(Arrays.asList(ranks));
        if (getRank("default") == null) {
            Ranks.add(new Rank("default", "Default rank", "", "", 0, true, false, new ArrayList<>()));
        }
    }
    public Rank getRank(String name) {
        for (Rank p : Ranks) {
            if (p.name.equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public Rank getRank(Player targetplayer) {
        PlayerManager.Player plr = Main.playerManager.getPlayer(targetplayer.getUniqueId().toString());
        if (plr == null) {
            return getRank("default");
        }
        return getRank(plr.rank);
    }

    public Rank[] getRanks() {
        return Ranks.toArray(new Rank[0]);
    }

    public ArrayList<String> getPermissions(Player targetplayer) {
        PlayerManager.Player plr = Main.playerManager.getPlayer(targetplayer.getUniqueId().toString());
        if (plr == null) {
            return getRank("default").permissions;
        }
        return getRank(plr.rank).permissions;
    }

    public static class Rank {
        public String name;
        public String description;
        public String chatPrefix;
        public String tabPrefix;
        public Integer tabPiority;
        public Boolean isDefault;
        public Boolean ignoreEconomic;
        public ArrayList<String> permissions;
        public Rank(String name, String description, String chatPrefix, String tabPrefix, Integer tabPiority, Boolean isDefault, Boolean ignoreEconomic, ArrayList<String> permissions) {
            this.name = name;
            this.description = description;
            this.chatPrefix = chatPrefix;
            this.tabPrefix = tabPrefix;
            this.tabPiority = tabPiority;
            this.isDefault = isDefault;
            this.ignoreEconomic = ignoreEconomic;
            this.permissions = permissions;
        }
    }
}
