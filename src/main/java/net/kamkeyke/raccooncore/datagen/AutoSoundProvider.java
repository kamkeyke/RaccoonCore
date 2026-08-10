package net.kamkeyke.raccooncore.datagen;

import com.google.gson.JsonObject;
import net.kamkeyke.example.datagen.ExampleAutoModSoundProvider;
import net.kamkeyke.raccooncore.data.SoundEntry;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;

import java.util.List;

/**
 * Automatic data provider for generating {@code sounds.json} from a list of {@link SoundEntry}.
 * To see an example of usage head to {@link ExampleAutoModSoundProvider}.
 * <p>
 * Instead of manually calling {@link #singleSound(JsonObject, String, String)},
 * {@link #variedSound(JsonObject, String, String, int)} or
 * {@link #customPaths(JsonObject, String, String...)},
 * this provider automatically generates every sound event from the list returned by
 * {@link #getEntries()}.
 * <p>
 * Each {@link SoundEntry} is analyzed and the appropriate registration method is selected:
 * <ul>
 *     <li>If {@code customPathsAndNames()} is present, {@code customPaths(...)} is used.</li>
 *     <li>If {@code variations() > 0}, {@code variedSound(...)} is used.</li>
 *     <li>Otherwise, {@code singleSound(...)} is used.</li>
 * </ul>
 * <p>
 * In most cases, {@link #getEntries()} should simply return the list of
 * {@link SoundEntry} maintained by your mod's sound registry.
 * This allows your registry to become the single source of truth for both
 * {@link SoundEvent} registration and {@code sounds.json}
 * generation, eliminating duplicated definitions.
 * <p>
 * To generate the file:
 * <ol>
 *     <li>Register this provider during the {@code GatherDataEvent}.</li>
 *     <li>Run your development environment's {@code runData} task
 *     (such as Gradle's {@code runData}, depending on your setup).</li>
 * </ol>
 * <p>
 * This provider only generates the JSON file.
 * All referenced sound files must still exist under
 * {@code assets/<modid>/sounds/}.
 * Missing sound files will result in silent or broken sounds in-game.
 */
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
