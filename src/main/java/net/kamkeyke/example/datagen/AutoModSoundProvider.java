package net.kamkeyke.example.datagen;

import net.kamkeyke.example.RaccoonCoreMod;
import net.kamkeyke.example.registry.ModSounds;
import net.kamkeyke.raccooncore.data.SoundEntry;
import net.kamkeyke.raccooncore.datagen.AutoSoundProvider;
import net.minecraft.data.PackOutput;

import java.util.List;

/**
 * See also: {@link ManualModSoundProvider}
 * <hr>
 * This class was not registered in the ModEventBus and therefore will not appear in-game.
 * <p>
 * It serves only as an example.
 */
public class AutoModSoundProvider extends AutoSoundProvider {
    protected AutoModSoundProvider(PackOutput output) {
        super(output, RaccoonCoreMod.MODID);
    }

    @Override
    protected List<SoundEntry> getEntries() {
        return ModSounds.entries;
    }
}
