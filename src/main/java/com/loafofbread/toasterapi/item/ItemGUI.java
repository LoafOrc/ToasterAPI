package com.loafofbread.toasterapi.item;

import com.loafofbread.toasterapi.Loottable;
import com.loafofbread.toasterapi.ToasterAPI;
import com.loafofbread.toasterapi.fish.CustomFish;
import com.loafofbread.toasterapi.fish.Fishing;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class ItemGUI implements InventoryHolder {

    private GUIListener Listener;
    private NamespacedKey actionKey;

    private final JavaPlugin plugin;

    public ItemGUI(JavaPlugin plugin) {
        Listener = new GUIListener();
        actionKey = new NamespacedKey(plugin, "action");
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(Listener, plugin);
    }

    public void openInv(final HumanEntity entity, int offset) {
        entity.openInventory(getInventory(offset));
    }

    @Override
    public Inventory getInventory() {
        return getInventory(0);
    }

    public Inventory getInventory(int offset) {
        Inventory inventory = Bukkit.createInventory(this, 36, "Creative Menu");
        int x = -1;
        for (Map.Entry<String, CustomItem> pair : ToasterAPI.allItems.entrySet()) {
            x++;
            if(offset > x) continue;
            CustomItem item = pair.getValue();
            inventory.addItem(item.getItem());
        }

        ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta _meta = border.getItemMeta();

        _meta.setDisplayName(" ");
        _meta = setAction(_meta, "cancel");

        border.setItemMeta(_meta);

        int start = offset == 0 ? 27 : 28;
        int end = ToasterAPI.allItems.size() - (3*9) - offset != 0 ? 35 : 36;
        for(int i = 27; i < 36; i++) {
            inventory.setItem(i, border);
        }
        if(start == 28) {
            ItemStack backButton = new ItemStack(Material.OAK_BUTTON, 1);
            ItemMeta meta = border.getItemMeta();

            meta.setDisplayName("Back");
            meta = setAction(meta, "open_"+(offset-9));

            backButton.setItemMeta(meta);
            inventory.setItem(start-1, backButton);
        }
        if(end == 35) {
            ItemStack nextButton = new ItemStack(Material.BIRCH_BUTTON, 1);
            ItemMeta meta = border.getItemMeta();

            meta.setDisplayName("Next");
            meta = setAction(meta, "open_"+(offset+9));

            nextButton.setItemMeta(meta);
            inventory.setItem(end, nextButton);
        }
        return inventory;
    }

    private ItemMeta setAction(ItemMeta meta, String action) {
        meta.getPersistentDataContainer().set(actionKey, PersistentDataType.STRING, action);
        return meta;
    }
    private String getAction(ItemMeta meta) {
        if(!meta.getPersistentDataContainer().has(actionKey, PersistentDataType.STRING)) return "";
        return meta.getPersistentDataContainer().get(actionKey, PersistentDataType.STRING);
    }

    private class GUIListener implements org.bukkit.event.Listener {
        @EventHandler
        public void enchantGuiClick(final InventoryClickEvent event) {
            if(event.getInventory() == null) return;
            if (event.getInventory().getHolder() instanceof ItemGUI) {
                event.setCancelled(true);
                String[] args = getAction(event.getCurrentItem().getItemMeta()).split("_");
                if(args[0].equals("open")) {
                    openInv(event.getWhoClicked(), Integer.parseInt(args[1]));
                    return;
                } else if(args[0].equals("cancel")) return;
                if(event.getClick() == ClickType.SHIFT_LEFT) {
                    event.setCancelled(false);
                    ItemStack newStack = event.getCurrentItem();
                    if(newStack == null) return;
                    //newStack.setAmount(event.getCurrentItem().getMaxStackSize());
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
