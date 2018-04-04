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

package eu.mikroskeem.helios.api.world;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;


/**
 * Helios World wrapper
 *
 * @author Mark Vainomaa
 */
public interface World extends org.bukkit.World {
    /**
     * {@inheritDoc}
     * @deprecated Use {@link World#setSpawnLocation(Location)} or
     *             {@link World#setSpawnLocation(double, double, double, float, float)}
     */
    @Override
    @Deprecated
    boolean setSpawnLocation(int x, int y, int z);

    /**
     * Set world spawn location
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param yaw Yaw
     * @param pitch Pitch
     * @return True, if spawn location was successfully set
     */
    boolean setSpawnLocation(double x, double y, double z, float yaw, float pitch);

    /**
     * Set world spawn location
     *
     * @param location Location object
     * @return True, if spawn location was successfully set
     * @see org.bukkit.World
     */
    boolean setSpawnLocation(@NotNull Location location);
}
