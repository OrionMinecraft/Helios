package eu.mikroskeem.helios.mod.mixins.packets;

import eu.mikroskeem.shuriken.common.Ensure;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


/**
 * Adds outgoing chat packet serialized length limit check
 *
 * @author Mark Vainomaa
 */
@Mixin(value = PacketPlayOutChat.class, remap = false)
public abstract class MixinPacketPlayOutChat {
    private final static String helios$SERIALIZE = "b(Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;)V";
    private final static String helios$SERIALIZE_STRING = "Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;" +
            "a(Ljava/lang/String;)Lnet/minecraft/server/v1_12_R1/PacketDataSerializer;";

    @Redirect(method = helios$SERIALIZE, at = @At(
            value = "INVOKE",
            target = helios$SERIALIZE_STRING
    ))
    private PacketDataSerializer serializeString(PacketDataSerializer serializer, String raw) {
        Ensure.ensureCondition(raw.length() <= 32767, "Serialized chat string is too large! (Not " +
                raw.length() + " <= 32767!)");
        return serializer.a(raw);
    }
}
