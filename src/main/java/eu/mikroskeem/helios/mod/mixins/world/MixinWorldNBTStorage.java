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

package eu.mikroskeem.helios.mod.mixins.world;

import eu.mikroskeem.helios.mod.HeliosMod;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.IPlayerFileData;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.WorldNBTStorage;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


/**
 * Mixin which allows/disallows player data saving
 *
 * @author Mark Vainomaa
 */
@Mixin(value = WorldNBTStorage.class, remap = false)
@Implements(@Interface(iface = IPlayerFileData.class, prefix = "pfd$"))
public abstract class MixinWorldNBTStorage {
    private final static String[] EMPTY_STRING_ARRAY = new String[0];

    @Shadow public abstract String[] getSeenPlayers();

    @Inject(method = "save", cancellable = true, at = @At("HEAD"))
    public void onSave(EntityHuman e, CallbackInfo cb) {
        if(helios$isSavingDisabled())
            cb.cancel();
    }

    @Inject(method = "load", cancellable = true, at = @At("HEAD"))
    public void load(EntityHuman e, CallbackInfoReturnable<NBTTagCompound> cb) {
        if(helios$isSavingDisabled())
            cb.setReturnValue(null);
    }

    @Intrinsic(displace = true)
    public String[] pfd$getSeenPlayers() {
        if(helios$isSavingDisabled())
            return EMPTY_STRING_ARRAY;
        return this.getSeenPlayers();
    }

    private boolean helios$isSavingDisabled() {
        return HeliosMod.INSTANCE.getConfigurationWrapper()
                .getConfiguration()
                .getPlayerConfiguration()
                .getPlayerDataSavingDisabled();
    }
}