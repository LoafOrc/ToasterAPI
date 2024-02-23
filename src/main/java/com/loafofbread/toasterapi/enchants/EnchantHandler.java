package com.loafofbread.toasterapi.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class EnchantHandler {

    //please, this is painful
    public static Map<String, CustomEnchant> allEnchants = new HashMap();
    public static Map<JavaPlugin, Map<String, CustomEnchant>> pluginEnchants = new HashMap();

    public static void registerNewEnchant(CustomEnchant enchant) {
        allEnchants.put(enchant.getId(), enchant);
        Map<String, CustomEnchant> enchantsForPlugin = pluginEnchants.get(enchant.getOwner());
        if(enchantsForPlugin == null) {
            enchantsForPlugin = new HashMap<>();
        }
        enchantsForPlugin.put(enchant.getId(), enchant);
        registerEnchantment(enchant);
    }

    public static int getLevel(ItemStack item, Enchantment enchant){
        if(item.getItemMeta() != null && item.getItemMeta().getEnchants() != null && item.getItemMeta().getEnchants().size() > 0){
            for (Iterator<Map.Entry<Enchantment, Integer>> it = item.getItemMeta().getEnchants().entrySet().iterator(); it.hasNext();) {
                Map.Entry<Enchantment, Integer> e = it.next();
                if(e.getKey().equals(enchant)){
                    return e.getValue();
                }
            }
        }
        return 0;
    }

    private static final String[] NUMERALS = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };

    public static String returnEnchantmentName(Enchantment ench, int enchLevel){
        if(enchLevel == 1 && ench.getMaxLevel() == 1){
            return ench.getName();
        }
        if(enchLevel > 10 || enchLevel <= 0){
            return ench.getName() + " enchantment.level." + enchLevel;
        }

        return ench.getName() + " " + NUMERALS[enchLevel- 1];
    }

    /**
    private static void unregisterEnchantment(Enchantment enchantment) {
        // Disable the Power enchantment
        Enchantment enchant = Enchantment.ARROW_DAMAGE;
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            if (byKey.containsKey(enchantment.getKey())) {
                byKey.remove(enchantment.getKey());
            }
            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            if (byName.containsKey(enchantment.getName())) {
                byName.remove(enchantment.getName());
            }
        } catch (Exception ignored) {
        }
    } */

    private static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException) {} else {
                registered = false;
                e.printStackTrace();
            }
        }
        if(registered){
            // It's been registered!
        }
    }
}
