package net.modanub.manager.command;

import net.modanub.manager.command.commands.PermissionCommand;
import net.modanub.manager.command.commands.RefreshCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {
    public final ArrayList<net.modanub.manager.command.Command> commands = new ArrayList<>();
    public CommandManager() {
        commands.add(new RefreshCommand());
        commands.add(new PermissionCommand());
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        boolean isPlayer = commandSender instanceof org.bukkit.entity.Player;
        for (net.modanub.manager.command.Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(command.getName())) {
                if (!isPlayer && !cmd.allowConsole()) {
                    commandSender.sendMessage(ChatColor.RED + "Only players can use this command!");
                    return true;
                }
                return cmd.execute(commandSender, strings);
            }
        }
        commandSender.sendMessage(ChatColor.RED + "Unable to process the command.");
        return true;
    }
}
