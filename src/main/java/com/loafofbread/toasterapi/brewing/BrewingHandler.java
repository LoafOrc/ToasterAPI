package com.loafofbread.toasterapi.brewing;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BrewingHandler implements Listener {
    private final JavaPlugin plugin;
    public static List<BrewingRecipe> recipes = new ArrayList<>();
    public BrewingHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void forceIngredient(final InventoryClickEvent event) {
        if(event.getInventory().getType() != InventoryType.BREWING) return;
        final ItemStack inSlot = event.getCurrentItem();
        final ItemStack cursor = event.getCursor();
        if(cursor == null) return;
        if(event.getClick() == ClickType.LEFT) { //Normal Left-Click

        }
    }

    public static BrewingRecipe getRecipe(BrewerInventory inventory) {
        for(BrewingRecipe recipe : recipes) {

        }
        return null;
    }
}
