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

package eu.mikroskeem.helios.mod.configuration

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * Server sub-configuration
 *
 * @author Mark Vainomaa
 */
@ConfigSerializable
class ServerConfiguration {
    @Setting(value = "accept-eula",
            comment = "Accepts Mojang EULA by default. See https://account.mojang.com/documents/minecraft_eula. " +
                    "If you don't agree EULA, set this to false")
    var acceptEULA = true
        private set

    @Setting(value = "modify-query-plugins-list",
            comment = "Whether to modify query protocol plugins list or not (https://dinnerbone.com). Turned on by " +
                    "default to avoid data leakage")
    var modifyQueryPluginsList = true
        private set

    @Setting(value = "query-custom-plugin-information",
            comment = "Custom query plugins response. Won't be used if `modify-query-plugins-list` is false")
    var queryCustomPluginInformation = "Helios/Paper on Bukkit: Nice try to peek; " +
            "all these plugins here,; but you failed miserably.; Have a good day!"
        private set
}