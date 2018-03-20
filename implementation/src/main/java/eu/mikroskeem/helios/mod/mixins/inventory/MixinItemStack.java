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

import eu.mikroskeem.helios.mod.interfaces.itemstack.HeliosItemStack;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;


/**
 * Mixin to deal with ItemStack NBT stripping
 *
 * @author Mark Vainomaa
 */
@Mixin(value = ItemStack.class, remap = false)
public abstract class MixinItemStack implements HeliosItemStack {
    private final static String helios$HELIOS_STRIPPED = "helios.itemStripped";

    @Shadow public abstract boolean hasTag();
    @Shadow @Nullable public abstract NBTTagCompound getTag();
    @Shadow public abstract void setTag(@Nullable NBTTagCompound nbttagcompound);

    private boolean helios$didCheck = false;
    private boolean helios$isStripped = false;

    @Override
    public boolean isStripped() {
        if(!helios$didCheck && hasTag() && getTag().hasKeyOfType(helios$HELIOS_STRIPPED, 1)) {
            helios$didCheck = true;
            setStrippedFlag(true);
        }
        return helios$isStripped;
    }

    @Override
    public void setStrippedFlag(boolean value) {
        if(getTag() == null) setTag(new NBTTagCompound());
        helios$didCheck = true;
        helios$isStripped = value;
        if(value) {
            getTag().set(helios$HELIOS_STRIPPED, new NBTTagByte((byte)1));
        } else {
            getTag().remove(helios$HELIOS_STRIPPED);
        }
    }
}
