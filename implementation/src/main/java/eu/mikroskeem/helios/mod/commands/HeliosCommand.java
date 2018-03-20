/*
 * This file is part of project Helios, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017-2018 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
        if(!testPermission(sender)) return false;

        if(args.length > 0) {
            switch (args[0]) {
                case "reload":
                    HeliosMod.INSTANCE.reloadConfiguration();
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
