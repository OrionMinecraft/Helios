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

package eu.mikroskeem.helios.api.inventory.meta;

import eu.mikroskeem.helios.api.profile.Profile;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


/**
 * Helios Skull meta exposing GameProfile/textures manipulation features
 *
 * @author Mark Vainomaa
 */
public interface HeliosSkullMeta extends SkullMeta {
    /**
     * Gets Profile associated with given skull. Might be absent if skull does not have textures associated
     *
     * @return Wrapped GameProfile (see {@link Profile})
     */
    @Nullable
    Profile getProfile();

    /**
     * Sets Profile to skull
     *
     * @param profile {@link Profile} instance
     */
    void setProfile(@NotNull Profile profile);

    /**
     * Gets textures associated with given skull. Might be absent
     *
     * @return Textures
     */
    @Nullable
    String getTextures();

    /**
     * Sets new textures to skull with new GameProfile
     *
     * @param textures Textures
     * @param ownerUUID GameProfile owner UUID
     */
    void setTextures(@Nullable String textures, UUID ownerUUID);

    /**
     * Sets new textures to skull with new GameProfile. GameProfile owner UUID is picked randomly
     *
     * @param textures Textures
     */
    void setTextures(@Nullable String textures);
}
