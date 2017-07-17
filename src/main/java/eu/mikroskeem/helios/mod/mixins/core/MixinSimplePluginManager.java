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

package eu.mikroskeem.helios.mod.mixins.core;

import eu.mikroskeem.helios.api.events.plugin.PluginLoadEvent;
import eu.mikroskeem.helios.api.plugin.HeliosPluginManager;
import eu.mikroskeem.helios.mod.delegate.plugin.HeliosPluginManagerDelegate;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Mixin to fire plugin load/unload events and implement unload method
 *
 * @author Mark Vainomaa
 */
@Mixin(value = SimplePluginManager.class, remap = false)
public abstract class MixinSimplePluginManager implements HeliosPluginManager {
    @NotNull @Override @Accessor public abstract Map<String, Plugin> getLookupNames();
    @NotNull @Override @Accessor("plugins") public abstract List<Plugin> getPluginsList();
    @NotNull @Override @Accessor public abstract SimpleCommandMap getCommandMap();

    @Override
    public void unloadPlugin(@NotNull Plugin plugin){
        HeliosPluginManagerDelegate.unloadPlugin(this, plugin);
    }

    @Inject(method = "loadPlugins", at = @At(
            value = "RETURN"
    ))
    public void onLoadPlugins(File directory, CallbackInfoReturnable<Plugin[]> cir) {
        Arrays.stream(cir.getReturnValue()).forEach(p -> callEvent(new PluginLoadEvent(p)));
    }

    @Inject(method = "loadPlugin", at = @At(
            value = "RETURN"
    ))
    public void onLoadPlugin(File file, CallbackInfoReturnable<Plugin> cir) {
        callEvent(new PluginLoadEvent(cir.getReturnValue()));
    }
}
