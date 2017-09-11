package eu.mikroskeem.helios.api.inventory.meta;

import eu.mikroskeem.helios.api.profile.Profile;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


/**
 * Helios Skull meta exposing GameProfile/textures manipulation features
 *
 * @author Mark Vainomaa
 */
public interface HeliosSkullMeta extends SkullMeta {
    /**
     * Gets Profile associated with given skull. Might be absent if skull does not have textures associated
     *
     * @return Wrapped GameProfile (see {@link Profile})
     */
    @Nullable
    Profile getProfile();

    /**
     * Sets Profile to skull
     *
     * @param profile {@link Profile} instance
     */
    void setProfile(@NotNull Profile profile);

    /**
     * Gets textures associated with given skull. Might be absent
     *
     * @return Textures
     */
    @Nullable
    String getTextures();

    /**
     * Sets new textures to skull with new GameProfile
     *
     * @param textures Textures
     * @param ownerUUID GameProfile owner UUID
     */
    void setTextures(@Nullable String textures, UUID ownerUUID);

    /**
     * Sets new textures to skull with new GameProfile. GameProfile owner UUID is picked randomly
     *
     * @param textures Textures
     */
    void setTextures(@Nullable String textures);
}
