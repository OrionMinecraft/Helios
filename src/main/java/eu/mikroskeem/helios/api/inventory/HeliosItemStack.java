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

package eu.mikroskeem.helios.api.inventory;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;


/**
 * Missing & extra ItemStack methods
 *
 * @author Mark Vainomaa
 */
public interface HeliosItemStack {
    /**
     * Sets list of material what given tool can break
     *
     * @param materials List of materials. Use null to clear
     */
    void setCanDestroy(@Nullable Collection<Material> materials);

    /**
     * Gets list of material what given tool can break
     *
     * @return List of materials
     */
    @NotNull Collection<Material> getCanDestroy();

    /**
     * Sets list of material where given block can be placed
     *
     * @param materials List of materials. Use null to clear
     */
    void setCanPlaceOn(@Nullable Collection<Material> materials);

    /**
     * Gets list of material where given block can be placed
     *
     * @return List of materials
     */
    @NotNull Collection<Material> getCanPlaceOn();
}
