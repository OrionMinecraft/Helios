package eu.mikroskeem.helios.api.profile;

import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


/**
 * A Mojang GameProfile wrapper
 *
 * @author Mark Vainomaa
 */
public interface Profile {
    /* *
     * Constructs new wrapped GameProfile
     *
     * @param name Player name. May be null if {@code uuid} is set
     * @param uuid Player UUID. May be null if {@code name} is set
     * @return Wrapped GameProfile (instance of {@link Profile})
     * /
    @Contract("null, null -> fail")
    Profile newProfile(String name, UUID uuid);
    */

    /**
     * Gets profile name
     *
     * @return Profile name
     */
    @Nullable
    String getName();

    /**
     * Gets profile UUID
     *
     * @return Profile UUID
     */
    @Nullable
    UUID getUUID();

    /**
     * Gets whether profile is legacy or not
     *
     * @return Legacy or not
     */
    boolean isLegacy();

    /**
     * Gets GameProfile's properties
     *
     * @return GameProfile properties
     */
    @NotNull
    Multimap<String, Property> getProperties();

    /**
     * Gets wrapped GameProfile object
     *
     * @return GameProfile object
     */
    @NotNull
    Object getWrappedGameProfile();
}
