package eu.mikroskeem.helios.mod.configuration.categories

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
}