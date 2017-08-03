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

package eu.mikroskeem.helios.plugin;

import com.destroystokyo.paper.event.player.IllegalPacketEvent;
import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import eu.mikroskeem.helios.api.events.error.CommandDispatchExceptionEvent;
import eu.mikroskeem.helios.mod.HeliosMod;
import eu.mikroskeem.helios.mod.configuration.SentryConfiguration;
import eu.mikroskeem.helios.mod.plugin.HeliosPluginBase;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;
import io.sentry.event.interfaces.ExceptionInterface;
import io.sentry.event.interfaces.UserInterface;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;

import java.util.Arrays;


/**
 * The Helios plugin
 *
 * @author Mark Vainomaa
 */
public final class HeliosPlugin extends HeliosPluginBase implements Listener {
    private SentryClient sentry;

    @Override
    public void onEnable() {
        if(getSConfig().getEnableSentry()) {
            String dsn = getSConfig().getSentryDsn();
            if(dsn.isEmpty()) {
                getLogger().warning("Not enabling Sentry integration, because DSN is not configured!");
                return;
            }


            getLogger().info("Setting up Sentry connection");
            sentry = SentryClientFactory.sentryClient(dsn);

            getLogger().info("Setting up listeners");
            getServer().getPluginManager().registerEvents(this, this);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                sentry.closeConnection();
            }, "Sentry Raven shutdown thread"));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(IllegalPacketEvent event) {
        reportIllegalPacket(event.getPlayer(), event.getType(), event.isShouldKick(),
                event.getKickMessage(), event.getExceptionMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(ServerExceptionEvent event) {
        reportException("Recoverable server exception caught: " + event.getException().getMessage(), event.getException());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(CommandDispatchExceptionEvent event) {
        reportCommandException(event.getCommandSender(), event.getCommand(), event.getLabel(),
                event.getArgs(), event.getException());
    }

    /* For usual exceptions */
    private void reportException(String text, Throwable e) {
        EventBuilder eventBuilder = new EventBuilder()
                .withMessage(text)
                .withLevel(Event.Level.ERROR)
                .withLogger("Helios Error reporter")
                .withSentryInterface(new ExceptionInterface(e));
        sentry.sendEvent(eventBuilder.build());
    }

    /* For event passing exceptions */
    private void reportEventPassException(RegisteredListener listener, Throwable e, org.bukkit.event.Event event) {
        EventBuilder eventBuilder = new EventBuilder()
                .withMessage(String.format(
                        "Could not pass event %s to listener %s",
                        event.toString(),
                        listener.getListener()
                ))
                .withLevel(Event.Level.ERROR)
                .withLogger("Helios Error reporter")
                .withSentryInterface(new ExceptionInterface(e))
                .withExtra("Plugin", listener.getPlugin())
                .withExtra("Event", event.toString());
        sentry.sendEvent(eventBuilder.build());
    }

    /* For command execution exceptions */
    private void reportCommandException(CommandSender sender, Command command, String label, String[] args, Throwable e) {
        EventBuilder eventBuilder = new EventBuilder()
                .withMessage("Could not handle command '" + command.getName() + "'")
                .withLevel(Event.Level.ERROR)
                .withLogger("Helios Error reporter")
                .withSentryInterface(new ExceptionInterface(e))
                .withExtra("Command sender", sender)
                .withExtra("Command", command)
                .withExtra("Label", label)
                .withExtra("Arguments", Arrays.toString(args));
        sentry.sendEvent(eventBuilder.build());
    }

    /* For illegal packets */
    private void reportIllegalPacket(Player player, String type, boolean shouldKick, String kickMessage,
                                    String exceptionMessage) {
        UserInterface userInterface = new UserInterface(
                player.getUniqueId().toString(),
                player.getName(),
                player.getAddress().toString(),
                "<unavailable>");
        EventBuilder eventBuilder = new EventBuilder()
                .withMessage(String.format(
                        "Player sent invalid packet '%s', server throw exception: %s",
                        type, exceptionMessage
                ))
                .withLevel(Event.Level.ERROR)
                .withLogger("Helios Error reporter")
                .withSentryInterface(userInterface)
                .withExtra("shouldKick", shouldKick)
                .withExtra("kickMessage", kickMessage);
        sentry.sendEvent(eventBuilder.build());
    }

    private SentryConfiguration getSConfig() {
        return HeliosMod.INSTANCE.getConfigurationWrapper().getConfiguration().getSentryConfiguration();
    }
}
