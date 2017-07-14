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

package eu.mikroskeem.helios.mod.mixins.inventory;

import eu.mikroskeem.helios.api.inventory.HeliosItemStack;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.*;
import java.util.stream.Collectors;


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
        /* Get tag */
        NBTTagCompound tag;
        if((tag = handle.getTag()) == null) {
            /* Do not create new tag, if materials is null */
            if(materials == null) return;

            /* Create new tag */
            tag = new NBTTagCompound();
        }

        /* Set 'CanDestroy' list */
        if(materials != null) {
            NBTTagList canDestroy = new NBTTagList();
            new HashSet<>(materials).stream()
                    .map(this::helios$getItemId)
                    .map(NBTTagString::new)
                    .forEach(canDestroy::add);
            tag.set("CanDestroy", canDestroy);
        } else {
            if(tag.getList("CanDestroy", (byte) 8) != null)
                tag.remove("CanDestroy");
        }

        /* Apply tag */
        handle.setTag(tag);
    }

    @Override
    public @NotNull Collection<Material> getCanDestroy() {
        /* Get tag, or return empty set */
        NBTTagCompound tag;
        if((tag = handle.getTag()) == null)
            return Collections.emptySet();

        /* Try to get list 'canDestroy' */
        NBTTagList canDestroy = tag.getList("CanDestroy", (byte) 8);
        if(canDestroy == null || canDestroy.isEmpty())
            return Collections.emptySet();

        /* Convert NBT String array */
        return helios$convertItemsToSet(canDestroy.list);
    }

    @Override
    public void setCanPlaceOn(@Nullable Collection<Material> materials) {
        /* Get tag */
        NBTTagCompound tag;
        if((tag = handle.getTag()) == null) {
            /* Do not create new tag, if materials is null */
            if(materials == null) return;

            /* Create new tag */
            tag = new NBTTagCompound();
        }

        if(materials != null) {
            NBTTagList canPlaceOn = new NBTTagList();
            new HashSet<>(materials).stream()
                    .map(this::helios$getItemId)
                    .map(NBTTagString::new)
                    .forEach(canPlaceOn::add);
            tag.set("CanPlaceOn", canPlaceOn);
        } else {
            if(tag.getList("CanPlaceOn", (byte) 8) != null)
                tag.remove("CanPlaceOn");
        }

        /* Apply tag */
        handle.setTag(tag);
    }

    @Override
    public @NotNull Collection<Material> getCanPlaceOn() {
        /* Get tag, or return empty set */
        NBTTagCompound tag;
        if((tag = handle.getTag()) == null)
            return Collections.emptySet();

        /* Try to get list 'CanPlaceOn' */
        NBTTagList canPlaceOn = tag.getList("CanPlaceOn", (byte) 8);
        if(canPlaceOn == null || canPlaceOn.isEmpty())
            return Collections.emptySet();

        /* Convert NBT String array */
        return helios$convertItemsToSet(canPlaceOn.list);
    }

    /* Material.DIAMOND_ORE -> minecraft:diamond_ore */
    private String helios$getItemId(Material material) {
        return Block.REGISTRY.b(CraftMagicNumbers.getBlock(material)).toString();
    }

    private Set<Material> helios$convertItemsToSet(List<NBTBase> nbtStringList) {
        return nbtStringList.stream()
                .map(nbtBase -> (NBTTagString) nbtBase)
                .map(NBTTagString::c_)
                .map(CraftMagicNumbers.INSTANCE::getMaterialFromInternalName)
                .collect(Collectors.toSet());
    }
}
