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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.PublicKey;


/**
 * Mojang GameProfile Property wrapper
 *
 * @author Mark Vainomaa
 */
public interface Property {
    /**
     * Gets property name
     *
     * @return Property name
     */
    @NotNull
    String getName();

    /**
     * Gets property value
     *
     * @return Property value
     */
    @NotNull
    String getValue();

    /**
     * Gets property signature. Might be absent
     *
     * @return Property signature
     */
    @Nullable
    String getSignature();

    /**
     * Returns whether Property has a signature or not
     *
     * @return Whether Property has a signature or not
     */
    default boolean hasSignature() {
        return getSignature() != null;
    }

    /**
     * Returns whether Property has valid signature (against {@code publicKey})
     *
     * @param publicKey Public key to check signature against
     * @return Wheter Property has valid signature or not
     */
    boolean isSignatureValid(@NotNull PublicKey publicKey);

    /**
     * Gets wrapped GameProfile Property object
     *
     * @return GameProfile Property object
     */
    @NotNull
    Object getWrappedProperty();
}
