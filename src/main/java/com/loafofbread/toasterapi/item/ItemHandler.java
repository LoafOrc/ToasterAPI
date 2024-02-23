package com.loafofbread.toasterapi.item;

import com.loafofbread.toasterapi.ToasterAPI;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class ItemHandler implements Listener {
    @EventHandler
    public void rightClick(final PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final CustomItem item = getItem(event.getItem());
            if(item != null) item.rightClick(event);
        }
    }
    @EventHandler
    public void leftClick(final PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            final CustomItem item = getItem(event.getItem());
            if(item != null) item.leftClick(event);
        }
    }
    @EventHandler
    public void entityHit(final EntityDamageByEntityEvent event) {
        if(event.getDamager().getType() != EntityType.PLAYER) return;
        final Player player = (Player) event.getDamager();
        final CustomItem item = getItem(player.getItemInUse());
        if(item != null) item.entityHit(event);
    }
    @EventHandler
    public void consumed(final PlayerItemConsumeEvent event) {
        CustomItem item = getItem(event.getItem());
        if(item != null) item.consumed(event);
    }
    @EventHandler
    public void dropped(final PlayerDropItemEvent event) {
        CustomItem item = getItem(event.getItemDrop().getItemStack());
        if(item != null) item.dropped(event);
    }

    private CustomItem getItem(final ItemStack item) {
        if(item == null) return null;
        if(!item.getItemMeta().getPersistentDataContainer().has(ToasterAPI.item, PersistentDataType.STRING)) return null;
        final String id = item.getItemMeta().getPersistentDataContainer().get(ToasterAPI.item, PersistentDataType.STRING);
        return ToasterAPI.allItems.get(id);
    }

    public static boolean consumeItem(final Player player, int count, final Material mat) {
        final Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(mat);

        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

            if (count <= 0)
                break;
        }

        player.updateInventory();
        return true;
    }


    public static boolean consumeItem(final Player player, int count, ItemStack mat) {
        final Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(mat);
 
        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

            if (count <= 0)
                break;
        }

        player.updateInventory();
        return true;
    }

}
