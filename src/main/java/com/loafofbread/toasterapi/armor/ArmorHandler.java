package com.loafofbread.toasterapi.armor;

import com.loafofbread.toasterapi.ToasterAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ArmorHandler implements Listener {
    private final ToasterAPI plugin;
    public ArmorHandler(ToasterAPI plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void InventoryClick(final InventoryClickEvent event) {
        BukkitTask _task = new Task((Player) event.getWhoClicked()).runTask(plugin);
    }

    @EventHandler
    public void HotbarClick(final PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            BukkitTask _task = new Task(event.getPlayer()).runTask(plugin);
        }
    }

    public class Task extends BukkitRunnable {

        Player player;
        public Task(Player _x)  {
            this.player = _x;
        }

        @Override
        public void run() {
            for(ItemStack item : player.getInventory().getArmorContents()) {
                CustomArmor armor = ToasterAPI.allArmor.get(item.getItemMeta().getPersistentDataContainer().get(ToasterAPI.armor, PersistentDataType.STRING));
                if(armor != null) {
                    armor.onEquip(player);
                }
            }
        }
    }
}
