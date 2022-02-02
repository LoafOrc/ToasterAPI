package com.loafofbread.toasterapi.fish;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FishCaughtEvent extends Event implements Cancellable {
    private CustomFish fish;
    private Player player;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public FishCaughtEvent(CustomFish fish, Player player) {
        this.fish = fish;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public CustomFish getFish() {
        return fish;
    }
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }
}