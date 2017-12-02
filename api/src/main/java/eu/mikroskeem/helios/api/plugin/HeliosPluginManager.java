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

package eu.mikroskeem.helios.api.plugin;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;


/**
 * Plugin manager with extra functionality
 *
 * @author Mark Vainomaa
 */
public interface HeliosPluginManager extends PluginManager {
    /**
     * Gets map of {@link Plugin} lookup names and instances
     *
     * @return Map of plugin lookup names and instances
     */
    @NotNull
    Map<String, Plugin> getLookupNames();

    /**
     * Gets list of loaded {@link Plugin}s
     *
     * @return List of loaded plugins
     */
    @NotNull
    List<Plugin> getPluginsList();

    /**
     * Gets instance of {@link SimpleCommandMap} associated with this plugin manager
     *
     * @return Instance of {@link SimpleCommandMap}
     */
    @NotNull
    SimpleCommandMap getCommandMap();

    /**
     * Unloads {@link Plugin}
     *
     * @param plugin Plugin to unload
     */
    void unloadPlugin(@NotNull Plugin plugin);
}
