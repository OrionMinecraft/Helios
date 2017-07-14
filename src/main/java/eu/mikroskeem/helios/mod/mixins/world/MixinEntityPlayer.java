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

import eu.mikroskeem.helios.mod.interfaces.world.HeliosWorldData;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to inject more precise spawnpoint into game
 *
 * @author Mark Vainomaa
 */
@Mixin(value = EntityPlayer.class, remap = false)
public abstract class MixinEntityPlayer extends Entity {
    private final static String GET_SPAWN_WS = "Lnet/minecraft/server/v1_12_R1/WorldServer;" +
            "getSpawn()Lnet/minecraft/server/v1_12_R1/BlockPosition;";
    private final static String GET_SPAWN_W = "Lnet/minecraft/server/v1_12_R1/World;getSpawn()" +
            "Lnet/minecraft/server/v1_12_R1/BlockPosition;";
    private final static String SET_POS = "Lnet/minecraft/server/v1_12_R1/EntityPlayer;setPosition(DDD)V";
    private final static String SET_POS_ROT = "Lnet/minecraft/server/v1_12_R1/EntityPlayer;" +
            "setPositionRotation(Lnet/minecraft/server/v1_12_R1/BlockPosition;FF)V";

    public MixinEntityPlayer() { super(null); }

    @Shadow public abstract CraftPlayer getBukkitEntity();

    /* Flag which indicates that player spawn location should be redirected */
    private boolean helios$redirectWorldSpawn = false;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = GET_SPAWN_WS))
    public BlockPosition getSpawnProxy_ctor(WorldServer worldServer) {
        helios$redirectWorldSpawn = true;
        return worldServer.getSpawn();
    }

    @Redirect(method = "spawnIn", at = @At(value = "INVOKE", target = GET_SPAWN_W))
    public BlockPosition getSpawnProxy_spawnIn(World world) {
        helios$redirectWorldSpawn = true;
        return world.getSpawn();
    }

    @Redirect(method = "spawnIn", at = @At(value = "INVOKE", target = SET_POS))
    public void setPosition_spawnIn(EntityPlayer entityPlayer, double x, double y, double z) {
        if(helios$redirectWorldSpawn) {
            Location spawnpoint = ((HeliosWorldData) world.worldData).getSpawnpoint();
            setPositionRotation(
                    spawnpoint.getX(),
                    spawnpoint.getY(),
                    spawnpoint.getZ(),
                    spawnpoint.getYaw(),
                    spawnpoint.getPitch()
            );
            helios$redirectWorldSpawn = false;
        }
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = SET_POS_ROT))
    public void setPositionRotation_ctor(EntityPlayer entity, BlockPosition blockposition, float yaw, float pitch) {
        if(helios$redirectWorldSpawn) {
            Location spawnpoint = ((HeliosWorldData) world.worldData).getSpawnpoint();
            setPositionRotation(
                    spawnpoint.getX(),
                    spawnpoint.getY(),
                    spawnpoint.getZ(),
                    spawnpoint.getYaw(),
                    spawnpoint.getPitch()
            );
            helios$redirectWorldSpawn = false;
        }
    }
}