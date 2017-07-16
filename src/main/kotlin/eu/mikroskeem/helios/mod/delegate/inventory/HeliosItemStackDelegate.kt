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

package eu.mikroskeem.helios.mod.delegate.inventory

import net.minecraft.server.v1_12_R1.*
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
    fun setCanDestroy(handle: ItemStack, materials: Collection<Material>?) =
            setMaterialsTag(handle, CAN_DESTROY, materials)

    @JvmStatic
    fun setCanPlaceOn(handle: ItemStack, materials: Collection<Material>?) =
            setMaterialsTag(handle, CAN_PLACE_ON, materials)

    @JvmStatic
    fun getCanDestroy(handle: ItemStack): Collection<Material> =
            getMaterialsTag(handle, CAN_DESTROY)

    @JvmStatic
    fun getCanPlaceOn(handle: ItemStack): Collection<Material> =
            getMaterialsTag(handle, CAN_PLACE_ON)

    // Common code to set materials into NBT tag
    private fun setMaterialsTag(handle: ItemStack, tagName: String, materials: Collection<Material>?) {
        // Create new tag only if materials collection is not null
        val tag = handle.tag ?: if(materials != null) NBTTagCompound() else return

        if(materials != null) {
            tag.set(tagName, NBTTagList().let {
                materials.map(this@HeliosItemStackDelegate::getItemId)
                        .map { NBTTagString(it) }
                        .forEach(it::add)
                return@let it
            })
        } else {
            tag.getList(tagName, 8)?.run {
                tag.remove(tagName)
            }
        }

        // Set tag
        handle.tag = tag
    }

    // Common code to read materials from NBT tag
    private fun getMaterialsTag(handle: ItemStack, tagName: String): Collection<Material> {
        handle.tag?.run {
            this.getList(tagName, 8)?.takeIf { !it.isEmpty }?.run {
                return convertItemsToSet(this.list)
            }
        }
        return emptySet()
    }

    // Material.DIAMOND_ORE -> minecraft:diamond_ore
    private fun getItemId(material: Material): String =
        Block.REGISTRY.b(CraftMagicNumbers.getBlock(material)).toString()

    // NBTList[minecraft:diamond_ore, ...] -> Set[Material.DIAMOND_ORE, ...]
    private fun convertItemsToSet(nbtStringList: List<NBTBase>): Set<Material> =
        nbtStringList.map { it as NBTTagString }
                .map(NBTTagString::c_)
                .map(CraftMagicNumbers.INSTANCE::getMaterialFromInternalName)
                .toSet()
}