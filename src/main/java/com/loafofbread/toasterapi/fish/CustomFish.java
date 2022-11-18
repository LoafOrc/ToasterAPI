package com.loafofbread.toasterapi.fish;

import com.loafofbread.toasterapi.ToasterAPI;
import com.loafofbread.toasterapi.armor.CustomArmor;
import com.loafofbread.toasterapi.item.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class CustomFish extends CustomItem {

    public static int WEATHER_BONUS = 5;
    public int getWeatherMulti() { return 1; }
    public int getBigFishChance() { return 5; }

    public int getMinSmall() { return 5; }
    public int getMaxSmall() { return 25; }

    public int getMinBig() { return 35; }
    public int getMaxBig() { return 45; }

    public int getWeight() { return 15; }

    public CustomFish(JavaPlugin plugin, String id, Material base, String name) {
        super(plugin, id, base, name);
    }

    public abstract int getXP(int size, Player player);

    public List<String> naturalFish(ItemMeta meta, Player player, boolean giveXP) {
        List<String> lore = new ArrayList<>();
        int size = getRandomLength(player, meta.getDisplayName());
        String text;
        if(size >= getMinBig()) text = ChatColor.GOLD + String.valueOf(size);
        else text = ChatColor.AQUA + String.valueOf(size);

        lore.add(ChatColor.DARK_GRAY + "Length: " + text + ChatColor.DARK_GRAY + "cm");
        lore.add(ChatColor.DARK_GRAY + "Caught by: " + ChatColor.AQUA + player.getDisplayName());

        meta.getPersistentDataContainer().set(ToasterAPI.player, PersistentDataType.STRING, player.getDisplayName());
        meta.getPersistentDataContainer().set(ToasterAPI.fish_length, PersistentDataType.INTEGER, size);

        if(giveXP) {
            int xp = getXP(size, player) + ThreadLocalRandom.current().nextInt(0, getMinSmall());
            player.sendMessage(ChatColor.GREEN + "+" + xp + "xp");
            player.giveExp(xp);
        }

        return lore;
    }

    public List<String> naturalFish(ItemMeta meta, Player player) {
        return naturalFish(meta, player, true);
    }

    public int getRandomLength(Player player, String name) {
        int weatherBonus = player.getWorld().hasStorm()? WEATHER_BONUS : 0;
        weatherBonus *= getWeatherMulti();
        int random = ThreadLocalRandom.current().nextInt(1, 100 + 1);
        if(random <= getBigFishChance()) {
            //QuestHandler handler = MinecraftOverhaul.questHandler;
            int size = ThreadLocalRandom.current().nextInt(getMinBig(), getMaxBig()) + weatherBonus;
            handleAdvancement(size, player);
            return size;
        } else {
            int size = ThreadLocalRandom.current().nextInt(getMinSmall(), getMaxSmall()) + weatherBonus;
            player.sendMessage(ChatColor.GRAY + "You caught a " + ChatColor.AQUA + name + "! Length: " + ChatColor.AQUA + size + ChatColor.GRAY + "cm");
            return size;
        }
    }

    public void handleAdvancement(int Size, Player player) {
        player.sendMessage(ChatColor.GOLD + "Great catch! " + ChatColor.GRAY + "Length: " + ChatColor.AQUA + Size + ChatColor.GRAY + "cm");
    }

    @Override
    public void put() {
        HashMap<String, CustomFish> pluginFish = ToasterAPI.pluginFish.get(plugin);
        if(pluginFish == null) {
            pluginFish = new HashMap<>();
        }
        pluginFish.put(getID(), this);
        ToasterAPI.allFish.put(getID(), this);
        Fishing.Fish.add(this, getWeight());
    }

    @Override
    public Recipe getRecipe(NamespacedKey recipeKey) { return null; }
}
