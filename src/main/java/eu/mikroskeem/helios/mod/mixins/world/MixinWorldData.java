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
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.WorldData;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Location;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to inject more precise spawnpoint location into world data
 *
 * Note: priority is 1001 to apply it after {@link MixinWorldDataAccessors}
 *
 * @author Mark Vainomaa
 */
@Mixin(value = WorldData.class, priority = 1001, remap = false)
public abstract class MixinWorldData implements HeliosWorldData {
    private final static String NBT_BASE = "helios.spawnpoint";

    @Shadow public WorldServer world;
    @Shadow public abstract int b(); // MCP - getSpawnX
    @Shadow public abstract int c(); // MCP - getSpawnY
    @Shadow public abstract int d(); // MCP - getSpawnZ
    @Shadow private int h;  // MCP - spawnX
    @Shadow private int j;  // MCP - spawnY
    @Shadow private int i;  // MCP - spawnZ

    @Override
    public Location getSpawnpoint() {
        return new Location(
                this.world.getWorld(),
                getSpawnpointX(),
                getSpawnpointY(),
                getSpawnpointZ(),
                getSpawnpointYaw(),
                getSpawnpointPitch()
        );
    }

    @Override
    public void setSpawnpoint(Location location) {
        setSpawnpointX(location.getX());
        setSpawnpointY(location.getY());
        setSpawnpointZ(location.getZ());
        setSpawnpointYaw(location.getYaw());
        setSpawnpointPitch(location.getPitch());

        /* Sigh... */
        this.h = location.getBlockX();
        this.i = location.getBlockY();
        this.j = location.getBlockZ();
    }

    /* updateTagCompound - in MCP */
    @Inject(method = "a(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;" +
            "Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V", at = @At("HEAD"))
    public void onUpdateTagCompound(NBTTagCompound nbt, NBTTagCompound playerNbt, CallbackInfo cb) {
        NBTTagCompound heliosSpawn = new NBTTagCompound();
        heliosSpawn.setDouble("x", getSpawnpointX());
        heliosSpawn.setDouble("y", getSpawnpointY());
        heliosSpawn.setDouble("z", getSpawnpointZ());
        heliosSpawn.setFloat("yaw", getSpawnpointYaw());
        heliosSpawn.setFloat("pitch", getSpawnpointPitch());
        nbt.set(NBT_BASE, heliosSpawn);
    }

    @Inject(method = "<init>(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V", at = @At("RETURN"))
    public void onConstructUsingNBT(NBTTagCompound nbt, CallbackInfo cb) {
        setSpawnpointX(this.b());
        setSpawnpointY(this.c());
        setSpawnpointZ(this.d());
        setSpawnpointYaw(0);
        setSpawnpointPitch(0);
        if(nbt.hasKeyOfType(NBT_BASE, 10)) {
            NBTTagCompound spawnpoint = nbt.getCompound(NBT_BASE);
            setSpawnpointX(spawnpoint.getDouble("x"));
            setSpawnpointY(spawnpoint.getDouble("y"));
            setSpawnpointZ(spawnpoint.getDouble("z"));
            setSpawnpointYaw(spawnpoint.getFloat("yaw"));
            setSpawnpointPitch(spawnpoint.getFloat("pitch"));
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/server/v1_12_R1/WorldData;)V", at = @At("RETURN"))
    public void onConstructUsingOtherWorldData(WorldData worldData, CallbackInfo cb) {
        HeliosWorldData heliosWorldData = ((HeliosWorldData) worldData);
        setSpawnpointX(heliosWorldData.getSpawnpointX());
        setSpawnpointY(heliosWorldData.getSpawnpointY());
        setSpawnpointZ(heliosWorldData.getSpawnpointZ());
        setSpawnpointYaw(heliosWorldData.getSpawnpointYaw());
        setSpawnpointPitch(heliosWorldData.getSpawnpointPitch());
    }

    /* Cancel spawn setting from internal NMS code */
    @Inject(method = "setSpawn", cancellable = true, at = @At("HEAD"))
    public void onSetSpawn(BlockPosition blockPosition, CallbackInfo cb) {
        new Throwable("Helios: WorldData -> setSpawn(). Please report this to @mikroskeem").printStackTrace();
        cb.cancel();
    }
}
