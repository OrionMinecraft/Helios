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

package eu.mikroskeem.helios.mod.helpers

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.Multiset
import com.mojang.authlib.GameProfile
import eu.mikroskeem.helios.api.profile.GameProfileWrapper
import eu.mikroskeem.helios.api.profile.Profile
import eu.mikroskeem.helios.api.profile.Property
import java.security.PublicKey
import java.util.UUID

/**
 * @author Mark Vainomaa
 */
object HeliosGameProfileWrapper: GameProfileWrapper {
    override fun newGameProfile(name: String?, uuid: UUID?) = HeliosGameProfile(name, uuid)
    override fun newGameProfile(uuid: UUID) = HeliosGameProfile(null, uuid)
    override fun wrapGameProfile(mojangGameProfile: Any) = HeliosGameProfile(mojangGameProfile as GameProfile)

    override fun newProperty(name: String, value: String, signature: String?): Property =
            HeliosWrappedProperty(com.mojang.authlib.properties.Property(name, value, signature))
}

class HeliosGameProfile: Profile {
    private val gameProfile: GameProfile

    constructor(name: String? = null, uuid: UUID? = UUID.randomUUID()) {
        gameProfile = GameProfile(uuid, name)
    }

    constructor(gameProfile: GameProfile) {
        this.gameProfile = gameProfile
    }

    override fun getName(): String? = gameProfile.name
    override fun getUUID(): UUID? = gameProfile.id
    override fun isLegacy(): Boolean = gameProfile.isLegacy
    override fun getProperties(): Multimap<String, Property> = HeliosPropertyMap(gameProfile)

    override fun getWrappedGameProfile(): Any = gameProfile

    override fun hashCode(): Int = gameProfile.hashCode()
    override fun equals(other: Any?): Boolean = gameProfile == other
    override fun toString(): String = "HeliosGameProfile{name=$name, id=$uuid, legacy=$isLegacy, properties=$properties, " +
            "wrappedGameProfile=$gameProfile}"
}

class HeliosPropertyMap(gameProfile: GameProfile): Multimap<String, Property> {
    private val delegate = gameProfile.properties

    // Whoops, inefficient!
    private fun newEntry(key: String, entryValue: Property): MutableMap.MutableEntry<String, Property> {
        return object: MutableMap.MutableEntry<String, Property> {
            override val key: String = key
            override var value: Property = entryValue
            override fun setValue(newValue: Property): Property {
                value = newValue
                return newValue
            }
        }
    }

    override fun entries(): MutableCollection<MutableMap.MutableEntry<String, Property>> =
        delegate.entries().map { (k, v) -> newEntry(k, v.wrap()) }.toCollection(ArrayList())

    override fun putAll(key: String?, values: MutableIterable<Property>?): Boolean =
            delegate.putAll(key, values?.unwrap())

    override fun putAll(multimap: Multimap<out String, out Property>?): Boolean {
        val wrappedMap = multimap?.run {
            val wrappedMap: Multimap<String, com.mojang.authlib.properties.Property> = HashMultimap.create()
            multimap.asMap().map { (k, v) -> Pair(k, v.unwrap()) }.forEach { (k, v) -> wrappedMap.putAll(k, v) }
            return@run wrappedMap
        }
        return delegate.putAll(wrappedMap)
    }

    override fun asMap(): MutableMap<String, MutableCollection<Property>> =
        mutableMapOf(*delegate.asMap().map { (k, v) -> Pair(k, v.wrap()) }.toTypedArray())

    override fun replaceValues(key: String?, values: MutableIterable<Property>?): MutableCollection<Property> =
        delegate.replaceValues(key, values?.unwrap()).wrap()

    override fun size(): Int = delegate.size()
    override fun removeAll(key: Any?): MutableCollection<Property> = delegate.removeAll(key).wrap()
    override fun keys(): Multiset<String> = delegate.keys()
    override fun keySet(): MutableSet<String> = delegate.keySet()
    override fun clear() = delegate.clear()
    override fun isEmpty(): Boolean = delegate.isEmpty
    override fun remove(key: Any?, value: Any?): Boolean = delegate.remove(key, value)
    override fun containsKey(key: Any?): Boolean = delegate.containsKey(key)
    override fun put(key: String?, value: Property?): Boolean = delegate.put(key, value?.unwrap())
    override fun containsValue(value: Any?): Boolean = delegate.containsValue(value)
    override fun containsEntry(key: Any?, value: Any?): Boolean = delegate.containsEntry(key, value)
    override fun get(key: String?): MutableCollection<Property> = delegate.get(key).wrap()
    override fun values(): MutableCollection<Property> = delegate.values().wrap()
}

class HeliosWrappedProperty(private val wrappedProperty: com.mojang.authlib.properties.Property): Property {
    override fun getName(): String = wrappedProperty.name
    override fun getValue(): String = wrappedProperty.value
    override fun getSignature(): String? = wrappedProperty.signature
    override fun isSignatureValid(publicKey: PublicKey): Boolean = wrappedProperty.isSignatureValid(publicKey)

    override fun getWrappedProperty(): Any = wrappedProperty

    override fun hashCode(): Int = wrappedProperty.hashCode()
    override fun equals(other: Any?): Boolean = wrappedProperty == other
    override fun toString(): String = "WrappedProperty{name=$name, value=$value, signature=$signature, " +
            "wrappedProperty=$wrappedProperty"
}

fun com.mojang.authlib.properties.Property.wrap() = HeliosWrappedProperty(this)
fun Property.unwrap() = this.wrappedProperty as com.mojang.authlib.properties.Property
fun MutableIterable<com.mojang.authlib.properties.Property>.wrap(): ArrayList<Property> = this.map { it.wrap() }.toCollection(ArrayList())
fun MutableIterable<Property>.unwrap(): ArrayList<com.mojang.authlib.properties.Property> = this.map { it.unwrap() }.toCollection(ArrayList())