package com.loafofbread.toasterapi.boss;

import com.loafofbread.toasterapi.ToasterAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class CustomBoss implements Listener {

    private Entity boss;
    public Entity getBoss() {
        return boss;
    }

    private final Class entityClass;
    public Class getEntityClass() { return entityClass; }

    public float getMaxHealth() { return 0; }

    protected int getInvolvedRange() { return 50; }
    public ArrayList<Player> getInvolved() {
        int range = getInvolvedRange();
        ArrayList<Player> _Result = new ArrayList<>();
        for(Entity _e: boss.getNearbyEntities(range,range,range)) {
            if(_e instanceof Player) _Result.add((Player) _e);
        }
        return _Result;
    }

    private final BossBar bossBar;
    protected BarColor getBossBarColor() { return BarColor.PURPLE; }

    private final String id;

    private final ArrayList<Entity> children = new ArrayList<>();
    public ArrayList<Entity> getChildren() { return children; }

    protected CustomBoss(JavaPlugin plugin, Location spawnLocation, Class entityClass, String name) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.entityClass = entityClass;
        this.id = name.toLowerCase(Locale.ROOT).replace(' ', '_');

        boss = spawnLocation.getWorld().spawn(spawnLocation, entityClass);

        boss.setCustomName(ChatColor.GOLD + name);
        boss.setCustomNameVisible(true);

        PersistentDataContainer container = boss.getPersistentDataContainer();
        container.set(ToasterAPI.boss, PersistentDataType.STRING, id);

        if(getMaxHealth() != 0) {
            Attributable attributable = (Attributable) boss;
            attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
            Damageable damageable = (Damageable) boss;
            damageable.setHealth(getMaxHealth());
        }
        boss.setGlowing(true);

        bossBar = Bukkit.createBossBar(name, getBossBarColor(), BarStyle.SOLID);
        for(Player player : getInvolved()) {
            bossBar.addPlayer(player);
        }
    }

    public double healthPercentage() {
        Damageable damageable = (Damageable) boss;
        Attributable attributable = (Attributable) boss;
        return damageable.getHealth() / attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
    }

    protected Entity createChild(Class childClass, Location spawnLocation) {
        Entity child = boss.getWorld().spawn(spawnLocation, entityClass);
        child.getPersistentDataContainer().set(ToasterAPI.boss_child, PersistentDataType.STRING, id);
        children.add(child);
        return child;
    }

    public void childHit(EntityDamageEvent event) {}
    public void childDeath(EntityDeathEvent event) {}

    public void bossHit(EntityDamageEvent event) {}
    public void bossDeath(EntityDeathEvent event) {}

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {
        if(getInvolved().size() == 0) remove();

        bossBar.removeAll();
        for(Player _P : getInvolved()) {
            bossBar.addPlayer(_P);
        }
    }

    @EventHandler
    public void updateBossBar(EntityDamageEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setDamage(0);
        bossBar.setProgress(healthPercentage());
    }

    private void remove() {
        for(Entity e : children) {
            e.remove();
        }
        children.clear();

        HandlerList.unregisterAll(this);
        bossBar.removeAll();
    }

    @EventHandler
    public void damageEvents(EntityDamageEvent event) {
        if(isBoss(event.getEntity())) {
            bossHit(event);
        } else if(isChild(event.getEntity())) {
            childHit(event);
        }
    }
    @EventHandler
    public void deathEvents(EntityDeathEvent event) {
        if(isBoss(event.getEntity())) {
            bossDeath(event);
        } else if(isChild(event.getEntity())) {
            childDeath(event);
        }
    }
    public boolean isChild(Entity Check){
        if(Check.getPersistentDataContainer().get(ToasterAPI.boss_child, PersistentDataType.STRING) == null) return false;
        return Check.getPersistentDataContainer().get(ToasterAPI.boss_child, PersistentDataType.STRING).equals(id);
    }
    public boolean isBoss(Entity Check){
        if(Check.getType() != boss.getType()) return false;
        if(Check.getPersistentDataContainer().get(ToasterAPI.boss, PersistentDataType.STRING) == null) return false;
        return Check.getPersistentDataContainer().get(ToasterAPI.boss, PersistentDataType.STRING).equals(id);
    }

}
