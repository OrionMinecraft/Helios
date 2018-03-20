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

import eu.mikroskeem.helios.api.events.server.ServerStoppingEvent;
import eu.mikroskeem.helios.mod.helpers.CallEventSafe;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Mixin to detect server stop and change brand
 *
 * @author Mark Vainomaa
 */
@Mixin(value = MinecraftServer.class, remap = false)
public abstract class MixinMinecraftServer {
    private final static String helios$LOG_INFO = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V";

    @Inject(method = "stop", at = @At(
            value = "INVOKE", target = helios$LOG_INFO,
            ordinal = 0, shift = At.Shift.AFTER
    ))
    private void callServerStopping(CallbackInfo ci) {
        CallEventSafe.callEvent(new ServerStoppingEvent());
    }

    /* Replace server mod name */
    public String getServerModName() {
        return "Helios/Paper";
    }
}
