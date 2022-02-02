package com.loafofbread.toasterapi.item;

import com.loafofbread.toasterapi.ToasterAPI;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemHandler implements Listener {
    @EventHandler
    public void rightClick(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            CustomItem item = getItem(event.getItem());
            if(item != null) item.rightClick(event);
        }
    }
    @EventHandler
    public void leftClick(PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            CustomItem item = getItem(event.getItem());
            if(item != null) item.leftClick(event);
        }
    }
    @EventHandler
    public void entityHit(EntityDamageByEntityEvent event) {
        if(event.getDamager().getType() != EntityType.PLAYER) return;
        Player player = (Player) event.getDamager();
        CustomItem item = getItem(player.getItemInUse());
        if(item != null) item.entityHit(event);
    }

    private CustomItem getItem(ItemStack item) {
        if(item == null) return null;
        String id = item.getItemMeta().getPersistentDataContainer().get(ToasterAPI.item, PersistentDataType.STRING);
        return ToasterAPI.allItems.get(id);
    }
}
