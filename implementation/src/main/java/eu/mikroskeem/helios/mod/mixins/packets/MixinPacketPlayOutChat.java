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

package eu.mikroskeem.helios.mod.mixins.packets;

import eu.mikroskeem.shuriken.common.Ensure;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


/**
 * Adds outgoing chat packet serialized length limit check
 *
 * @author Mark Vainomaa
 */
@Mixin(value = PacketPlayOutChat.class, remap = false)
public abstract class MixinPacketPlayOutChat {
    private final static String helios$SERIALIZE = "b(Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;)V";
    private final static String helios$SERIALIZE_STRING = "Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;" +
            "a(Ljava/lang/String;)Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;";

    @Redirect(method = helios$SERIALIZE, at = @At(
            value = "INVOKE",
            target = helios$SERIALIZE_STRING
    ))
    private PacketDataSerializer serializeString(PacketDataSerializer serializer, String raw) {
        Ensure.ensureCondition(raw.length() <= 32767, "Serialized chat string is too large! (Not " +
                raw.length() + " <= 32767!)");
        return serializer.a(raw);
    }
}
