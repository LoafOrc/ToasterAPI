package com.loafofbread.toasterapi;

import com.loafofbread.toasterapi.armor.ArmorHandler;
import com.loafofbread.toasterapi.armor.CustomArmor;
import com.loafofbread.toasterapi.fish.CustomFish;
import com.loafofbread.toasterapi.fish.Fishing;
import com.loafofbread.toasterapi.fish.FishingCommand;
import com.loafofbread.toasterapi.item.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class ToasterAPI extends JavaPlugin {
    public static NamespacedKey item;
    public static NamespacedKey armor;

    public static NamespacedKey player;
    public static NamespacedKey fish_length;

    public static NamespacedKey boss;
    public static NamespacedKey boss_child;

    public static HashMap<JavaPlugin, HashMap<String, CustomItem>> pluginItems = new HashMap<>();
    public static HashMap<String, CustomItem> allItems = new HashMap<>();

    public static HashMap<JavaPlugin, HashMap<String, CustomArmor>> pluginArmor = new HashMap<>();
    public static HashMap<String, CustomArmor> allArmor = new HashMap<>();

    public static HashMap<JavaPlugin, HashMap<String, CustomFish>> pluginFish = new HashMap<>();
    public static HashMap<String, CustomFish> allFish = new HashMap<>();

    public static ItemGUI itemGUI;


    @Override
    public void onEnable() {
        Bukkit.getLogger().log(Level.INFO, "Constructing Keys");
        item = new NamespacedKey(this, "item");
        armor = new NamespacedKey(this, "armor");
        player = new NamespacedKey(this, "player");
        fish_length = new NamespacedKey(this, "fish_length");
        boss = new NamespacedKey(this, "boss");
        boss_child = new NamespacedKey(this, "boss_child");

        itemGUI = new ItemGUI(this);

        Bukkit.getLogger().log(Level.INFO, "Registering Listeners");
        registerListener(new ItemHandler());
        registerListener(new ArmorHandler(this));
        registerListener(new Fishing(this));

        Bukkit.getLogger().log(Level.INFO, "Creating Commands");
        getCommand("fishingguide").setExecutor(new FishingCommand(this));
        getCommand("creativemenu").setExecutor(new CreativeCommand(this));

        Bukkit.getLogger().log(Level.INFO, "Done!");
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
    public void callEvent(Event event) {
        getServer().getPluginManager().callEvent(event);
    }
}
