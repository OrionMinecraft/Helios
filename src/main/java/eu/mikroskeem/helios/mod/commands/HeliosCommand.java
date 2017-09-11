package eu.mikroskeem.helios.mod.commands;

import eu.mikroskeem.helios.mod.HeliosMod;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Helios main command
 *
 * @author Mark Vainomaa
 */
public final class HeliosCommand extends Command {
    private final List<String> SUBCOMMANDS = Arrays.asList("reload");

    public HeliosCommand() {
        super("helios", "Helios mod main command", "/<command>", Collections.emptyList());
        setPermission("helios.command");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(args.length > 0) {
            switch (args[0]) {
                case "reload":
                    HeliosMod.INSTANCE.loadConfiguration();
                    HeliosMod.INSTANCE.saveConfiguration();
                    sender.sendMessage("§aReload done!");
                    return true;
            }
        }
        sender.sendMessage("§cUsage: /" + commandLabel + " [reload]");
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return StringUtil.copyPartialMatches(args[0], SUBCOMMANDS, new ArrayList<>());
    }
}
