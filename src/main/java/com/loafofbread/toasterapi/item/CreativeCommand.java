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
    private String createErrorMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', "&4&l(!) &f"+message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("creativemenu")) {
            throw new RuntimeException("how tf");
        }

        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage(createErrorMessage("Unavaliable in the console"));
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("toasterapi.item.menu")) {
            sender.sendMessage(createErrorMessage("No Permission"));
            return true;
        }

        player.sendMessage("Opening Creative Menu");
        ToasterAPI.itemGUI.openInv(player, 0);
        
        return true;
    }
}
