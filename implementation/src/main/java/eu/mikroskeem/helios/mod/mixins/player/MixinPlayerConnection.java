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
import eu.mikroskeem.helios.mod.HeliosMod;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Handles player idle/active events, keep-alive packet sending/receiving and idle timeout kicking
 *
 * @author Mark Vainomaa
 */
@Mixin(value = PlayerConnection.class, remap = false)
public abstract class MixinPlayerConnection {
    private final static String helios$NETWORK_TICK_COUNT = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;e:I";
    private final static String helios$LAST_SENT_PING_PACKET = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;h:J";
    private final static String helios$PROCESS_KEEP_ALIVE = "a(Lnet/minecraft/server/v1_12_R1/PacketPlayInKeepAlive;)V";
    private final static String helios$GET_LAST_ACTIVE_TIME = "Lnet/minecraft/server/v1_12_R1/EntityPlayer;J()J";

    @Shadow public abstract void sendPacket(Packet<?> packet);
    @Shadow public abstract CraftPlayer getPlayer();
    @Shadow public EntityPlayer player;

    private int helios$awayTicks = 0;

    @Redirect(method = "e", at = @At(value = "INVOKE", target = helios$GET_LAST_ACTIVE_TIME, ordinal = 0))
    private long getLastActiveTime(EntityPlayer entityPlayer) {
        boolean dontKick = HeliosMod.INSTANCE.getConfigurationWrapper()
                .getConfiguration()
                .getPlayer()
                .getDontKickOppedPlayersOnIdle();

        if(entityPlayer.getBukkitEntity().isOp() && dontKick)
            /* if(0L > 0L && ...) */
            return 0L;
        else
            /* Default */
            return entityPlayer.J();
    }

    @Inject(method = "e", at = @At("TAIL"))
    private void onUpdate(CallbackInfo cb) {
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