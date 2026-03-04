package net.kamkeyke.raccooncore.util;

import net.kamkeyke.raccooncore.api.IPlayerCapability;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Utility methods for player capability handling.
 * <p>
 * Yes, there's only one method, I know...
 */
public class PlayerCapabilityUtils {

    /**
     * Clones a capability from the original player to the new player instance.
     * <p>
     * Should be called inside {@link PlayerEvent.Clone}.
     * Only performs cloning if {@code wasDeath} is true.
     *
     * @param event       the PlayerEvent.Clone event
     * @param capability  the capability instance
     * @param <T>         capability type extending IPlayerCapability
     */
    public static <T extends IPlayerCapability<T>> void cloneOnDeath(PlayerEvent.Clone event, Capability<T> capability) {
        if (!event.isWasDeath()) return;

        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();

        original.reviveCaps();

        original.getCapability(capability).ifPresent(oldCap ->
                newPlayer.getCapability(capability).ifPresent(newCap ->
                        newCap.copyFrom(oldCap)
                )
        );

        original.invalidateCaps();
    }
}