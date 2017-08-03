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

package eu.mikroskeem.helios.plugin

import com.destroystokyo.paper.event.player.IllegalPacketEvent
import com.destroystokyo.paper.event.server.ServerExceptionEvent
import eu.mikroskeem.helios.api.events.error.CommandDispatchExceptionEvent
import eu.mikroskeem.helios.mod.HeliosMod
import eu.mikroskeem.helios.mod.configuration.SentryConfiguration
import eu.mikroskeem.helios.mod.plugin.HeliosPluginBase
import io.sentry.SentryClient
import io.sentry.SentryClientFactory
import io.sentry.event.Event
import io.sentry.event.EventBuilder
import io.sentry.event.interfaces.ExceptionInterface
import io.sentry.event.interfaces.UserInterface
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.RegisteredListener
import java.util.*

/**
 * @author Mark Vainomaa
 */
class HeliosPlugin: HeliosPluginBase(), Listener {
    private lateinit var sentry: SentryClient

    override fun onEnable() {
        if(!getSConfig().enableSentry) return
        val dsn = getSConfig().sentryDsn
        if (dsn.isEmpty()) {
            logger.warning("Not enabling Sentry integration, because DSN is not configured!")
            return
        }

        logger.info("Setting up Sentry connection")
        sentry = SentryClientFactory.sentryClient(dsn)

        logger.info("Setting up listeners")
        server.pluginManager.registerEvents(this, this)

        Runtime.getRuntime().addShutdownHook(Thread({ sentry.closeConnection() }, "Sentry Raven shutdown thread"))
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun on(event: IllegalPacketEvent) {
        reportIllegalPacket(event.player, event.type, event.isShouldKick, event.kickMessage, event.exceptionMessage)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun on(event: ServerExceptionEvent) {
        reportException("Recoverable server exception caught: " + event.exception.message, event.exception)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun on(event: CommandDispatchExceptionEvent) {
        reportCommandException(event.commandSender, event.command, event.label, event.args, event.exception)
    }

    /* For usual exceptions */
    private fun reportException(text: String, e: Throwable) {
        val eventBuilder = EventBuilder()
                .withMessage(text)
                .withLevel(Event.Level.ERROR)
                .withLogger("Helios Error reporter")
                .withSentryInterface(ExceptionInterface(e))
        sentry.sendEvent(eventBuilder.build())
    }

    /* For event passing exceptions */
    private fun reportEventPassException(listener: RegisteredListener, e: Throwable, event: org.bukkit.event.Event) {
        val eventBuilder = EventBuilder()
                .withMessage("Could not pass event $event to listener ${listener.listener}")
                .withLevel(Event.Level.ERROR)
                .withLogger("Helios Error reporter")
                .withSentryInterface(ExceptionInterface(e))
                .withExtra("Plugin", listener.plugin)
                .withExtra("Event", event.toString())
        sentry.sendEvent(eventBuilder.build())
    }

    /* For command execution exceptions */
    private fun reportCommandException(sender: CommandSender, command: Command, label: String, args: Array<String>, e: Throwable) {
        val eventBuilder = EventBuilder()
                .withMessage("Could not handle command '${command.name}'")
                .withLevel(Event.Level.ERROR)
                .withLogger("Helios Error reporter")
                .withSentryInterface(ExceptionInterface(e))
                .withExtra("Command sender", sender)
                .withExtra("Command", command)
                .withExtra("Label", label)
                .withExtra("Arguments", Arrays.toString(args))
        sentry.sendEvent(eventBuilder.build())
    }

    /* For illegal packets */
    private fun reportIllegalPacket(player: Player, type: String, shouldKick: Boolean, kickMessage: String, exceptionMessage: String) {
        val userInterface = UserInterface(player.uniqueId.toString(), player.name, player.address.toString(), "<unavailable>")
        val eventBuilder = EventBuilder()
                .withMessage("Player sent invalid packet '$type', server throw exception: $exceptionMessage")
                .withLevel(Event.Level.ERROR)
                .withLogger("Helios Error reporter")
                .withSentryInterface(userInterface)
                .withExtra("shouldKick", shouldKick)
                .withExtra("kickMessage", kickMessage)
        sentry.sendEvent(eventBuilder.build())
    }

    private fun getSConfig(): SentryConfiguration =
        HeliosMod.INSTANCE.configurationWrapper.configuration.sentryConfiguration
}