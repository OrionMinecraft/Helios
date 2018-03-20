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

package eu.mikroskeem.helios.mod.configuration

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * Command system sub-configuration
 *
 * @author Mark Vainomaa
 */
@ConfigSerializable
class CommandConfiguration {
    @Setting(value = "override-plugin-command-permission-denied-message",
            comment = "Should Helios override plugin's command permission denied message, regardless if it" +
                    "is defined in plugin's plugin.yml or not?")
    var overridePluginCommandPermissionDeniedMessage = true
        private set

    @Setting(value = "permission-denied-message",
            comment = "Command permission denied message. Defaults to Bukkit's default")
    var permissionDeniedMessage = "§cI'm sorry, but you do not have permission to perform this command. " +
            "Please contact the server administrators if you believe that this is in error."
        private set
}