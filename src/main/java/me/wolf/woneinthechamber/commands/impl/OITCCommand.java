package me.wolf.woneinthechamber.commands.impl;

import fr.mrmicky.fastboard.FastBoard;
import me.wolf.woneinthechamber.OneInTheChamberPlugin;
import me.wolf.woneinthechamber.commands.BaseCommand;
import me.wolf.woneinthechamber.constants.Constants;
import me.wolf.woneinthechamber.game.GameState;
import me.wolf.woneinthechamber.utils.CustomLocation;
import me.wolf.woneinthechamber.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class OITCCommand extends BaseCommand {

    private OneInTheChamberPlugin plugin = OneInTheChamberPlugin.getPlugin();
    private FileConfiguration cfg = plugin.getConfig();


    public OITCCommand() {
        super("oitc");
    }


    @Override
    protected void run(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();

        if (args.length != 1) tell(Constants.Messages.HELP_MESSAGE);

        if (args[0].equalsIgnoreCase("join")) {
            addPlayer(uuid);
            plugin.getGameManager().setGameState(GameState.RECRUITING);
        } else if (args[0].equalsIgnoreCase("leave")) {
            removePlayer(uuid);
        } else if (args[0].equalsIgnoreCase("help")) {
            tell(Constants.Messages.HELP_MESSAGE);
        }

        if (isAdmin()) {
            if (args[0].equalsIgnoreCase("admin")) {
                tell(Constants.Messages.ADMIN_HELP_MESSAGE);
            } else if (args[0].equalsIgnoreCase("forcestart")) {
                plugin.getGameManager().setGameState(GameState.LOBBY_COUNTDOWN);
            } else if (args[0].equalsIgnoreCase("forceend")) {
                plugin.getGameManager().setGameState(GameState.END);
            } else if (args[0].equalsIgnoreCase("setspawnloc")) {
                arenaValidation(player);
                tell(Constants.Messages.SPAWN_LOC_SET);
            } else if (args[0].equalsIgnoreCase("setlobbyspawn")) {
                plugin.getConfig().set("LobbySpawn", player.getLocation());
                plugin.saveConfig();
                tell(Constants.Messages.LOBBY_SPAWN_SET);
            } else if (args[0].equalsIgnoreCase("setworldspawn")) {
                plugin.getConfig().set("WorldSpawn", player.getLocation());
                plugin.saveConfig();
                tell(Constants.Messages.WORLD_SPAWN_SET);
            }
        }
    }

    private void addPlayer(final UUID uuid) {
        if (plugin.getOitcPlayers().size() != cfg.getInt("max-players")) {
            if (!plugin.getOitcPlayers().containsKey(uuid)) {
                plugin.getOitcPlayers().put(uuid, 0);
                tell(Constants.Messages.JOINED_WAITING_ROOM);
                plugin.getGameManager().teleportToLobby(Bukkit.getPlayer(uuid));
                plugin.getScoreboard().waitingRoomScoreboard(Bukkit.getPlayer(uuid));
            } else tell(Constants.Messages.ALREADY_INGAME
            );
        } else tell(Constants.Messages.GAME_IS_FULL);
    }

    private void removePlayer(final UUID uuid) {
        if (plugin.getOitcPlayers().containsKey(uuid)) {
            plugin.getOitcPlayers().remove(uuid);
            tell(Constants.Messages.LEFT_WAITING_ROOM);

            FastBoard fastBoard = plugin.getBoardMap().remove(uuid);
            if (fastBoard != null) {
                fastBoard.delete();
            }

        } else tell(Constants.Messages.NOT_INGAME);
    }

    private void arenaValidation(final Player player) {
        if (Utils.isInArena(player,
                new Location(player.getWorld(), cfg.getDouble("arena.x1"), 80, cfg.getDouble("arena.z1")),
                new Location(player.getWorld(), cfg.getDouble("arena.x2"), 80, cfg.getDouble("arena.z2")))) {
            plugin.getSpawnLocations().add(CustomLocation.fromBukkitLocation(player.getLocation()));
            int i = 1;
            for (final CustomLocation location : plugin.getSpawnLocations()) {
                plugin.getConfig().set("spawn-locations." + i, location.serialize());
                plugin.saveConfig();
                i++;
            }
        } else tell(Constants.Messages.CANT_CREATE_SPAWN_POINT);
    }
}

