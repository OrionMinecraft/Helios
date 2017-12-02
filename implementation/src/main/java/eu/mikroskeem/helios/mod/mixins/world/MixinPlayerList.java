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
import net.minecraft.server.v1_12_R1.PlayerList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to inject more precise spawnpoint into game
 *
 * @author Mark Vainomaa
 */
@Mixin(value = PlayerList.class, remap = false)
public abstract class MixinPlayerList {
    private final static String helios$MOVE_TO_WORLD = "moveToWorld(Lnet/minecraft/server/v1_12_R1/EntityPlayer;IZ" +
            "Lorg/bukkit/Location;Z)Lnet/minecraft/server/v1_12_R1/EntityPlayer;";

    @Redirect(method = helios$MOVE_TO_WORLD,
            at = @At(value = "NEW",
                    target = "(Lorg/bukkit/World;DDD)Lorg/bukkit/Location;",
                    ordinal = 1,
                    args = "class=org/bukkit/Location"
            ))
    private Location newLocation(World world, double x, double y, double z) {
        return ((HeliosWorldData) ((CraftWorld) world).getHandle().worldData).getSpawnpoint();
    }
}
