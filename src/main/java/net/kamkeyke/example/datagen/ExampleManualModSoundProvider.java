package net.kamkeyke.example.datagen;

import com.google.gson.JsonObject;
import net.kamkeyke.example.RaccoonCoreMod;
import net.kamkeyke.raccooncore.datagen.RaccoonSoundProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.ApiStatus;

/**
 * Example implementation of {@link RaccoonSoundProvider}.
 * <p>
 * Demonstrates manual sound registration using
 * {@code singleSound()}, {@code variedSound()} and
 * {@code customPaths()}.
 * <p>
 * See also: {@link ExampleAutoModSoundProvider}
 * <hr>
 * This class was not registered in the ModEventBus and therefore will not appear in-game.
 * <p>
 * It serves only as an example.
 */
@ApiStatus.Internal
public class ExampleManualModSoundProvider extends RaccoonSoundProvider {
    public ExampleManualModSoundProvider(PackOutput output) {
        super(output, RaccoonCoreMod.MODID);
    }

    @Override
    protected void registerSounds(JsonObject root) {
        // Without subfolder
        singleSound(root, "vine_boom", "vineboom.ogg");

        // With subfolder
        singleSound(root, "pluh", "sfx/pluh");
        // You can add .ogg in the end or not, it makes no difference

        // With variations and subfolder
        variedSound(root, "fire_crackling", "sfx/fire_cracklings/fire_crackling.ogg", 3);

        // With variations and without subfolder (messy)
        variedSound(root, "hit", "hit.ogg", 5);

        // Custom paths or names (especial cases)
        customPaths(root, "custom_mix",
                "music/intro.ogg",
                "sfx/bang.ogg",
                "misc/dang.ogg"
        );
    }
}
