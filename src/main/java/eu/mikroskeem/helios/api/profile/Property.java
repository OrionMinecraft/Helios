package eu.mikroskeem.helios.api.profile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.PublicKey;


/**
 * Mojang GameProfile Property wrapper
 *
 * @author Mark Vainomaa
 */
public interface Property {
    /**
     * Gets property name
     *
     * @return Property name
     */
    @NotNull
    String getName();

    /**
     * Gets property value
     *
     * @return Property value
     */
    @NotNull
    String getValue();

    /**
     * Gets property signature. Might be absent
     *
     * @return Property signature
     */
    @Nullable
    String getSignature();

    /**
     * Returns whether Property has a signature or not
     *
     * @return Whether Property has a signature or not
     */
    default boolean hasSignature() {
        return getSignature() != null;
    }

    /**
     * Returns whether Property has valid signature (against {@code publicKey})
     *
     * @param publicKey Public key to check signature against
     * @return Wheter Property has valid signature or not
     */
    boolean isSignatureValid(@NotNull PublicKey publicKey);

    /**
     * Gets wrapped GameProfile Property object
     *
     * @return GameProfile Property object
     */
    @NotNull
    Object getWrappedProperty();
}
