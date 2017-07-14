package eu.mikroskeem.helios.mod.configuration.categories

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * World sub-configuration
 *
 * @author Mark Vainomaa
 */
@ConfigSerializable
class WorldConfiguration {
    @Setting(value = "disable-uuid-conversion", comment = "Disables UUID conversion on startup, useful if server " +
                    "does *certainly* not need to migrate legacy data")
    var disableUUIDConversion = false
        private set
}