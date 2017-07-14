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

package eu.mikroskeem.helios.mod.mixins.player;

import eu.mikroskeem.helios.api.entity.Player;
import eu.mikroskeem.helios.api.events.player.idle.PlayerActiveEvent;
import eu.mikroskeem.helios.api.events.player.idle.PlayerIdleEvent;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Handles player idle/active events and
 *
 * @author Mark Vainomaa
 */
@Mixin(value = PlayerConnection.class, remap = false)
public abstract class MixinPlayerConnection {
    @Shadow public abstract CraftPlayer getPlayer();

    private int helios$awayTicks = 0;

    @Inject(method = "e", at = @At("TAIL"))
    public void onUpdate(CallbackInfo cb) {
        Player player = (Player) getPlayer();
        if(player.isAway()) {
            if(helios$awayTicks == 0) {
                getPlayer().getServer().getPluginManager()
                        .callEvent(new PlayerIdleEvent(player, MinecraftServer.aw()));
            }
            helios$awayTicks++;
        } else {
            if(helios$awayTicks > 0) {
                helios$awayTicks = 0;
                getPlayer().getServer().getPluginManager()
                        .callEvent(new PlayerActiveEvent(player, MinecraftServer.aw()));
            }
        }
    }
}
