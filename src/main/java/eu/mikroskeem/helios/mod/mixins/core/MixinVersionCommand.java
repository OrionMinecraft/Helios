/*
 * This file is part of project Helios, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VersionCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


/**
 * Injects Helios version information to {@link VersionCommand}
 *
 * @author Mark Vainomaa
 */
@Mixin(value = VersionCommand.class, remap = false)
public abstract class MixinVersionCommand {
    private final static String EXECUTE = "execute(Lorg/bukkit/command/CommandSender;" +
            "Ljava/lang/String;[Ljava/lang/String;)Z";
    private final static String SEND_VERSION = "Lorg/bukkit/command/defaults/VersionCommand;" +
            "sendVersion(Lorg/bukkit/command/CommandSender;)V";

    @Shadow protected abstract void sendVersion(CommandSender sender);

    @Redirect(method = EXECUTE, at = @At(value = "INVOKE", target = SEND_VERSION))
    public void proxySendVersion(VersionCommand versionCommand, CommandSender sender) {
        sendVersion(sender);
        sender.sendMessage("§7Server uses §6Helios§7, made with §c\u2764 §7by §c§lmikroskeem");
    }
}
