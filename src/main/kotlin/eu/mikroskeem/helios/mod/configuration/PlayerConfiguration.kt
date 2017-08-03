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
 * Player sub-configuration
 *
 * @author Mark Vainomaa
 */
@ConfigSerializable
class PlayerConfiguration {
    @Setting(value = "milliseconds-until-to-mark-player-away",
            comment = "Time how long should player be idle to mark one away")
    var millisecondsUntilToMarkPlayerAway: Long = 120 * 1000
        private set

    @Setting(value = "player-data-saving-disabled",
            comment = "Disables player data saving to disk. Useful for Lobby servers")
    var playerDataSavingDisabled = false
        private set

    @Setting(value = "keepalive-packet-send-threshold",
            comment = "How many ticks should server wait before sending new keep-alive packet to client? " +
                    "Note that client times out if server doesn't send keep-alives for 20 seconds.")
    var keepalivePacketThreshold = 40L
        private set

    @Setting(value = "dont-kick-opped-players-on-idle-timeout",
            comment = "Should opped players get kicked when they are idle for too long? (specified in server.properties" +
                    " using `player-idle-timeout` option)")
    var dontKickOppedPlayersOnIdle = false
        private set
}