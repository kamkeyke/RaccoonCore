package net.kamkeyke.example.capability;

import net.kamkeyke.raccooncore.api.IPlayerCapability;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class CapData implements IPlayerCapability<CapData> {
    private boolean value = false;

    public boolean getValue() {
        return value;
    }
    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public void copyFrom(CapData source) {
        this.value = source.value;
    }

    @Override
    public void saveNBTData(CompoundTag tag) {
        tag.putBoolean("value", value);
    }

    @Override
    public void loadNBTData(CompoundTag tag) {
        value = tag.getBoolean("value");
    }
}
