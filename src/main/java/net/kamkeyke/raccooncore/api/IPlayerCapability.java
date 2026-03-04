package net.kamkeyke.raccooncore.api;

import net.minecraft.nbt.CompoundTag;

/**
 * Base contract for player capabilities.
 *
 * @param <T> self type
 */
public interface IPlayerCapability<T> {

    /**
     * Copies data from another instance.
     */
    void copyFrom(T source);

    /**
     * Saves internal state into the given tag.
     */
    void saveNBTData(CompoundTag tag);

    /**
     * Loads internal state from the given tag.
     */
    void loadNBTData(CompoundTag tag);
}
