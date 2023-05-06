package net.modanub.manager.command.commands;

import net.modanub.manager.Main;
import net.modanub.manager.PlayerManager;
import net.modanub.manager.StorageManager;
import net.modanub.manager.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static net.modanub.manager.PermissionManager.*;

public class PermissionCommand extends Command {
    @Override
    public String getName() {
        return "permission";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"rank", "permissions", "perms"};
    }

    @Override
    public String getDescription() {
        return "Manage permissions";
    }

    @Override
    public String getSyntax() {
        return "/permission <save|view|set|list|refresh> <player|rank>";
    }

    @Override
    public Boolean allowConsole() {
        return true;
    }

    @Override
    public Boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Invalid syntax! Usage: " + getSyntax());
            return true;
        }
        String subCommand = args[0];
        switch (subCommand.toLowerCase()) {
            case "view": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Invalid syntax! Usage: " + ChatColor.WHITE + "/permission view <player|rank>");
                    return true;
                }
                Rank targetrank = Main.permissionManager.getRank(args[1].toLowerCase());
                Player targetplayer = Main.getPlugin().getServer().getPlayer(args[1]);
                if (targetrank == null && targetplayer == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid player or rank!");
                    return true;
                }
                if (targetrank != null) {
                    sender.sendMessage(ChatColor.GRAY + "Permissions for rank " + ChatColor.WHITE + targetrank.name + ChatColor.GRAY + ":");
                    for (String permission : targetrank.permissions) {
                        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + permission);
                    }
                }
                if (targetplayer != null) {
                    sender.sendMessage(ChatColor.GRAY + "Permissions for player " + ChatColor.WHITE + targetplayer.getName() + ChatColor.GRAY + ":");
                    for (String permission : Main.permissionManager.getPermissions(targetplayer)) {
                        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + permission);
                    }
                }
                break;
            }
            case "set": {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "Invalid syntax! Usage: " + ChatColor.WHITE + "/permission set <rank> <prefix|tabpriority|permissions> <value>");
                    return true;
                }
                Rank targetrank = Main.permissionManager.getRank(args[1].toLowerCase());
                if (targetrank == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid rank!");
                    return true;
                }
                String subsubCommand = args[2].toLowerCase();
                switch (subsubCommand) {
                    case "prefix":
                    {
                        targetrank.chatPrefix = args[3];
                        sender.sendMessage(ChatColor.GRAY + "Set chat prefix for rank " + ChatColor.WHITE + targetrank.name + ChatColor.GRAY + " to " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', targetrank.chatPrefix));
                        break;
                    }
                    case "tabpriority":
                    {
                        try {
                            targetrank.tabPiority = Integer.parseInt(args[3]);
                            sender.sendMessage(ChatColor.GRAY + "Set tab priority for rank " + ChatColor.WHITE + targetrank.name + ChatColor.GRAY + " to " + ChatColor.WHITE + targetrank.tabPiority);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid number!");
                            return true;
                        }
                        break;
                    }
                    case "permissions":
                    {
                        // join all args after the 3rd arg
                        StringBuilder permissions = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            permissions.append(args[i]).append(" ");
                        }
                        permissions = new StringBuilder(permissions.toString().trim());
                        for (String permission : permissions.toString().split(" ")) {
                            boolean delete = permission.startsWith("-");
                            // check if permission exist inside the permission list
                            if (delete && targetrank.permissions.contains(permission.substring(1))) {
                                targetrank.permissions.remove(permission.substring(1));
                                sender.sendMessage(ChatColor.GRAY + "Removed permission " + ChatColor.WHITE + permission.substring(1) + ChatColor.GRAY + " from rank " + ChatColor.WHITE + targetrank.name);
                            } else if (!delete && !targetrank.permissions.contains(permission)) {
                                targetrank.permissions.add(permission);
                                sender.sendMessage(ChatColor.GRAY + "Added permission " + ChatColor.WHITE + permission + ChatColor.GRAY + " to rank " + ChatColor.WHITE + targetrank.name);
                            } else {
                                sender.sendMessage(ChatColor.RED + "Invalid permission parameter!");
                                return true;
                            }
                        }
                        for (Player player : Main.getPlugin().getServer().getOnlinePlayers()) {
                            if (Main.permissionManager.getRank(player).equals(targetrank)) {
                                Main.permissionManager.initialPlayer(player);
                            }
                        }
                    }
                }
                break;
            }
            case "player": {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Invalid syntax! Usage: " + ChatColor.WHITE + "/permission player <player> <rank>");
                    return true;
                }
                Player targetplayer = Main.getPlugin().getServer().getPlayer(args[1]);
                if (targetplayer == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid player!");
                    return true;
                }
                PlayerManager.Player plrtoo = Main.playerManager.getPlayer(targetplayer.getUniqueId().toString());
                if (plrtoo == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid player!");
                    return true;
                }
                Rank targetrank = Main.permissionManager.getRank(args[2].toLowerCase());
                if (targetrank == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid rank!");
                    return true;
                }
                plrtoo.rank = targetrank.name;
                Main.permissionManager.initialPlayer(targetplayer);
                sender.sendMessage(ChatColor.GRAY + "Set rank for player " + ChatColor.WHITE + targetplayer.getName() + ChatColor.GRAY + " to " + ChatColor.WHITE + targetrank.name);
                break;
            }
            case "list": {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Invalid syntax! Usage: " + ChatColor.WHITE + "/permission list <player|rank>");
                    return true;
                }
                if (!args[1].equalsIgnoreCase("player") && !args[1].equalsIgnoreCase("rank")) {
                    sender.sendMessage(ChatColor.RED + "Invalid syntax! Usage: " + ChatColor.WHITE + "/permission list <player|rank>");
                    return true;
                }
                if (args[1].equalsIgnoreCase("player")) {
                    // show online players list that include their ranks
                    sender.sendMessage(ChatColor.GRAY + "Players:");
                    for (Player player : Main.getPlugin().getServer().getOnlinePlayers()) {
                        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + player.getName() + ChatColor.GRAY + " (" + ChatColor.WHITE + Main.permissionManager.getRank(player).name + ChatColor.GRAY + ")");
                    }
                }
                if (args[1].equalsIgnoreCase("rank")) {
                    sender.sendMessage(ChatColor.GRAY + "Ranks:");
                    for (Rank rank : Arrays.stream(Main.permissionManager.getRanks()).sorted((rank1, rank2) -> rank1.tabPiority - rank2.tabPiority).toArray(Rank[]::new)) {
                        sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + rank.name + ChatColor.GRAY + " (" + ChatColor.WHITE + rank.tabPiority + ChatColor.GRAY + ")");
                    }
                }
                break;
            }
            case "save": {
                sender.sendMessage(ChatColor.DARK_GRAY + "Saving...");
                StorageManager.Save(StorageManager.StorageType.PERMISSION);
                sender.sendMessage(ChatColor.GREEN + "Saved!");
                break;
            }
            default: {
                sender.sendMessage(ChatColor.RED + "Invalid syntax! Usage: " + ChatColor.WHITE + getSyntax());
                return true;
            }
        }
        return true;
    }
}
