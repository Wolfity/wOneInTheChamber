package me.wolf.woneinthechamber.game;

import me.wolf.woneinthechamber.OneInTheChamberPlugin;
import me.wolf.woneinthechamber.constants.Constants;
import me.wolf.woneinthechamber.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
public class GameMechanics implements Listener {

    private final OneInTheChamberPlugin plugin = OneInTheChamberPlugin.getPlugin();
    private final FileConfiguration cfg = plugin.getConfig();

    @EventHandler
    public void onArrowHit(final EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Arrow) {
            final LivingEntity shooter = (LivingEntity) ((Arrow) event.getDamager()).getShooter();
            final Player player = (Player) event.getEntity();

            assert shooter != null;
            if (plugin.getOitcPlayers().containsKey(shooter.getUniqueId()) && plugin.getOitcPlayers().containsKey(player.getUniqueId())) {
                event.setDamage(100);
            }
        }
    }

    @EventHandler
    public void onPlayerDie(final PlayerDeathEvent event) {
        event.setKeepInventory(true);
        event.setDroppedExp(0);

        if (event.getEntity().getKiller() != null) {

            final Player killer = event.getEntity().getKiller();
            final Player killed = event.getEntity();
            if (plugin.getOitcPlayers().containsKey(killed.getUniqueId()) && plugin.getOitcPlayers().containsKey(killer.getUniqueId())) {
                if(killed == killer) return;
                plugin.getOitcPlayers().put(killer.getUniqueId(), plugin.getOitcPlayers().get(killer.getUniqueId()) + 1); //perhaps create a seperate data class

                endGame();

                killer.getInventory().addItem(new ItemStack(Material.ARROW));
                resetInventory(killed);
                killed.sendMessage(Constants.Messages.PLAYER_RESPAWNED);

            }
        }
    }

    @EventHandler
    public void respawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        final Location location1 = new Location(player.getWorld(), cfg.getDouble("arena.x1"), 80, cfg.getDouble("arena.z1"));
        final Location location2 = new Location(player.getWorld(), cfg.getDouble("arena.x2"), 80, cfg.getDouble("arena.z2"));

        if (Utils.isInArena(player, location1, location2)) {
            final Random random = new Random();
            if (plugin.getSpawnLocations().isEmpty())  return;
            final int index = random.nextInt(plugin.getSpawnLocations().size());
            event.setRespawnLocation(plugin.getSpawnLocations().get(index).toBukkitLocation());

        }

    }

    private void resetInventory(final Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0,new ItemStack(Material.IRON_SWORD));
        player.getInventory().setItem(1,new ItemStack(Material.BOW));
        player.getInventory().setItem(2,new ItemStack(Material.ARROW));
    }

    private void endGame() {
        int maxKills = cfg.getInt("max-kills");
        for (Map.Entry<UUID, Integer> entry : plugin.getOitcPlayers().entrySet()) {
            if (entry.getValue() == maxKills) {

                final List<UUID> top3 = plugin.getOitcPlayers().entrySet().stream()
                        .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed()).limit(3)
                        .map(Map.Entry::getKey).collect(Collectors.toList());

                final Player first = Bukkit.getPlayer(top3.get(0));
                final Player second = Bukkit.getPlayer(top3.get(1));
                final Player third = Bukkit.getPlayer(top3.get(2));

                final int secondKills = (plugin.getOitcPlayers().get(top3.get(1)));
                final int thirdKills = (plugin.getOitcPlayers().get(top3.get(2)));

                plugin.getOitcPlayers().keySet().stream().filter(Objects::nonNull).forEach(uuid -> {
                    final Player player = Bukkit.getPlayer(uuid);
                    player.sendMessage(Constants.Messages.GAME_WON
                            .replace("{first}", first.getDisplayName())
                            .replace("{maxkills}", String.valueOf(maxKills))
                            .replace("{second}", second.getDisplayName()) //second
                            .replace("{secondkills}", String.valueOf(secondKills)) //second kills
                            .replace("{third}", third.getDisplayName()) //third
                            .replace("{thirdkills}", String.valueOf(thirdKills))); //third kills

                    player.getInventory().clear();
                });
                plugin.getGameManager().gameTimer = 0;
                plugin.getGameManager().setGameState(GameState.END);
            }
        }
    }
}
