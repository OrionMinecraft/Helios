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

import eu.mikroskeem.helios.api.plugin.HeliosPluginManager;
import eu.mikroskeem.helios.mod.interfaces.plugin.HeliosJavaPluginLoader;
import eu.mikroskeem.shuriken.common.Ensure;
import eu.mikroskeem.shuriken.common.SneakyThrow;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * @author Mark Vainomaa
 */
@Mixin(value = SimplePluginManager.class, remap = false)
public abstract class MixinSimplePluginManager implements HeliosPluginManager {
    @Shadow @Final private Map<String, Plugin> lookupNames;
    @Shadow @Final private List<Plugin> plugins;
    @Shadow @Final private SimpleCommandMap commandMap;
    @Shadow public abstract void disablePlugin(Plugin plugin);

    @Override
    public void unloadPlugin(@NotNull Plugin plugin){
        Ensure.notNull(plugin, "Plugin should not be null!");
        String pluginName = plugin.getName().toLowerCase(Locale.ENGLISH);

        /* Disable */
        disablePlugin(plugin);

        /* Remove from lists */
        lookupNames.remove(pluginName, plugin);
        plugins.remove(plugin);

        /* Unregister commands */
        new HashMap<>(commandMap.getKnownCommands()).values().stream()
                .filter(c -> c instanceof PluginCommand)
                .map(c -> (PluginCommand) c)
                .filter(c -> c.getPlugin().getName().toLowerCase(Locale.ENGLISH).equals(pluginName))
                .forEach(c -> {
                    c.unregister(commandMap);
                    commandMap.getKnownCommands().remove(c.getName(), c);
                    commandMap.getKnownCommands().remove(pluginName + ":" + c.getName());
                });

        /* Unregister listeners for sure */
        HandlerList.unregisterAll(plugin);

        /* Close classloader */
        ClassLoader classLoader = plugin.getClass().getClassLoader();
        if(plugin.getPluginLoader() instanceof JavaPluginLoader) {
            HeliosJavaPluginLoader jpl = (HeliosJavaPluginLoader) plugin.getPluginLoader();
            PluginClassLoader pcl = (PluginClassLoader) classLoader;
            if(jpl.getLoaders().contains(pcl))
                jpl.getLoaders().remove(pcl);
        }
        try {
            if(classLoader instanceof URLClassLoader) ((URLClassLoader) classLoader).close();
        }
        catch (IOException e) { SneakyThrow.throwException(e); }
    }
}
