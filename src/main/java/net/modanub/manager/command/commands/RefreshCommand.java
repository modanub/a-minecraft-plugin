package net.modanub.manager.command.commands;

import net.modanub.manager.StorageManager;
import net.modanub.manager.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RefreshCommand extends Command {
    @Override
    public String getName() {
        return "refresh";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"refresh"};
    }

    @Override
    public String getDescription() {
        return "Reload all cached data";
    }

    @Override
    public String getSyntax() {
        return "/refresh";
    }

    @Override
    public Boolean allowConsole() {
        return true;
    }

    @Override
    public Boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.GRAY + "Refreshing data...");
        StorageManager.Load(StorageManager.StorageType.PLAYER);
        StorageManager.Load(StorageManager.StorageType.PERMISSION);
        sender.sendMessage(ChatColor.GREEN + "Data refreshed!");
        return true;
    }
}
