package me.wolf.woneinthechamber.game;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import me.wolf.woneinthechamber.OneInTheChamberPlugin;
import me.wolf.woneinthechamber.constants.Constants;
import me.wolf.woneinthechamber.utils.CustomLocation;
import me.wolf.woneinthechamber.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class GameManager {

    private final OneInTheChamberPlugin plugin = OneInTheChamberPlugin.getPlugin();
    private final FileConfiguration cfg = plugin.getConfig();

    @Getter
    public GameState gameState = GameState.RECRUITING;

    private int lobbyCountdown = cfg.getInt("lobby-countdown");
    public int gameTimer = cfg.getInt("game-timer");
    private final int minPlayers = cfg.getInt("min-players");


    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        switch (gameState) {
            case RECRUITING:
                recruitingAlert();
                enoughPlayers();
                break;
            case LOBBY_COUNTDOWN:
                lobbyCountdown();
                break;
            case ACTIVE:
                loadSpawnPoints();
                teleportToGameSpawns();
                gameTimer();
                setupPlayer();
                break;
            case END:
                gameStop();
                break;

        }
    }

    private void setupPlayer() {
        plugin.getOitcPlayers().keySet().stream()
                .filter(Objects::nonNull)
                .forEach(uuid -> {
                    final Player player = Bukkit.getPlayer(uuid);
                    player.getInventory().addItem(new ItemStack(Material.BOW));
                    player.getInventory().addItem(new ItemStack(Material.ARROW));
                    player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                });
    }

    private void lobbyCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (lobbyCountdown > 0) {
                    lobbyCountdown--;
                    plugin.getOitcPlayers().keySet().stream().filter(Objects::nonNull).forEach(uuid -> {
                        final Player player = Bukkit.getPlayer(uuid);
                        plugin.getScoreboard().waitingRoomScoreboard(player);
                        player.sendMessage(Utils.colorize(Constants.Messages.COUNTDOWN_MESSAGE).replace("{countdown}", String.valueOf(lobbyCountdown)));
                    });
                } else {
                    this.cancel();
                    lobbyCountdown = cfg.getInt("lobby-countdown");
                    plugin.getOitcPlayers().keySet().stream().filter(Objects::nonNull).forEach(uuid ->
                            Bukkit.getPlayer(uuid).sendMessage(Constants.Messages.GAME_STARTED.replace("{maxkills}", String.valueOf(cfg.getInt("max-kills")))));
                    setGameState(GameState.ACTIVE);

                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void gameTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameTimer > 0) {
                    gameTimer--;
                    plugin.getOitcPlayers().keySet().stream().filter(Objects::nonNull)
                            .forEach(uuid -> plugin.getScoreboard().ingameScoreBoard(Bukkit.getPlayer(uuid)));
                } else {
                    this.cancel();
                    plugin.getOitcPlayers().keySet().stream().filter(Objects::nonNull)
                            .forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(Constants.Messages.TIMER_RAN_OUT));
                    setGameState(GameState.END);
                }

            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void gameStop() {
        plugin.getOitcPlayers().keySet().stream().filter(Objects::nonNull).forEach(uuid -> {
            final Player player = Bukkit.getPlayer(uuid);
            FastBoard fastBoard = plugin.getBoardMap().remove(uuid);
            fastBoard.delete();
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.getInventory().clear(), 20L);
        });
        teleportToWorld();
        plugin.getOitcPlayers().clear();
    }

    private void recruitingAlert() {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(gameState == GameState.RECRUITING) {
                        if (plugin.getOitcPlayers().size() < minPlayers) {
                            Bukkit.broadcastMessage(Constants.Messages.RECRUITING);
                        }
                    } else this.cancel();
                }
            }.runTaskTimer(plugin, 200L, 1000L);
        }

    private void loadSpawnPoints() {
        for(String spawnLoc : cfg.getConfigurationSection("spawn-locations").getKeys(false)) {
            plugin.getSpawnLocations().add(CustomLocation.deserialize(cfg.getString("spawn-locations." + spawnLoc)));
        }
    }
    private void enoughPlayers() {
        if (gameState == GameState.RECRUITING) {
            if (plugin.getOitcPlayers().size() >= minPlayers) {
                setGameState(GameState.LOBBY_COUNTDOWN);
            }
        }
    }
    private void teleportToWorld() {
        Location worldSpawn = (Location) cfg.get("WorldSpawn");
        plugin.getOitcPlayers().keySet().stream().filter(Objects::nonNull).forEach(uuid ->
                Bukkit.getPlayer(uuid).teleport(worldSpawn));
    }

    private void teleportToGameSpawns() {
        plugin.getOitcPlayers().keySet().stream().filter(Objects::nonNull).forEach(uuid -> {
            final Random random = new Random();
            final int index = random.nextInt(plugin.getSpawnLocations().size());
            Bukkit.getPlayer(uuid)
                    .teleport(plugin.getSpawnLocations().get(index).toBukkitLocation());
        });
    }

    public void teleportToLobby(final Player player) {
        Location lobbySpawn = (Location) cfg.get("LobbySpawn");
        player.teleport(lobbySpawn);
    }
}


