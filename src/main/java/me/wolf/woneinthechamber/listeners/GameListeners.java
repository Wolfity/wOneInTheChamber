package me.wolf.woneinthechamber.listeners;

import me.wolf.woneinthechamber.OneInTheChamberPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;


public class GameListeners implements Listener {

    private final OneInTheChamberPlugin plugin = OneInTheChamberPlugin.getPlugin();

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        if(plugin.getOitcPlayers().containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {
        if(plugin.getOitcPlayers().containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeather(final WeatherChangeEvent event) {
        if (event.getWorld().isThundering()) {
            event.getWorld().setThundering(false);
        }
        if (event.getWorld().hasStorm()) {
            event.getWorld().setStorm(false);
        }
    }
    @EventHandler
    public void onPickup(final EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player) {
            if (plugin.getOitcPlayers().containsKey(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent event) {
        if (plugin.getOitcPlayers().containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            if (plugin.getOitcPlayers().containsKey(event.getEntity().getUniqueId())){
                event.setCancelled(true);
            }
        }
    }
}


