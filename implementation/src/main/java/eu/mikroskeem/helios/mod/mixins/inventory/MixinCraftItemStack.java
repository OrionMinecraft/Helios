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

package eu.mikroskeem.helios.mod.mixins.inventory;

import eu.mikroskeem.helios.api.inventory.HeliosItemStack;
import eu.mikroskeem.helios.mod.delegate.inventory.HeliosItemStackDelegate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;


/**
 * Mixin to implement methods in {@link HeliosItemStack}
 *
 * @author Mark Vainomaa
 */
@Mixin(value = CraftItemStack.class, remap = false)
public abstract class MixinCraftItemStack implements HeliosItemStack {
    @Shadow net.minecraft.server.v1_12_R1.ItemStack handle;

    @Override
    public void setCanDestroy(@Nullable Collection<Material> materials) {
        HeliosItemStackDelegate.setCanDestroy(handle, materials);
    }

    @Override
    public @NotNull Collection<Material> getCanDestroy() {
        return HeliosItemStackDelegate.getCanDestroy(handle);
    }

    @Override
    public void setCanPlaceOn(@Nullable Collection<Material> materials) {
        HeliosItemStackDelegate.setCanPlaceOn(handle, materials);
    }

    @Override
    public @NotNull Collection<Material> getCanPlaceOn() {
        return HeliosItemStackDelegate.getCanPlaceOn(handle);
    }
}
