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

import eu.mikroskeem.helios.mod.HeliosMod;
import eu.mikroskeem.helios.mod.configuration.ServerConfiguration;
import net.minecraft.server.v1_12_R1.EULA;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;


/**
 * Mixin to accept Mojang EULA according to {@link ServerConfiguration#acceptEULA}
 *
 * Why? Because you must agree to EULA anyway when you run Minecraft server
 *
 * @author Mark Vainomaa
 */
@Mixin(value = EULA.class, remap = false)
public abstract class MixinEULA {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(File file, CallbackInfo ci) {
        /* Remove EULA agree system property if present */
        if(helios$doesAccept() && Boolean.getBoolean("com.mojang.eula.agree"))
            System.getProperties().remove("com.mojang.eula.agree");
    }

    @Inject(method = "a(Ljava/io/File;)Z", cancellable = true, at = @At("HEAD"))
    public void onA(CallbackInfoReturnable<Boolean> cb) {
        if(helios$doesAccept())
            cb.setReturnValue(true);
    }

    @Inject(method = "b", cancellable = true, at = @At("HEAD"))
    public void onB(CallbackInfo cb) {
        if(helios$doesAccept())
            cb.cancel();
    }

    private boolean helios$doesAccept() {
        return HeliosMod.INSTANCE.getConfigurationWrapper()
                .getConfiguration()
                .getServerConfiguration()
                .getAcceptEULA();
    }
}
