package com.loafofbread.toasterapi.fish;

import com.loafofbread.toasterapi.ToasterAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class FishingCommand implements CommandExecutor {

    private final Permission fishing_guide = Bukkit.getPluginManager().getPermission("loafofbread.fishing.guide");
    private final String noPermission = ChatColor.RED + "" + ChatColor.BOLD + "(!)" + ChatColor.RESET + " " + ChatColor.RED + " Insufficient Permission.";
    private final String onlyPlayersCanRunThisCommand = ChatColor.RED + "" + ChatColor.BOLD + "(!)" + ChatColor.RESET + " " + ChatColor.RED + " Only players can run this command.";

    private final ToasterAPI plugin;
    public FishingCommand(ToasterAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("fishingguide")) {
            if(sender instanceof ConsoleCommandSender) {
                sender.sendMessage(onlyPlayersCanRunThisCommand);
                return true;
            }
            Player player = (Player) sender;
            if(!player.hasPermission(fishing_guide)) {
                player.sendMessage(noPermission);
                return true;
            }
            new FishingGUI(plugin, player).openInv(player);
        }
        return true;
    }
}
