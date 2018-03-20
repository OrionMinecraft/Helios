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

package eu.mikroskeem.helios.api.entity;

import eu.mikroskeem.helios.api.profile.Profile;
import org.jetbrains.annotations.Contract;


/**
 * Helios Player
 *
 * @author Mark Vainomaa
 */
public interface Player extends org.bukkit.entity.Player {
    /**
     * Gets the last time when player was active
     *
     * @return Seconds from Unix epoch
     */
    long getLastActiveTime();

    /**
     * Shows if player is away or not, value is set by comparing
     * value defined in mod's configuration
     *
     * @return Whether player is away or not
     */
    boolean isAway();

    /**
     * Sends an JSON message to player. Use <pre>net.kyori:text</pre> or similiar library for that
     *
     * @param jsonMessage JSON message
     */
    @Contract("null -> fail")
    void sendJsonMessage(String jsonMessage);

    /**
     * Returns {@link Profile} associated with given player
     *
     * @return {@link Profile} instance
     */
    Profile getGameProfile();
}
