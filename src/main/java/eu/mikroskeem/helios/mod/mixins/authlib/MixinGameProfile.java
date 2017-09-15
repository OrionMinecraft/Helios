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

package eu.mikroskeem.helios.mod.mixins.authlib;

import com.google.common.collect.Multimap;
import com.mojang.authlib.properties.PropertyMap;
import eu.mikroskeem.helios.api.profile.Profile;
import eu.mikroskeem.helios.api.profile.Property;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


/**
 * Makes Authlib GameProfile to implement Helios Profile
 * @author Mark Vainomaa
 */
@Mixin(value = com.mojang.authlib.GameProfile.class, remap = false)
@Implements(@Interface(iface = Profile.class, prefix = "heliosProfile$"))
public abstract class MixinGameProfile {
    @Shadow @Final private PropertyMap properties;

    @SuppressWarnings("unchecked")
    @NotNull
    public Multimap<String, Property> heliosProfile$getProperties() {
        return (Multimap<String, Property>) (Object) properties;
    }

    @NotNull
    public Object heliosProfile$getWrappedGameProfile() {
        return this;
    }
}