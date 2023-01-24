package com.loafofbread.toasterapi.fish;


import com.loafofbread.toasterapi.Loottable;
import com.loafofbread.toasterapi.ToasterAPI;
import com.loafofbread.toasterapi.enchants.EnchantHandler;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Fishing implements Listener {

    public static Loottable Fish = new Loottable();
    public static Loottable Treasure = new Loottable();
    public static Loottable Mobs = new Loottable();

    public enum CAUGHT {
        FISH,
        TREASURE,
        MOB,
        FAIL
    }

    private final float FISH_BASE_CHANCE =  85;
    private final float FISH_MODIFER =      1.5f;

    public float getFishChance(int level) {
        return FISH_BASE_CHANCE-(level*FISH_MODIFER);
    }

    private final float TREASURE_BASE_CHANCE =  10;
    private final float TREASURE_MODIFER =      2;

    public float getTreasureChance(int level) {
        return TREASURE_BASE_CHANCE+(level*TREASURE_MODIFER);
    }

    private final float MOB_BASE_CHANCE =  5;
    private final float MOB_MODIFER =      1;

    public float getMobChance(int level) {
        return MOB_BASE_CHANCE+(level*MOB_MODIFER);
    }

    public final int LEVEL_BONUS = 45;
    public final int LEVEL_ADD = 2;

    private final ToasterAPI plugin;
    private final Random random = new Random();

    public Fishing(ToasterAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void enhancedRod(final PlayerFishEvent event) {
        if(event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {

            CAUGHT caught;

            int LuckOfTheSeaLevel = EnchantHandler.getLevel(event.getPlayer().getItemInHand(), Enchantment.LUCK);
            float value = 0 + random.nextFloat() * ((getTreasureChance(LuckOfTheSeaLevel) + getFishChance(LuckOfTheSeaLevel) + getMobChance(LuckOfTheSeaLevel)) - 0);

            if(value <= getFishChance(LuckOfTheSeaLevel)) caught = CAUGHT.FISH;
            else if(value <= getFishChance(LuckOfTheSeaLevel) + getTreasureChance(LuckOfTheSeaLevel) && event.getHook().isInOpenWater()) caught = CAUGHT.TREASURE;
            else if(event.getHook().isInOpenWater()) caught = CAUGHT.MOB;
            else caught = CAUGHT.FAIL;

            Player player = event.getPlayer();
            World world = player.getWorld();

            switch (caught) {
                case FISH:
                    CustomFish fish = (CustomFish) Fish.getItem(world, player);
                    ItemStack stack = fish.getItem();

                    ItemMeta meta = stack.getItemMeta();
                    List<String> lore = fish.naturalFish(meta, player);

                    meta.setLore(lore);
                    stack.setItemMeta(meta);

                    FishCaughtEvent e = new FishCaughtEvent(fish, player);
                    plugin.callEvent(e);
                    if(!e.isCancelled()) {
                        ((Item) event.getCaught()).setItemStack(stack);
                    } else {
                        player.sendMessage(ChatColor.RED + "The line broke and you didn't catch anything");
                        event.getCaught().remove();
                    }
                    break;
                case TREASURE:
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 0 + random.nextFloat() * (2 - 0));
                    player.sendMessage(ChatColor.GOLD + "SHINY!");
                    ItemStack treasure = (ItemStack) Treasure.getItem(world, player).item;
                    int bonus = player.getLevel() >= LEVEL_BONUS? LEVEL_ADD : 0;
                    treasure.setAmount(treasure.getAmount() + bonus);

                    int xp = ThreadLocalRandom.current().nextInt(10,26);
                    player.sendMessage(ChatColor.GREEN + "+" + xp + "xp");
                    player.giveExp(xp);

                    ((Item) event.getCaught()).setItemStack(treasure);
                    break;
                case MOB:
                    Entity spawned = player.getWorld().spawn(event.getHook().getLocation(), Mobs.getItem(world, player).entity);
                    new Task(spawned, event.getCaught().getVelocity()).run();
                    ((Mob) spawned).setTarget(event.getPlayer());
                    event.getCaught().remove();
                    break;
                case FAIL:
                    player.sendMessage(ChatColor.RED + "The line broke and you didn't catch anything");
                    event.getCaught().remove();
                default:
                    throw new IllegalStateException("i do bad think");
            }
        }
    }

    private class Task extends BukkitRunnable {

        private final Entity _1;
        private final org.bukkit.util.Vector _2;

        public Task(Entity e, org.bukkit.util.Vector v) {
            _1 = e;
            _2 = v;
        }

        @Override
        public void run() {
            _1.setVelocity(_2);
        }
    }
}
