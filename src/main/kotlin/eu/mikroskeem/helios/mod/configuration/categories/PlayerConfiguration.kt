package eu.mikroskeem.helios.mod.configuration.categories

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
}