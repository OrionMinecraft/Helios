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

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


/**
 * @author Mark Vainomaa
 */
public abstract class HeliosPluginBase extends PluginBase {
    private boolean enabled = false;
    Logger logger;

    public HeliosPluginBase() {
        logger = new Logger(this.getClass().getCanonicalName(), null) {
            private final String prefix = "[" + getDescription().getName() + "] ";

            @Override
            public void log(LogRecord record) {
                record.setMessage(prefix + record.getMessage());
                super.log(record);
            }
        };

        logger.setParent(getServer().getLogger());
        logger.setLevel(Level.ALL);
    }

    /* ** Methods needed to override ** */
    @Override public void onDisable() {}
    @Override public void onLoad() {}
    @Override public void onEnable() {}
    /* ** Methods needed to override ** END */

    public void setEnabled(boolean value) {
        this.enabled = value;
        if(value)
            onEnable();
        else
            onDisable();
    }

    @Override public File getDataFolder() { return null; }
    @Override public PluginDescriptionFile getDescription() { return HeliosPluginLoader.HELIOS_PLUGINS.get(this.getClass()).toPluginDescriptionFile(); }
    @Override public FileConfiguration getConfig() { return null; }
    @Override public InputStream getResource(String filename) { return this.getClass().getClassLoader().getResourceAsStream(filename); }
    @Override public void saveConfig() {}
    @Override public void saveDefaultConfig() {}
    @Override public void saveResource(String resourcePath, boolean replace) {}
    @Override public void reloadConfig() {}
    @Override public PluginLoader getPluginLoader() { return HeliosPluginLoader.INSTANCE; }
    @Override public Server getServer() { return Bukkit.getServer(); }
    @Override public boolean isEnabled() { return enabled; }
    @Override public boolean isNaggable() { return false; }
    @Override public void setNaggable(boolean canNag) { }
    @Override public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) { return null; }
    @Override public Logger getLogger() { return logger; }
    @Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { return false; }
    @Override public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) { return Collections.emptyList(); }
}
