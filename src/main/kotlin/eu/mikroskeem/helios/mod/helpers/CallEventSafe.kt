package eu.mikroskeem.helios.mod.helpers

import org.bukkit.Bukkit
import org.bukkit.event.Event

/**
 * Null-safe event calling used in server internals
 *
 * @author Mark Vainomaa
 */
object CallEventSafe {
    @JvmStatic
    fun <T: Event> callEvent(event: T): T = event.apply { Bukkit.getServer()?.pluginManager?.callEvent(event) }
}