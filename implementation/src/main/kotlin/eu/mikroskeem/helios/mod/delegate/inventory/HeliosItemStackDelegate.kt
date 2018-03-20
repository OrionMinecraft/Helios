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

package eu.mikroskeem.helios.mod.delegate.inventory

import net.minecraft.server.v1_12_R1.Block
import net.minecraft.server.v1_12_R1.ItemStack
import net.minecraft.server.v1_12_R1.NBTBase
import net.minecraft.server.v1_12_R1.NBTTagCompound
import net.minecraft.server.v1_12_R1.NBTTagList
import net.minecraft.server.v1_12_R1.NBTTagString
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers

/**
 * CraftItemStack Mixin methods
 *
 * @author Mark Vainomaa
 */
private const val CAN_DESTROY = "CanDestroy"
private const val CAN_PLACE_ON = "CanPlaceOn"

object HeliosItemStackDelegate {
    @JvmStatic
    fun ItemStack.setCanDestroy( materials: Collection<Material>?) = setMaterialsTag(CAN_DESTROY, materials)

    @JvmStatic
    fun ItemStack.setCanPlaceOn(materials: Collection<Material>?) = setMaterialsTag(CAN_PLACE_ON, materials)

    @JvmStatic
    fun ItemStack.getCanDestroy(): Collection<Material> = getMaterialsTag(CAN_DESTROY)

    @JvmStatic
    fun ItemStack.getCanPlaceOn(): Collection<Material> = getMaterialsTag(CAN_PLACE_ON)

    @JvmStatic
    fun setLore(handle: ItemStack, vararg lore: String): ItemStack =
        handle.apply {
            this.tag = NBTTagCompound().apply {
                set("display", NBTTagCompound().apply {
                    set("Lore", NBTTagList().apply {
                        lore.forEach { add(it.convertToTag()) }
                    })
                })
            }
        }

    @JvmStatic
    fun ItemStack.appendLore(vararg lore: String): ItemStack = apply {
        val loreList = this.tag?.getCompound("display")?.getList("Lore", 8) ?: return setLore(this, *lore)
        lore.forEach { loreList.add(it.convertToTag()) }
    }


    // Common code to set materials into NBT tag
    private fun ItemStack.setMaterialsTag(tagName: String, materials: Collection<Material>?) {
        // Create new tag only if materials collection is not null
        val nbtTag = tag ?: materials?.run { NBTTagCompound() } ?: return

        if(materials != null) {
            nbtTag.set(tagName, NBTTagList().apply {
                materials.map { NBTTagString(it.getItemId()) }.forEach(::add)
            })
        } else {
            nbtTag.getList(tagName, 8)?.run {
                nbtTag.remove(tagName)
            }
        }

        // Set tag back
        tag = nbtTag
    }

    // Common code to read materials from NBT tag
    private fun ItemStack.getMaterialsTag(tagName: String): Collection<Material> {
        tag?.run {
            this.getList(tagName, 8)?.takeIf { !it.isEmpty }?.run {
                list.convertItemsToSet()
            }
        }
        return emptySet()
    }

    // Material.DIAMOND_ORE -> minecraft:diamond_ore
    private fun Material.getItemId(): String = Block.REGISTRY.b(CraftMagicNumbers.getBlock(this)).toString()

    // NBTList[minecraft:diamond_ore, ...] -> Set[Material.DIAMOND_ORE, ...]
    private fun List<NBTBase>.convertItemsToSet(): Set<Material> = map { it as NBTTagString }
                .map(NBTTagString::c_)
                .map(CraftMagicNumbers.INSTANCE::getMaterialFromInternalName)
                .toSet()

    // String -> NBTTagString
    private fun String.convertToTag(): NBTTagString = NBTTagString(this)
}