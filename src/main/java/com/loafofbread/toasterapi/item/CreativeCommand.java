package com.loafofbread.toasterapi.item;

import com.loafofbread.toasterapi.ToasterAPI;
import com.loafofbread.toasterapi.fish.FishingGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class CreativeCommand implements CommandExecutor {

    private final String noPermission = ChatColor.RED + "" + ChatColor.BOLD + "(!)" + ChatColor.RESET + " " + ChatColor.RED + " Insufficient Permission.";
    private final String onlyPlayersCanRunThisCommand = ChatColor.RED + "" + ChatColor.BOLD + "(!)" + ChatColor.RESET + " " + ChatColor.RED + " Only players can run this command.";

    private final ToasterAPI plugin;
    public CreativeCommand(ToasterAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("creativemenu")) {
            if(sender instanceof ConsoleCommandSender) {
                sender.sendMessage(onlyPlayersCanRunThisCommand);
                return true;
            }
            Player player = (Player) sender;
            player.sendMessage("Opening Creative Menu");
            ToasterAPI.itemGUI.openInv(player, 0);
        }
        return true;
    }
}
