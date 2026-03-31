package net.kamkeyke.raccooncore.datagen;

import com.google.gson.JsonObject;
import net.kamkeyke.raccooncore.data.SoundEntry;
import net.minecraft.data.PackOutput;

import java.util.List;

public abstract class AutoSoundProvider extends RaccoonSoundProvider{
    protected AutoSoundProvider(PackOutput output, String modid) {
        super(output, modid);
    }

    protected abstract List<SoundEntry> getEntries();

    @Override
    protected void registerSounds(JsonObject root) {
        for (SoundEntry entry : getEntries()) {
            if (entry.customPathsAndNames() != null) {
                customPaths(root, entry.soundName(), entry.customPathsAndNames());
            }
            else if (entry.variations() > 0) {
                variedSound(root, entry.soundName(), entry.basePathAndName(), entry.variations());
            }
            else {
                singleSound(root, entry.soundName(), entry.basePathAndName());
            }
        }
    }
}
