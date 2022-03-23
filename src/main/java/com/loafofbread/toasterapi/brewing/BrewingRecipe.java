package com.loafofbread.toasterapi.brewing;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.java.JavaPlugin;

public class BrewingRecipe {

    private final RecipeChoice ingredient;
    public RecipeChoice getIngredient() { return ingredient; }

    private final RecipeChoice potionBase;
    public RecipeChoice getPotionBase() { return potionBase; }

    private final ItemStack result;
    public ItemStack getResult() { return result; }

    public int getCookingTime() { return 400; }
    protected BrewingRecipe(RecipeChoice ingredient, RecipeChoice potionBase, ItemStack result) {
        this.ingredient = ingredient;
        this.potionBase = potionBase;
        this.result = result;
        BrewingHandler.recipes.add(this);
    }

    public boolean isRecipe(BrewerInventory inventory) {
        
    }
}
