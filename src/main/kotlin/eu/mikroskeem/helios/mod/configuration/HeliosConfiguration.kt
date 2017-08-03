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
 * Helios configuration
 *
 * @author Mark Vainomaa
 */
@ConfigSerializable
class HeliosConfiguration {
    @Setting(value = "player", comment = "Player configuration")
    var playerConfiguration = PlayerConfiguration()
        private set

    @Setting(value = "world", comment = "World configuration")
    var worldConfiguration = WorldConfiguration()
        private set

    @Setting(value = "command", comment = "Command system configuration")
    var commandConfiguration = CommandConfiguration()
        private set

    @Setting(value = "server", comment = "Server configuration")
    var serverConfiguration = ServerConfiguration()
        private set

    @Setting(value = "exploit", comment = "Anti-exploit configuration")
    var exploitConfiguration = ExploitConfiguration()
        private set

    @Setting(value = "sentry", comment = "Sentry integration configuration")
    var sentryConfiguration = SentryConfiguration()
        private set
}