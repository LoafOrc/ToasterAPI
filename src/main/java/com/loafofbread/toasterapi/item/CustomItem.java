package com.loafofbread.toasterapi.item;

import com.loafofbread.toasterapi.Loottable;
import com.loafofbread.toasterapi.ToasterAPI;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.loot.Lootable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
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
            default: return ChatColor.RESET;
        }
    }

    private final String id;
    public String getID() { return id; }

    private ItemStack item;
    public ItemStack getItem() { return item; }

    private final String name;
    @Deprecated
    public String getName() { return name; }

    @Deprecated
    protected final JavaPlugin plugin;

    public CustomItem(JavaPlugin plugin, String id, Material base, String name) {
        this(plugin, id, base, name, new ArrayList<>());
    }

    private boolean recipeCompleted = false;

    public CustomItem(JavaPlugin plugin, String id, Material base, String name, ArrayList<String> lore) {
        this(plugin, id, new ItemStack(base, 1), name, lore, true);
    }

    public CustomItem(JavaPlugin plugin, String id, ItemStack base, String name, ArrayList<String> lore, boolean craft) {
        this.id = id;
        this.plugin = plugin;

        this.name = ChatColor.RESET + "" + getRarityColor(getRarity()) + name;

        item = base;
        if(base.getType() == Material.PLAYER_HEAD) {
            String skullID = getSkullID();
            if(skullID == null) {
                Bukkit.getLogger().log(Level.WARNING, plugin.getName() + " has created a player head without a skull id. It will show up as a default steve skin.");
            } else {
                item = SkullCreator.itemFromBase64(skullID);
            }
        }
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RESET + "" + getRarityColor(getRarity()) + name);
        if(!lore.isEmpty()) meta.setLore(lore);

        if(getUniqueInteger() != -1) {
            meta.setCustomModelData(getUniqueInteger());
        }
        meta.setUnbreakable(isUnbreakable());

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(ToasterAPI.item, PersistentDataType.STRING, id);

        item.setItemMeta(meta);
        if(craft) {
            createRecipe();
        }

        put();
    }

    protected void put() {
        HashMap<String, CustomItem> pluginItems = ToasterAPI.pluginItems.get(plugin);
        if(pluginItems == null) {
            pluginItems = new HashMap<>();
        }
        pluginItems.put(id, this);
        ToasterAPI.allItems.put(id, this);
    }

    public void createRecipe() {
        if(recipeCompleted) return;
        Recipe recipe = getRecipe(new NamespacedKey(plugin, id));
        if (recipe != null) {
            Bukkit.getServer().addRecipe(recipe);
        }
        recipeCompleted = true;
    }

    public void setItemMeta(ItemMeta newMeta) {
        item.setItemMeta(newMeta);
    }

    protected abstract Recipe getRecipe(NamespacedKey recipeKey);
    public abstract Rarity getRarity();
    public boolean isUnbreakable() { return false; }

    protected int getUniqueInteger(){
        return -1;
    }

    protected String getSkullID() { return null; }

    public void rightClick(PlayerInteractEvent event) {}
    public void leftClick(PlayerInteractEvent event) {}
    public void entityHit(EntityDamageByEntityEvent event) {}
    public void consumed(PlayerItemConsumeEvent event) {}
    public void dropped(PlayerDropItemEvent event) {}
}
