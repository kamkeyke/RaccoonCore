package net.kamkeyke.example.datagen;

import net.kamkeyke.example.RaccoonCoreMod;
import net.kamkeyke.example.registry.ModSounds;
import net.kamkeyke.raccooncore.data.SoundEntry;
import net.kamkeyke.raccooncore.datagen.AutoSoundProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * Example implementation of {@link AutoSoundProvider}.
 * <p>
 * Demonstrates automatic sound registration by returning a list of
 * {@link SoundEntry} instances.
 * <p>
 * See also: {@link ExampleManualModSoundProvider}
 * <hr>
 * This class was not registered in the ModEventBus and therefore will not appear in-game.
 * <p>
 * It serves only as an example.
 */
@ApiStatus.Internal
public class ExampleAutoModSoundProvider extends AutoSoundProvider {
    protected ExampleAutoModSoundProvider(PackOutput output) {
        super(output, RaccoonCoreMod.MODID);
    }

    @Override
    protected List<SoundEntry> getEntries() {
        return ModSounds.SOUND_ENTRIES;
    }
}
