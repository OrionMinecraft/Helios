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

package eu.mikroskeem.helios.api.events.error;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


/**
 * This event gets fired when command execution fails
 *
 * @author Mark Vainomaa
 */
public class CommandDispatchExceptionEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Command command;
    private final CommandSender commandSender;
    private final String label;
    private final String[] args;
    private final Throwable exception;

    /**
     * Constructs {@link CommandDispatchExceptionEvent}
     *
     * @param command Command
     * @param commandSender Command sender
     * @param label Command label
     * @param args Command arguments
     * @param exception Exception which got thrown on command execution
     */
    public CommandDispatchExceptionEvent(Command command, CommandSender commandSender, String label, String[] args,
                                         Throwable exception) {
        super(true);
        this.command = command;
        this.commandSender = commandSender;
        this.label = label;
        this.args = args;
        this.exception = exception;
    }

    /**
     * Gets executed command
     *
     * @return Executed {@link Command}
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Gets command sender
     *
     * @return {@link CommandSender} instance
     */
    public CommandSender getCommandSender() {
        return commandSender;
    }

    /**
     * Gets command label (in other words, alias)
     *
     * @return Command label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets command arguments
     *
     * @return Array of command arguments
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Gets exception thrown
     *
     * @return Exception
     */
    public Throwable getException() {
        return exception;
    }

    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}
