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

package eu.mikroskeem.helios.mod.mixins.world;

import eu.mikroskeem.helios.api.world.World;
import eu.mikroskeem.helios.mod.interfaces.world.HeliosWorldData;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


/**
 * Mixin to modify spawn location setter/getter behaviour
 *
 * @author Mark Vainomaa
 */
@Mixin(value = CraftWorld.class, remap = false)
public abstract class MixinCraftWorld implements World {
    @Shadow @Final private WorldServer world;

    @Override
    public Location getSpawnLocation() {
        return ((HeliosWorldData) this.world.worldData).getSpawnpoint();
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        ((HeliosWorldData) this.world.worldData).setSpawnpoint(location);
        return true;
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        return setSpawnLocation(new Location(null, x, y, z, 0, 0));
    }

    @Override
    public boolean setSpawnLocation(double x, double y, double z, float yaw, float pitch) {
        return setSpawnLocation(new Location(null, x, y, z, yaw, pitch));
    }

    @Override
    public String toString() {
        return "HeliosWorld{name=" + this.getName() + '}';
    }
}
