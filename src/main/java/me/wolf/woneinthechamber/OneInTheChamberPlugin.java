package me.wolf.woneinthechamber;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import me.wolf.woneinthechamber.commands.impl.OITCCommand;
import me.wolf.woneinthechamber.game.GameManager;
import me.wolf.woneinthechamber.game.GameMechanics;
import me.wolf.woneinthechamber.listeners.GameListeners;
import me.wolf.woneinthechamber.scoreboard.Scoreboard;
import me.wolf.woneinthechamber.utils.CustomLocation;
import me.wolf.woneinthechamber.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;

@Getter
public class OneInTheChamberPlugin extends JavaPlugin {

    @Getter private static OneInTheChamberPlugin plugin;

    private GameManager gameManager;
    private Scoreboard scoreboard;

    private final Map<UUID, Integer> oitcPlayers = new HashMap<>();
    private final List<CustomLocation> spawnLocations = new ArrayList<>();
    private final Map<UUID, FastBoard> boardMap = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info(Utils.colorize("&aEnabled One In The Chamber"));

        registerCommands();
        registerListeners();
        registerManagers();

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {

        getLogger().info(Utils.colorize("&4Disabled One In The Chamber"));

    }

    private void registerCommands() {
        Collections.singletonList(
                new OITCCommand()
        ).forEach(this::registerCommand);

    }

    private void registerListeners() {
        Arrays.asList(
                new GameListeners(),
                new GameMechanics()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void registerManagers() {
        this.gameManager = new GameManager();
        this.scoreboard = new Scoreboard();
    }

    private void registerCommand(final Command command) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(command.getLabel(), command);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
