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

package eu.mikroskeem.helios.api.profile;

import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


/**
 * A Mojang GameProfile wrapper
 *
 * @author Mark Vainomaa
 */
public interface Profile {
    /* *
     * Constructs new wrapped GameProfile
     *
     * @param name Player name. May be null if {@code uuid} is set
     * @param uuid Player UUID. May be null if {@code name} is set
     * @return Wrapped GameProfile (instance of {@link Profile})
     * /
    @Contract("null, null -> fail")
    Profile newProfile(String name, UUID uuid);
    */

    /**
     * Gets profile name
     *
     * @return Profile name
     */
    @Nullable
    String getName();

    /**
     * Gets profile UUID
     *
     * @return Profile UUID
     */
    @Nullable
    UUID getUUID();

    /**
     * Gets whether profile is legacy or not
     *
     * @return Legacy or not
     */
    boolean isLegacy();

    /**
     * Gets GameProfile's properties
     *
     * @return GameProfile properties
     */
    @NotNull
    Multimap<String, Property> getProperties();

    /**
     * Gets wrapped GameProfile object
     *
     * @return GameProfile object
     */
    @NotNull
    Object getWrappedGameProfile();
}
