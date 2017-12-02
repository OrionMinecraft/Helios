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
import net.minecraft.server.v1_12_R1.WorldData;
import org.spongepowered.asm.mixin.Mixin;


/**
 * Mixin to add new fields to WorldData class
 *
 * @author Mark Vainomaa
 */
@Mixin(value = WorldData.class, remap = false)
public abstract class MixinWorldDataAccessors implements HeliosWorldData {
    private double spawnpointX;
    private double spawnpointY;
    private double spawnpointZ;
    private float spawnpointYaw;
    private float spawnpointPitch;

    @Override
    public double getSpawnpointX() {
        return spawnpointX;
    }

    @Override
    public double getSpawnpointY() {
        return spawnpointY;
    }

    @Override
    public double getSpawnpointZ() {
        return spawnpointZ;
    }

    @Override
    public float getSpawnpointYaw() {
        return spawnpointYaw;
    }

    @Override
    public float getSpawnpointPitch() {
        return spawnpointPitch;
    }

    @Override
    public void setSpawnpointX(double spawnpointX) {
        this.spawnpointX = spawnpointX;
    }

    @Override
    public void setSpawnpointY(double spawnpointY) {
        this.spawnpointY = spawnpointY;
    }

    @Override
    public void setSpawnpointZ(double spawnpointZ) {
        this.spawnpointZ = spawnpointZ;
    }

    @Override
    public void setSpawnpointYaw(float spawnpointYaw) {
        this.spawnpointYaw = spawnpointYaw;
    }

    @Override
    public void setSpawnpointPitch(float spawnpointPitch) {
        this.spawnpointPitch = spawnpointPitch;
    }
}
