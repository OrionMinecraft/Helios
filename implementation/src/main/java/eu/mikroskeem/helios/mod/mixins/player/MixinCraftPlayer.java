/*
 * This file is part of project Helios, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017-2018 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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

package eu.mikroskeem.helios.mod.mixins.player;

import eu.mikroskeem.helios.api.entity.Player;
import eu.mikroskeem.helios.api.events.player.chat.PlayerPluginSendMessageEvent;
import eu.mikroskeem.helios.mod.HeliosMod;
import eu.mikroskeem.shuriken.common.Ensure;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.TileEntitySign;
import org.apache.commons.lang.Validate;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;


/**
 * CraftPlayer Mixin, to provide implementation for {@link Player}
 *
 * @author Mark Vainomaa
 */
@Mixin(value = CraftPlayer.class, remap = false)
@Implements(@Interface(iface = CommandSender.class, prefix = "helios$"))
public abstract class MixinCraftPlayer implements Player {
    @Shadow public abstract EntityPlayer getHandle();

    @Override
    public long getLastActiveTime() {
        return this.getHandle().J();
    }

    @Override
    public boolean isAway() {
        long configuredAwayTime = HeliosMod.INSTANCE.getConfiguration()
                .getPlayer()
                .getMillisecondsUntilToMarkPlayerAway();
        long currentTime = MinecraftServer.aw();
        return (currentTime - this.getLastActiveTime()) >= configuredAwayTime;
    }

    @Intrinsic(displace = true)
    public void helios$sendMessage(String message) {
        PlayerPluginSendMessageEvent event = new PlayerPluginSendMessageEvent(this, message);
        getServer().getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            sendMessage(message);
        }
    }

    @Override
    public void sendJsonMessage(String jsonMessage) {
        IChatBaseComponent nmsChatComponent = IChatBaseComponent.ChatSerializer
                .a(Ensure.notNull(jsonMessage, "Message must not be null!"));
        PacketPlayOutChat packet = new PacketPlayOutChat(nmsChatComponent);
        getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    @Intrinsic // If https://github.com/PaperMC/Paper/pull/1070 gets finally merged
    public void openSign(Sign sign) {
        Validate.isTrue(sign.getWorld().equals(this.getWorld()), "Sign must be in the same world as player is in");
        CraftSign craftSign = (CraftSign) sign;
        TileEntitySign teSign = craftSign.getTileEntity();
        // Make sign editable temporarily, will be set back to false in PlayerConnection later
        teSign.isEditable = true;

        getHandle().openSign(teSign);
    }
}
