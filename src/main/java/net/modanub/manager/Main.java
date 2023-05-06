package net.modanub.manager;

import net.md_5.bungee.api.ChatColor;
import net.modanub.manager.command.Command;
import net.modanub.manager.command.CommandManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends JavaPlugin {
    public  static ChatManager chatManager;
    public static CommandManager commandManager = new CommandManager();
    public static StorageManager storageManager;
    public static PermissionManager permissionManager;
    public static PlayerManager playerManager;
    private static Main plugin;
    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        chatManager = new ChatManager();
        playerManager = new PlayerManager();
        permissionManager = new PermissionManager();
        storageManager = new StorageManager();
        getPlugin().getServer().getPluginManager().registerEvents(playerManager, this);
        getPlugin().getServer().getPluginManager().registerEvents(chatManager, this);
        for (Command command : commandManager.commands) {
            getCommand(command.getName()).setExecutor(commandManager);
            getLogger().info("Registered command: " + command.getName());
        }
    }

    @Override
    public void onDisable() {
        storageManager.Cleanup();
    }

    public static String ColorFromHex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}