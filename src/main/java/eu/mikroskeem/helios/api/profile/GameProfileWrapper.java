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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


/**
 * Mojang AuthLib's GameProfile wrapper
 *
 * @author Mark Vainomaa
 */
public interface GameProfileWrapper {
    /**
     * Constructs new GameProfile wrapper using {@code name} and/or {@code uuid}. Either of them has to be present
     *
     * @param name Player name
     * @param uuid Player UUID
     * @return Wrapped GameProfile instance
     */
    @NotNull
    @Contract("null, null -> fail")
    Profile newGameProfile(String name, UUID uuid);

    /**
     * Constructs new GameProfile wrapper using {@code uuid}
     *
     * @param uuid Player UUID
     * @return Wrapped GameProfile instance
     */
    @NotNull
    Profile newGameProfile(@NotNull UUID uuid);

    /**
     * Wraps existing Mojang GameProfile
     *
     * @param mojangGameProfile Mojang GameProfile object
     * @return Wrapped GameProfile instance
     */
    @NotNull
    Profile wrapGameProfile(@NotNull Object mojangGameProfile);

    /**
     * Constructs new GameProfile's Property wrapper
     *
     * @param name Property name
     * @param value Property value
     * @param signature Property signature. Might be absent
     * @return Wrapped Property instance
     */
    @NotNull
    Property newProperty(@NotNull String name, @NotNull String value, @Nullable String signature);
}
