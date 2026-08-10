package net.kamkeyke.raccooncore.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.kamkeyke.example.datagen.ExampleAutoModSoundProvider;
import net.kamkeyke.example.datagen.ExampleManualModSoundProvider;
import net.kamkeyke.raccooncore.data.SoundEntry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Base data provider for generating {@code sounds.json}.
 * <p>
 * There are two ways to generate sound definitions:
 * <ul>
 *     <li>Extend this class to manually register every sound.</li>
 *     <li>Extend {@link AutoSoundProvider} to automatically generate everything
 *     from a list of {@link SoundEntry}.</li>
 * </ul>
 * <p>
 * <b>Use only one approach.</b>
 * Both providers generate the same {@code assets/<modid>/sounds.json} file,
 * so registering both at the same time will cause one provider to overwrite
 * the other.
 * <p>
 * To see examples, head to {@link ExampleManualModSoundProvider} or
 * {@link ExampleAutoModSoundProvider}.
 * <p>
 * This class simplifies sound registration by providing helper methods
 * for common use cases such as single sounds, variations, and custom lists.
 * <p>
 * All sound files must be placed inside:
 * {@code assets/<modid>/sounds/}
 * <p>
 * Supported structures:
 * <ul>
 *     <li>Flat: {@code sounds/my_sound.ogg}</li>
 *     <li>Subfolders: {@code sounds/music/my_song.ogg}</li>
 *     <li>Nested: {@code sounds/sfx/voices/voice_1.ogg}</li>
 * </ul>
 * <p>
 * Variation rule:
 * <ul>
 *     <li>Use the pattern {@code name_#} starting at 1.</li>
 *     <li>Example: {@code sound_1.ogg}, {@code sound_2.ogg}, {@code sound_3.ogg}.</li>
 * </ul>
 * <p>
 * Usage:
 * <pre>{@code
 * @Override
 * protected void registerSounds(JsonObject root) {
 *     singleSound(root, "vine_boom", "misc/vine_boom");
 *     variedSound(root, "fire_crackling", "sfx/fire_cracklings/fire_crackling", 5);
 * }
 * }</pre>
 * <p>
 * This provider only generates the JSON file.
 * All referenced sound files must still exist under
 * {@code assets/<modid>/sounds/}.
 * Missing sound files will result in silent or broken sounds in-game.
 */
public abstract class RaccoonSoundProvider implements DataProvider {
    protected final PackOutput output;
    protected final String modid;

    protected RaccoonSoundProvider(PackOutput output, String modid) {
        this.output = output;
        this.modid = modid;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        JsonObject root = new JsonObject();

        registerSounds(root);

        Path path = output.getOutputFolder().resolve("assets/" + modid + "/sounds.json");

        return DataProvider.saveStable(cache, root, path);
    }

    protected abstract void registerSounds(JsonObject root);


    // ----- API Principal -----

    /**
     * Registers a single sound event.
     * <p>
     * The sound file must exist inside {@code assets/<modid>/sounds/}.
     * If it's inside a subfolder, you need to specify that subfolder as well.
     * <p>
     * Example:
     * <pre>{@code
     *   singleSound(root, "cool_music", "music/cool_music.ogg");
     * }</pre>
     *
     * Generates:
     * <pre>{@code
     *   "cool_music": {
     *     "subtitles": "sound.<modid>.cool_music",
     *     "sounds": [
     *       "<modid>:music/cool_music"
     *     ]
     *   }
     * }</pre>
     */
    protected void singleSound(JsonObject root, String soundName, String pathAndFileName) {
        JsonObject json = subtitles(soundName);

        JsonArray sounds = new JsonArray();
        sounds.add(modid + ":" + normalize(pathAndFileName));

        json.add("sounds", sounds);
        root.add(soundName, json);
    }

    /**
     * Registers a sound event with multiple numbered variations.
     * <p>
     * Files must follow the pattern: {@code name_1, name_2, name3, ...}
     * <p>
     * Example:
     * <pre>{@code
     *   variedSound(root, "fire_crackling", "sfx/fire_cracklings/fire_crackling", 5);
     * }</pre>
     *
     * Generates:
     * <pre>{@code
     *   "fire_crackling": {
     *     "subtitles": "sound.<modid>.fire_crackling",
     *     "sounds": [
     *       "<modid>:sfx/fire_cracklings/fire_crackling_1"
     *       "<modid>:sfx/fire_cracklings/fire_crackling_2"
     *       ...
     *     ]
     *   }
     * }</pre>
     */
    protected void variedSound(JsonObject root, String soundName, String pathAndFileName, int count) {
        JsonObject json = subtitles(soundName);

        JsonArray sounds = new JsonArray();

        for (int i = 1; i <= count; i++) {
            sounds.add(modid + ":" + normalize(pathAndFileName) + "_" + i);
        }

        json.add("sounds", sounds);
        root.add(soundName, json);
    }

    /**
     * Registers a sound event with multiple variations from different places.
     * <p>
     * Useful when you're disorganized or your sounds come from different folders for some reason.
     * Can be used if your sounds have variations with different names too.
     * <p>
     * Example:
     * <pre>{@code
     *   customPaths(root, "weird_sound",
     *     "music/a.ogg",
     *     "music/b.ogg",
     *     "sfx/c.ogg"
     *   );
     * }</pre>
     */
    protected void customPaths(JsonObject root, String name, String... pathsAndFileNames) {
        JsonObject json = subtitles(name);

        JsonArray sounds = new JsonArray();
        for (String path : pathsAndFileNames) {
            sounds.add(modid + ":" + normalize(path));
        }

        json.add("sounds", sounds);
        root.add(name, json);
    }


    // ----- Helpers internos -----

    private JsonObject subtitles(String name) {
        JsonObject json = new JsonObject();
        json.addProperty("subtitles", "sound." + modid + "." + name);
        return json;
    }


    private String normalize(String path) {
        // remove .ogg do final do path
        if (path.endsWith(".ogg")) {
            path = path.substring(0, path.length() - 4);
        }
        return path;
    }

    @Override
    public @NotNull String getName() {
        return "Sound Provider: " + modid;
    }
}
