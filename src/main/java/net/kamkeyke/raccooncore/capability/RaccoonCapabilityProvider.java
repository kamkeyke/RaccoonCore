package net.kamkeyke.raccooncore.capability;

import net.kamkeyke.raccooncore.api.IPlayerCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Base provider that allows registering multiple player capabilities
 * without boilerplate duplication.
 */
public abstract class RaccoonCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    private final Map<Capability<?>, LazyOptional<?>> capabilityMap = new HashMap<>();
    private final Map<String, LazyOptional<? extends IPlayerCapability<?>>> serializableMap = new HashMap<>();

    protected <T extends IPlayerCapability<T>> void register(String key, Capability<T> capability, NonNullSupplier<T> factory){
        LazyOptional<T> optional = LazyOptional.of(factory);
        capabilityMap.put(capability, optional);
        serializableMap.put(key, optional);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return capabilityMap.containsKey(cap) ? capabilityMap.get(cap).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        serializableMap.forEach((key, optional) ->
                optional.ifPresent(cap -> {
                    CompoundTag subTag = new CompoundTag();
                    cap.saveNBTData(subTag);
                    tag.put(key, subTag);
                })
        );

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        serializableMap.forEach((key, optional) -> {
            if (nbt.contains(key)) {
                optional.ifPresent(cap ->
                        cap.loadNBTData(nbt.getCompound(key))
                );
            }
        });
    }
}
