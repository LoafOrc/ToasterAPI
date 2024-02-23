package com.loafofbread.toasterapi.armor;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.loafofbread.toasterapi.item.CustomItem;

public abstract class CustomArmor {
    private final String prefix;
    private final CustomItem helmet, chestplate, leggings, boots;
    
    public String getPrefix() { return prefix; }
    
    public CustomItem getHelmet() { return helmet; }
    public CustomItem getChestplate() { return chestplate; }
    public CustomItem getLeggings() { return leggings; }
    public CustomItem getBoots() { return boots; }

    protected CustomArmor(final String prefix) {
        this.prefix = prefix;

        this.helmet = createHelmet();
        this.chestplate = createChesplate();
        this.leggings = createLeggings();
        this.boots = createBoots();
    }

    protected String createName(final String name) {
        return prefix + " " + name;
    }
    protected abstract CustomItem createHelmet();
    protected abstract CustomItem createChesplate();
    protected abstract CustomItem createLeggings();
    protected abstract CustomItem createBoots();

    // No more setBonus() and resetBonus() methods, in favour of minecrafts in-build attribute system
    // Which is infintely more robust
}