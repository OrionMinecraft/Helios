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

import eu.mikroskeem.helios.api.events.error.CommandDispatchExceptionEvent;
import eu.mikroskeem.helios.mod.helpers.CallEventSafe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Mixin to add Helios command to server
 *
 * @author Mark Vainomaa
 */
@Mixin(value = SimpleCommandMap.class, remap = false)
public abstract class MixinSimpleCommandMap {
    private final static String COMMAND_EXECUTE = "Lorg/bukkit/command/Command;execute(" +
            "Lorg/bukkit/command/CommandSender;Ljava/lang/String;[Ljava/lang/String;)Z";

    @Shadow public abstract boolean register(String fallbackPrefix, Command command);

    @Inject(method = "setDefaultCommands()V", at = @At("HEAD"))
    public void onSetDefaultCommands(CallbackInfo callbackInfo) {
        /* TODO: Commands */
    }

    @Redirect(method = "dispatch", at = @At(value = "INVOKE", target = COMMAND_EXECUTE))
    public boolean errorCatchingDispatch(Command command, CommandSender sender, String commandLabel, String[] args) {
        try {
            return command.execute(sender, commandLabel, args);
        } catch (Throwable e) {
            CallEventSafe.callEvent(new CommandDispatchExceptionEvent(command, sender, commandLabel, args, e));
            throw e;
        }
    }
}
