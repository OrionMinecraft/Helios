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

package eu.mikroskeem.helios.mod.mixins.core;

import eu.mikroskeem.helios.mod.HeliosMod;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


/**
 * Mixin to modify command system behaviour
 *
 * @author Mark Vainomaa
 */
@Mixin(value = Command.class, remap = false, priority = 999)
public abstract class MixinCommand {
    @Shadow private String permissionMessage;
    @Shadow private String permission;
    @Shadow private String name;
    @Shadow public abstract boolean testPermissionSilent(CommandSender target);

    public boolean testPermission(CommandSender target) {
        if(testPermissionSilent(target))
            return true;

        /* Send permission message */
        boolean shouldOverride = HeliosMod.INSTANCE.getConfiguration()
                .getCommand()
                .getOverridePluginCommandPermissionDeniedMessage();

        String overrideMessage = HeliosMod.INSTANCE.getConfiguration()
                .getCommand()
                .getPermissionDeniedMessage();

        /* Pick correct message and send it to target */
        String[] messages = (shouldOverride ? overrideMessage : (permissionMessage != null ? permissionMessage : overrideMessage))
                .replace("<permission>", permission).replace("%permission%", permission)
                .replace("<command>", name).replace("%command%", name)
                .split("\n");

        target.sendMessage(messages);
        return false;
    }
}
