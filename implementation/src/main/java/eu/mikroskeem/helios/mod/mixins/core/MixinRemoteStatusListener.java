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
import net.minecraft.server.v1_12_R1.IMinecraftServer;
import net.minecraft.server.v1_12_R1.RemoteStatusListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Query response plugins list modifier
 *
 * @author Mark Vainomaa
 */
@Mixin(value = RemoteStatusListener.class, remap = false)
public abstract class MixinRemoteStatusListener {
    private final static String HELIOS$CREATE_QUERY_RESPONSE = "b(Ljava/net/DatagramPacket;)[B";
    private final static String HELIOS$GET_PLUGINS = "Lnet/minecraft/server/v1_12_R1/IMinecraftServer;" +
            "getPlugins()Ljava/lang/String;";

    @Redirect(method = HELIOS$CREATE_QUERY_RESPONSE, at = @At(
            value = "INVOKE",
            target = HELIOS$GET_PLUGINS
    ))
    private String getPlugins(IMinecraftServer iMinecraftServer) {
        if(helios$getConfiguration().getModifyQueryPluginsList()) {
            return helios$getConfiguration().getQueryCustomPluginInformation();
        }
        return iMinecraftServer.getPlugins();
    }


    private ServerConfiguration helios$getConfiguration() {
        return HeliosMod.INSTANCE.getConfigurationWrapper().getConfiguration().getServer();
    }
}
