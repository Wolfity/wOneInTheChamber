package me.wolf.woneinthechamber.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import me.wolf.woneinthechamber.OneInTheChamberPlugin;
import me.wolf.woneinthechamber.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Scoreboard {

    private final OneInTheChamberPlugin plugin = OneInTheChamberPlugin.getPlugin();

    public void waitingRoomScoreboard(final Player player) {

        FastBoard scoreboard = new FastBoard(player);
        scoreboard.updateTitle(Utils.colorize("&b&lWaiting Room"));
        new BukkitRunnable() {
            @Override
            public void run() {
                scoreboard.updateLines(
                        Utils.colorize("&aKill &2" + plugin.getConfig().getInt("max-players") + " &aplayers"),
                        "",
                        Utils.colorize("&b" + plugin.getOitcPlayers().size() + "&3/&b" + plugin.getConfig().getInt("max-players")),
                        ""
                );
            }
        }.runTaskTimer(plugin, 0L, 20L);

        plugin.getBoardMap().put(player.getUniqueId(), scoreboard);

    }


    public void ingameScoreBoard(final Player player) {

        FastBoard scoreboard = new FastBoard(player);
        scoreboard.updateTitle(Utils.colorize("&b&lOne In The Chamber"));

        scoreboard.updateLines(
                Utils.colorize("&aKill &2" + plugin.getConfig().getInt("max-players") + " &aplayers"),
                "",
                Utils.colorize("&bPlayers: " + plugin.getOitcPlayers().size() + "&3/&b" + plugin.getConfig().getInt("max-players")),
                "",
                Utils.colorize("&bTime left: &3" + plugin.getGameManager().gameTimer),
                "",
                Utils.colorize("&bKills: &3" + plugin.getOitcPlayers().get(player.getUniqueId())),
                ""
        );
        plugin.getBoardMap().put(player.getUniqueId(), scoreboard);
    }

}
