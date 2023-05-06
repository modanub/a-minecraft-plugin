package net.modanub.manager.command;

import org.bukkit.command.CommandSender;

public abstract class Command {
    public abstract String getName();
    public abstract String[] getAliases();
    public abstract String getDescription();
    public abstract String getSyntax();
    public abstract Boolean allowConsole();
    public abstract Boolean execute(CommandSender sender, String[] args);
}
