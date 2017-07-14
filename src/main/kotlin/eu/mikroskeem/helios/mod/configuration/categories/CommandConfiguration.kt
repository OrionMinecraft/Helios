package eu.mikroskeem.helios.mod.configuration.categories

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