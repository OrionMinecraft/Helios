package eu.mikroskeem.helios.api.profile;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


/**
 * Mojang AuthLib's GameProfile wrapper
 *
 * @author Mark Vainomaa
 */
public interface GameProfileWrapper {
    /**
     * Constructs new GameProfile wrapper using {@code name} and/or {@code uuid}. Either of them has to be present
     *
     * @param name Player name
     * @param uuid Player UUID
     * @return Wrapped GameProfile instance
     */
    @NotNull
    @Contract("null, null -> fail")
    Profile newGameProfile(String name, UUID uuid);

    /**
     * Constructs new GameProfile wrapper using {@code uuid}
     *
     * @param uuid Player UUID
     * @return Wrapped GameProfile instance
     */
    @NotNull
    Profile newGameProfile(@NotNull UUID uuid);

    /**
     * Wraps existing Mojang GameProfile
     *
     * @param mojangGameProfile Mojang GameProfile object
     * @return Wrapped GameProfile instance
     */
    @NotNull
    Profile wrapGameProfile(@NotNull Object mojangGameProfile);

    /**
     * Constructs new GameProfile's Property wrapper
     *
     * @param name Property name
     * @param value Property value
     * @param signature Property signature. Might be absent
     * @return Wrapped Property instance
     */
    @NotNull
    Property newProperty(@NotNull String name, @NotNull String value, @Nullable String signature);
}
