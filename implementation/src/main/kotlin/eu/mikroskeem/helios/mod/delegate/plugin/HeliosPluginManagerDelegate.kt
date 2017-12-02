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

package eu.mikroskeem.helios.mod.delegate.plugin

import eu.mikroskeem.helios.api.events.plugin.PluginUnloadEvent
import eu.mikroskeem.helios.api.plugin.HeliosPluginManager
import eu.mikroskeem.helios.mod.interfaces.plugin.HeliosJavaPluginLoader
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.PluginClassLoader
import java.net.URLClassLoader
import java.util.*

/**
 * Helios Plugin Manager delegate
 *
 * @author Mark Vainomaa
 */
object HeliosPluginManagerDelegate {
    @JvmStatic
    fun unloadPlugin(pluginManager: HeliosPluginManager, plugin: Plugin) {
        // Get lowercase version of plugin name
        val pluginName = plugin.name.toLowerCase(Locale.ENGLISH)

        // Disable plugin
        pluginManager.disablePlugin(plugin)

        // Fire plugin unload event
        pluginManager.callEvent(PluginUnloadEvent(plugin))

        // Remove plugin names from lists
        pluginManager.lookupNames.remove(pluginName, plugin)
        pluginManager.pluginsList.remove(plugin)

        // Unregister commands
        ArrayList(pluginManager.commandMap.knownCommands.values)
                .filter { it is PluginCommand }.map { it as PluginCommand }
                .filter { it.plugin.name.toLowerCase() == pluginName }
                .forEach {
                    it.unregister(pluginManager.commandMap)
                    pluginManager.commandMap.knownCommands.remove(it.name, it)
                    pluginManager.commandMap.knownCommands.remove("$pluginName:${it.name}", it)
                }

        // Remove plugin loader
        val classLoader = plugin.javaClass.classLoader
        val pluginLoader = plugin.pluginLoader
        if(pluginLoader is HeliosJavaPluginLoader && classLoader is PluginClassLoader)
            pluginLoader.loaders.remove(classLoader)

        // Close classloader
        (classLoader as? URLClassLoader)?.close()
    }
}