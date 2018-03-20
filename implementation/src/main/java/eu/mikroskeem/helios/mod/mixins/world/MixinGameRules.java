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

package eu.mikroskeem.helios.mod.mixins.world;

import eu.mikroskeem.helios.mod.HeliosMod;
import net.minecraft.server.v1_12_R1.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

/**
 * @author Mark Vainomaa
 */
@Mixin(value = GameRules.class, remap = false)
public abstract class MixinGameRules {
    @Shadow public abstract void a(String s, String s1, GameRules.EnumGameRuleType enumGameRuleType); // MCP - addGameRule

    private final static String helios$ADD_GAME_RULE = "Lnet/minecraft/server/v1_12_R1/GameRules;" +
            "a(Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/server/v1_12_R1/GameRules$EnumGameRuleType;)V";

    @Redirect(method = "<init>()V", at = @At(value = "INVOKE", target = helios$ADD_GAME_RULE))
    private void addGameRule(GameRules $this, String ruleName, String defaultValue, GameRules.EnumGameRuleType type) {
        $this.a(ruleName, helios$getGamerules().getOrDefault(ruleName, defaultValue), type);
    }

    private Map<String, String> helios$getGamerules() {
        return HeliosMod.INSTANCE.getConfiguration().getWorld().getDefaultGamerules();
    }
}
