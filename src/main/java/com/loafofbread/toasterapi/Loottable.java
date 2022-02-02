package com.loafofbread.toasterapi;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class Loottable {
    public static class Drop {
        public Object item;
        public Class entity;
        public boolean conditions(World world, Player player) { return true; }
        public Drop setItem(Object item) { this.item = item; return this; }
        public Drop setClass(Class item) { this.entity = item; return this; }
        protected Biome getBiome(Player player) {
            return player.getWorld().getBlockAt(player.getLocation()).getBiome();
        }
        public boolean day(World world) {
            long time = world.getTime();

            return time < 12300 || time > 23850;
        }
    }
    public NavigableMap<Double, Drop> map = new TreeMap<>();
    private final Random random = new Random();
    private double total = 0;
    public boolean add(Drop drop, int chance) {
        if (chance <= 0) return false;
        total += chance;
        map.put(total, drop);
        return true;
    }
    public Drop next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
    public Drop getItem(World world, Player player) {
        Drop _return = null;
        int failsafe = 0;
        while(_return == null) {
            Drop focused = next();
            if(focused.conditions(world, player)) {
                _return = focused;
                break;
            }
            failsafe++;
            if(failsafe > 300) break;
        }
        return _return;
    }
    public void add(Drop drop) { add(drop, 1); }
}
