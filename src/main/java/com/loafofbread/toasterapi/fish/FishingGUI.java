package com.loafofbread.toasterapi.fish;

import com.loafofbread.toasterapi.Loottable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Map;

public class FishingGUI implements InventoryHolder {

    private Inventory FIRST_PAGE;
    private GUIListener Listener;

    private final Player player;
    private final JavaPlugin plugin;

    public FishingGUI(JavaPlugin plugin, Player player) {
        Listener = new GUIListener();
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(Listener, plugin);
        FIRST_PAGE = Bukkit.createInventory(this, 36, "Fishing Guide");
        this.player = player;
        addItems();
    }

    private void addItems() {

        for (Map.Entry<Double, Loottable.Drop> pair : Fishing.Fish.map.entrySet()) {
            CustomFish fish = (CustomFish) pair.getValue();
            ItemStack _stack = fish.getItem();
            ItemMeta _meta = _stack.getItemMeta();

            _meta.setDisplayName(fish.getName());

            ArrayList<String> Lore = new ArrayList<>();
            //Lore.add(ChatColor.RESET + "" + ChatColor.BLUE + "Costs " + ChatColor.BOLD + pair.getValue().XPCost(i) + ChatColor.BLUE + " levels");
            Lore.add(ChatColor.RESET + "" + ChatColor.BLUE + + fish.getMinSmall() + " - " + fish.getMaxBig() + "cm");

            _meta.setLore(Lore);
            _stack.setItemMeta(_meta);
            FIRST_PAGE.addItem(_stack);
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

    private class GUIListener implements Listener {
        @EventHandler
        public void enchantGuiClick(final InventoryClickEvent event) {
            if (event.getInventory().getHolder() instanceof FishingGUI) {
                event.setCancelled(true);
            }
        }
    }
}
