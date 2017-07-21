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

package eu.mikroskeem.helios.mod.plugin;

import co.aikar.timings.TimedEventExecutor;
import eu.mikroskeem.shuriken.common.Ensure;
import eu.mikroskeem.shuriken.reflect.ClassWrapper;
import eu.mikroskeem.shuriken.reflect.Reflect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;


/**
 * @author Mark Vainomaa
 */
public final class HeliosPluginLoader implements PluginLoader {
    private final static Logger logger = LogManager.getLogger("Helios Plugin Loader");
    public final static Map<Class<? extends HeliosPluginBase>, HeliosPluginDescription> HELIOS_PLUGINS = new HashMap<>();
    public static HeliosPluginLoader INSTANCE;

    private final Map<String, HeliosPluginBase> loadedPlugins = new HashMap<>();
    private final Server server;

    public HeliosPluginLoader(Server server) {
        INSTANCE = this;
        this.server = server;
        logger.info("Loading all Helios plugins...");

        HELIOS_PLUGINS.forEach((pluginClass, desc) -> {
            ClassWrapper<? extends HeliosPluginBase> clazz = Reflect.wrapClass(pluginClass).construct();
            loadedPlugins.put(desc.getName(), clazz.getClassInstance());
            clazz.getClassInstance().onLoad();
        });
        loadedPlugins.values().forEach(p -> p.setEnabled(true));

        logger.info("Done!");
    }

    @Override
    public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException {
        throw new IllegalStateException("HeliosPluginLoader does not support PluginLoader#loadPlugin(File)");
    }

    @Override
    public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
        throw new IllegalStateException("HeliosPluginLoader does not support " +
                "PluginLoader#getPluginDescription(File)");
    }

    @Override
    public Pattern[] getPluginFileFilters() {
        return new Pattern[0];
    }

    @Override
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, final Plugin plugin) {
        Ensure.notNull(plugin, "Plugin can not be null");
        Ensure.notNull(listener, "Listener can not be null");

        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
            methods.addAll(Arrays.asList(publicMethods));
            methods.addAll(Arrays.asList(privateMethods));
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().severe("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
            return ret;
        }

        for (final Method method : methods) {
            if (method.isBridge() || method.isSynthetic()) continue;
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                plugin.getLogger().severe(plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = ret.computeIfAbsent(eventClass, k -> new HashSet<>());

            EventExecutor executor = new TimedEventExecutor(EventExecutor.create(method, eventClass), plugin, method, eventClass);
            eventSet.add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
        }
        return ret;
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        loadedPlugins.computeIfPresent(plugin.getName(), (n, p) -> {
            if(!p.isEnabled())
                p.onEnable();
            return p;
        });
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        loadedPlugins.computeIfPresent(plugin.getName(), (n, p) -> {
            if(p.isEnabled())
                p.onDisable();
            return p;
        });
    }

    public Map<String, HeliosPluginBase> getLoadedPlugins() {
        return loadedPlugins;
    }
}
