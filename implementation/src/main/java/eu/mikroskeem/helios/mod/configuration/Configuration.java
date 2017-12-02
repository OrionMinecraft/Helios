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

package eu.mikroskeem.helios.mod.configuration;

import eu.mikroskeem.helios.mod.configuration.HeliosConfiguration;
import eu.mikroskeem.shuriken.common.SneakyThrow;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;


/**
 * Helios configuration wrapper
 *
 * @author Mark Vainomaa
 */
public final class Configuration {
    private final ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
    private ObjectMapper<HeliosConfiguration>.BoundInstance configurationMapper;
    private CommentedConfigurationNode baseNode;
    private HeliosConfiguration heliosConfigurationInstance;

    public Configuration(ConfigurationLoader<CommentedConfigurationNode> configurationLoader) {
        this.configurationLoader = configurationLoader;
        try {
            configurationMapper = ObjectMapper.forClass(HeliosConfiguration.class).bindToNew();
            heliosConfigurationInstance = configurationMapper.getInstance();
        } catch (Exception e) {
            SneakyThrow.throwException(e);
        }
    }

    public void load() throws Exception {
        baseNode = configurationLoader.load();
        heliosConfigurationInstance = configurationMapper.populate(baseNode.getNode("helios"));
    }

    public void save() throws Exception {
        configurationMapper.serialize(baseNode.getNode("helios"));
        configurationLoader.save(baseNode);
    }

    public HeliosConfiguration getConfiguration() {
        return heliosConfigurationInstance;
    }
}