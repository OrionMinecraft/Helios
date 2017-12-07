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

package eu.mikroskeem.helios.mod.mixins.inventory.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import eu.mikroskeem.helios.api.inventory.meta.HeliosSkullMeta;
import eu.mikroskeem.helios.api.profile.Profile;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static eu.mikroskeem.helios.mod.helpers.ProfileConversion.convertProfile;


/**
 * @author Mark Vainomaa
 */
@Mixin(targets = "org/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaSkull", remap = false)
public abstract class MixinCraftSkullMeta implements HeliosSkullMeta {
    @Shadow private GameProfile profile;

    @Nullable
    @Override
    public Profile getProfile() {
        return this.profile != null ? (Profile) profile : null;
    }

    @Override
    public void setProfile(@Nullable Profile profile) {
        this.profile = (profile != null ? convertProfile(profile) : null);
    }

    @Nullable
    @Override
    public String getTextures() {
        /* Profile is null on skulls which do not have "SkullOwner" set */
        if(this.profile == null)
            return null;

        if(!profile.getProperties().containsKey("textures"))
            return null;

        Property textures = helios$getFirst(profile.getProperties().get("textures"));
        if(textures == null)
            return null;

        return textures.getValue();
    }

    @Override
    public void setTextures(@Nullable String texture, UUID ownerUUID) {
        GameProfile profile = this.profile != null ? this.profile : new GameProfile(ownerUUID, null);
        if(profile.getProperties().containsKey("textures"))
            profile.getProperties().removeAll("textures");

        if(texture == null)
            return;

        profile.getProperties().put("textures", new Property("textures", texture));
    }

    @Override
    public void setTextures(@Nullable String texture) {
        setTextures(texture, UUID.randomUUID());
    }

    @Nullable
    private <T> T helios$getFirst(Collection<T> collection) {
        if(collection instanceof List)
            return ((List<T>) collection).get(0);

        Iterator<T> itr = collection.iterator();
        return itr.hasNext() ? itr.next() : null;
    }
}
