package com.loafofbread.toasterapi.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public abstract class CustomEnchant extends Enchantment {

    private final String name;
    private final int maxLevel;
    private final EnchantmentTarget toolTarget;
    private final String id;
    public String getId() {
        return id;
    }
    private final JavaPlugin owningPlugin;
    public JavaPlugin getOwner() { return owningPlugin; }

    protected CustomEnchant(JavaPlugin plugin, String id, String name, int maxLevel, EnchantmentTarget toolTarget) {
        super(new NamespacedKey(plugin, name));

        this.name = name;
        this.maxLevel = maxLevel;
        this.toolTarget = toolTarget;
        this.id = id;
        this.owningPlugin = plugin;

        EnchantHandler.registerNewEnchant(this);
    }

    /**
     * @deprecated
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return toolTarget;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    /**
     * @deprecated
     */
    @Override
    public boolean isCursed() {
        return false;
    }

    public boolean isEnchant(Enchantment ench) {
        if(ench == null) return false;
        return ench.getKey() == getKey();
    }

    public boolean isEnchant(Enchantment... ench) {
        return Arrays.stream(ench).anyMatch(s -> s.getKey() == getKey());
    }

    public boolean isEnchant(Map<Enchantment, Integer> ench) {
        if(ench == null) return false;
        return ench.containsKey(this);
    }

    public abstract ArrayList<String> getDescription();

    public abstract int XPCost(int Level);

    public abstract boolean conflictsWith(Enchantment enchantment);

    public abstract boolean canEnchantItem(ItemStack itemStack);
}
