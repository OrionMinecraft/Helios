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

package eu.mikroskeem.helios.mod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import eu.mikroskeem.helios.api.profile.GameProfileWrapper;
import eu.mikroskeem.helios.mod.api.events.BukkitInitializedEvent;
import eu.mikroskeem.helios.mod.configuration.Configuration;
import eu.mikroskeem.helios.mod.helpers.HeliosGameProfileWrapper;
import eu.mikroskeem.helios.mod.plugin.HeliosPluginDescription;
import eu.mikroskeem.helios.mod.plugin.HeliosPluginLoader;
import eu.mikroskeem.helios.plugin.HeliosPlugin;
import eu.mikroskeem.orion.api.Orion;
import eu.mikroskeem.orion.api.annotations.OrionMod;
import eu.mikroskeem.orion.api.events.ModConstructEvent;
import eu.mikroskeem.orion.api.events.ModLoadEvent;
import eu.mikroskeem.shuriken.common.SneakyThrow;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import javax.inject.Inject;


/**
 * The Helios Mod
 *
 * @author Mark Vainomaa
 */
@OrionMod(id = "helios")
public final class HeliosMod {
    public static HeliosMod INSTANCE;

    @Inject public EventBus eventBus;
    @Inject public Logger logger;
    @Inject private Orion orion;
    @Inject private ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private Configuration configuration;

    /* API */
    private GameProfileWrapper gameProfileWrapper;

    public GameProfileWrapper getGameProfileWrapper() {
        return gameProfileWrapper;
    }

    @Subscribe
    public void on(ModConstructEvent e) throws Exception {
        INSTANCE = this;
        logger.info("Loading Helios...");

        logger.info("Setting up libraries");
        /* ** Register Kotlin library */
        orion.registerLibrary("org.jetbrains.kotlin:kotlin-stdlib:1.1.4-3");
        orion.registerLibrary("org.jetbrains.kotlin:kotlin-stdlib-jre7:1.1.4-3");
        orion.registerLibrary("org.jetbrains.kotlin:kotlin-stdlib-jre8:1.1.4-3");
        orion.registerLibrary("com.fasterxml.jackson.core:jackson-core:2.8.9");

        /* ** Add slf4j-over-log4j */
        orion.registerLibrary("org.apache.logging.log4j:log4j-slf4j-impl:2.8.1");

        /* ** Sentry */
        orion.registerLibrary("io.sentry:sentry:1.3.0");
        orion.registerLibrary("org.slf4j:slf4j-api:1.7.25");

        /* Register AT */
        logger.info("Setting up ATs...");
        orion.registerAT(HeliosMod.class.getResource("/helios_at.cfg"));

        /* Register Mixins */
        logger.info("Setting up core mixins...");
        orion.registerMixinConfig("mixins.helios.core.json");
        orion.registerMixinConfig("mixins.helios.event.json");
        orion.registerMixinConfig("mixins.helios.exploit.json");
        orion.registerMixinConfig("mixins.helios.inventory.json");
        orion.registerMixinConfig("mixins.helios.player.json");
        orion.registerMixinConfig("mixins.helios.packets.json");
        orion.registerMixinConfig("mixins.helios.world.json");
    }

    @Subscribe
    public void on(ModLoadEvent e) throws Exception {
        logger.info("Loading configuration...");
        configuration = new Configuration(configurationLoader);
        loadConfiguration();
        saveConfiguration();

        gameProfileWrapper = HeliosGameProfileWrapper.INSTANCE;
    }

    @Subscribe
    public void on(BukkitInitializedEvent e) throws Exception {
        Server server = Bukkit.getServer();
        HeliosPluginLoader.HELIOS_PLUGINS.put(
                HeliosPlugin.class,
                new HeliosPluginDescription("Helios", "0.0.1", HeliosPlugin.class.getName())
        );
        server.getPluginManager().registerInterface(HeliosPluginLoader.class);
    }

    public void loadConfiguration() {
        try {
            configuration.load();
        } catch (Exception e) {
            SneakyThrow.throwException(e);
        }
    }

    public void saveConfiguration() {
        try {
            configuration.save();
        } catch (Exception e) {
            SneakyThrow.throwException(e);
        }
    }

    public Configuration getConfigurationWrapper() {
        return configuration;
    }
}
