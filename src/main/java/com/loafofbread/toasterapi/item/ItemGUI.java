package com.loafofbread.toasterapi.item;

import com.loafofbread.toasterapi.Loottable;
import com.loafofbread.toasterapi.ToasterAPI;
import com.loafofbread.toasterapi.fish.CustomFish;
import com.loafofbread.toasterapi.fish.Fishing;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class ItemGUI implements InventoryHolder {

    //TODO: Make it actually have multiple pages, and have sorting options for rarity and filter by plugin

    private Inventory FIRST_PAGE;
    private GUIListener Listener;

    private final Player player;
    private final JavaPlugin plugin;

    public ItemGUI(JavaPlugin plugin, Player player) {
        Listener = new GUIListener();
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(Listener, plugin);
        FIRST_PAGE = Bukkit.createInventory(this, 36, "Creative Menu");
        this.player = player;
        addItems();
    }

    private void addItems() {

        for (Map.Entry<String, CustomItem> pair : ToasterAPI.allItems.entrySet()) {
            CustomItem item = pair.getValue();
            FIRST_PAGE.addItem(item.getItem());
        }

        ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta _meta = border.getItemMeta();

        _meta.setDisplayName(" ");

        border.setItemMeta(_meta);

        for(int i = 27; i < 36; i++) {
            FIRST_PAGE.setItem(i, border);
        }
    }

    public void openInv(final HumanEntity entity) {
        entity.openInventory(FIRST_PAGE);
    }

    @Override
    public Inventory getInventory() {
        return FIRST_PAGE;
    }

    private class GUIListener implements org.bukkit.event.Listener {
        @EventHandler
        public void enchantGuiClick(final InventoryClickEvent event) {
            if (event.getInventory().getHolder() instanceof ItemGUI) {
                event.setCancelled(true);
                if(event.getClick() == ClickType.SHIFT_LEFT) {
                    event.setCancelled(false);
                    ItemStack newStack = event.getCurrentItem();
                    if(newStack == null) return;
                    newStack.setAmount(event.getCurrentItem().getMaxStackSize());
                    event.setCurrentItem(newStack);
                } else if(event.getClick() == ClickType.LEFT) {
                    event.setCancelled(true);
                    ItemStack newStack = event.getCurrentItem();
                    if(newStack == null) return;
                    event.setCursor(newStack);
                }
            }
        }
    }
}
