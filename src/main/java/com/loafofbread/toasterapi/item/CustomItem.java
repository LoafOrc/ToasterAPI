package com.loafofbread.toasterapi.item;

import com.loafofbread.toasterapi.Loottable;
import com.loafofbread.toasterapi.ToasterAPI;

import dev.dbassett.skullcreator.SkullCreator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public abstract class CustomItem extends Loottable.Drop {
    public enum Rarity {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC
    }
    public static ChatColor getRarityColor(Rarity rarity) {
        switch (rarity) {
            case COMMON: return ChatColor.WHITE;
            case UNCOMMON: return ChatColor.GREEN;
            case RARE: return ChatColor.BLUE;
            case EPIC: return ChatColor.LIGHT_PURPLE;
        }
        return null;
    }
    
    private final String id;
    public String getID() { return id; }
    public boolean isItem(final ItemStack item) {
        if(!item.hasItemMeta()) return false;
        final PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if(container == null) return false;
        if(!container.has(ToasterAPI.item, PersistentDataType.STRING)) return false;
        return container.get(ToasterAPI.item, PersistentDataType.STRING).equalsIgnoreCase(getID());
    }
    
    private ItemStack item;
    public ItemStack getItem() { return item; }
    
    private final String name;
    @Deprecated
    public String getName() { return name; }
    
    private final NamespacedKey itemKey;
    

    public CustomItem(JavaPlugin plugin, String id, Material base, String name) {
        this.id = id;
        this.name = ChatColor.RESET + "" + getRarityColor(getRarity()) + name;
        
        if(base == Material.PLAYER_HEAD) {
            final String skullID = getSkullID();
            if(skullID == null) {
                Bukkit.getLogger().log(Level.WARNING, plugin.getName() + " has created a player head without a skull id. It will show up as a default steve skin.");
            } else {
                item = SkullCreator.itemFromBase64(skullID);
            }
        } else {
            item = new ItemStack(base);
        }
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(this.name);
        ArrayList<String> lore = getLore();
        if(!lore.isEmpty()) meta.setLore(lore);
        
        if(getModelID() != -1)
            meta.setCustomModelData(getModelID());
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(ToasterAPI.item, PersistentDataType.STRING, id);
        
        item.setItemMeta(editMeta(meta));
        itemKey = new NamespacedKey(plugin, id);
        Recipe recipe = getRecipe(itemKey);
        
        if(Bukkit.getRecipe(itemKey) != null && recipe != null) {
            Bukkit.getServer().addRecipe(recipe);
        }
        
        // Add it to add it to the list of all items, as well as that plugin's item
        HashMap<String, CustomItem> pluginItems = ToasterAPI.pluginItems.get(plugin);
        if(pluginItems == null) {
            pluginItems = new HashMap<>();
        }
        pluginItems.put(id, this);
        ToasterAPI.allItems.put(id, this);
    }
    protected ItemMeta editMeta(ItemMeta original) { return original; }
    
    protected Recipe getRecipe(NamespacedKey recipeKey) { return null; }
    public Rarity getRarity() { return Rarity.COMMON; }
    public ArrayList<String> getLore() { return new ArrayList<>(); }
    
    protected String getSkullID() { return null; }
    
    public int getModelID() { return -1; }
    
    public void rightClick(PlayerInteractEvent event) {}
    public void leftClick(PlayerInteractEvent event) {}
    public void entityHit(EntityDamageByEntityEvent event) {}
    public void consumed(PlayerItemConsumeEvent event) {}
    public void dropped(PlayerDropItemEvent event) {}
}
