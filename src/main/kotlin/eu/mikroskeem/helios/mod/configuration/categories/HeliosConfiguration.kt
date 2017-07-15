package eu.mikroskeem.helios.mod.configuration.categories

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
}