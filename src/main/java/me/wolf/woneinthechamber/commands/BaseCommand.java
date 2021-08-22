package me.wolf.woneinthechamber.commands;


import me.wolf.woneinthechamber.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand extends Command {

    protected CommandSender sender;

    public BaseCommand(final String name) {
        super(name);

    }

    @Override
    public boolean execute(final CommandSender sender, final String s, final String[] args) {
        this.sender = sender;


        if (!(sender instanceof Player)) {
            tell("&4You must be a player to perform this command");
            return false;

        }
        run(sender, args);
        return true;
    }

    protected abstract void run(CommandSender sender, String[] args);


    protected void tell(final String message) {
        sender.sendMessage(Utils.colorize(message));
    }

    protected boolean isAdmin() {
        return sender.hasPermission("oitc.admin");
    }

    protected void tell(final String... message) {
        sender.sendMessage(Utils.colorize(message));
    }

}

