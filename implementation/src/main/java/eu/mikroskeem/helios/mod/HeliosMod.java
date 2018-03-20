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

package eu.mikroskeem.helios.mod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import eu.mikroskeem.helios.mod.api.events.BukkitInitializedEvent;
import eu.mikroskeem.helios.mod.configuration.HeliosConfiguration;
import eu.mikroskeem.orion.api.Orion;
import eu.mikroskeem.orion.api.annotations.OrionMod;
import eu.mikroskeem.orion.api.configuration.ObjectConfigurationLoader;
import eu.mikroskeem.orion.api.events.ModConstructEvent;
import eu.mikroskeem.orion.api.events.ModLoadEvent;
import net.minecraft.launchwrapper.LaunchClassLoader;
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
    @Inject private ObjectConfigurationLoader<HeliosConfiguration> configurationLoader;

    @Subscribe
    public void on(ModConstructEvent e) throws Exception {
        INSTANCE = this;
        logger.info("Loading Helios...");

        logger.info("Setting up libraries");
        // Register Kotlin library
        orion.registerLibrary("org.jetbrains.kotlin:kotlin-stdlib:1.2.30");
        orion.registerLibrary("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.2.30");
        orion.registerLibrary("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.2.30");

        // Register AT
        logger.info("Setting up ATs...");
        orion.registerAT(HeliosMod.class.getResource("/helios_at.cfg"));

        // Register mixins
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
        reloadConfiguration();
    }

    @Subscribe
    public void on(BukkitInitializedEvent e) throws Exception {
        Server server = Bukkit.getServer();
    }

    public HeliosConfiguration getConfiguration() {
        return configurationLoader.getConfiguration();
    }

    public void reloadConfiguration() {
        configurationLoader.load();
        configurationLoader.save();
    }
}
