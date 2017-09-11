package eu.mikroskeem.helios.mod.mixins.inventory.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import eu.mikroskeem.helios.api.inventory.meta.HeliosSkullMeta;
import eu.mikroskeem.helios.api.profile.Profile;
import eu.mikroskeem.helios.mod.HeliosMod;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


/**
 * @author Mark Vainomaa
 */
@Mixin(targets = "org/bukkit/craftbukkit/v1_12_R1/inventory/CraftMetaSkull", remap = false)
public abstract class MixinCraftSkullMeta implements HeliosSkullMeta {
    @Shadow private GameProfile profile;

    @Nullable
    @Override
    public Profile getProfile() {
        return this.profile != null ? HeliosMod.INSTANCE.getGameProfileWrapper().wrapGameProfile(profile) : null;
    }

    @Override
    public void setProfile(@Nullable Profile profile) {
        this.profile = (GameProfile) (profile != null ? profile.getWrappedGameProfile() : null);
    }

    @Nullable
    @Override
    public String getTextures() {
        /* Profile is null on skulls which do not have "SkullOwner" set */
        if(this.profile == null)
            return null;

        if(!profile.getProperties().containsKey("textures"))
            return null;

        Property textures = helios$getFirst(profile.getProperties().get("textures"));
        if(textures == null)
            return null;

        return textures.getValue();
    }

    @Override
    public void setTextures(@Nullable String texture, UUID ownerUUID) {
        GameProfile profile = this.profile != null ? this.profile : new GameProfile(ownerUUID, null);
        if(profile.getProperties().containsKey("textures"))
            profile.getProperties().removeAll("textures");

        if(texture == null)
            return;

        profile.getProperties().put("textures", new Property("textures", texture));
    }

    @Override
    public void setTextures(@Nullable String texture) {
        setTextures(texture, UUID.randomUUID());
    }

    @Nullable
    private <T> T helios$getFirst(Collection<T> collection) {
        if(collection instanceof List)
            return ((List<T>) collection).get(0);

        Iterator<T> itr = collection.iterator();
        return itr.hasNext() ? itr.next() : null;
    }
}
