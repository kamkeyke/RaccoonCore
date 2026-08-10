package net.kamkeyke.example.registry;

import net.kamkeyke.example.RaccoonCoreMod;
import net.kamkeyke.raccooncore.data.SoundEntry;
import net.kamkeyke.raccooncore.datagen.AutoSoundProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Example registry class for all sound events of the mod.
 * <p>
 * This class also acts as the single source of truth for {@link SoundEntry},
 * which are later consumed by {@link AutoSoundProvider} to automatically
 * generate {@code sounds.json}.
 *
 * <p><b>Recommended pattern:</b>
 * Create helper methods in this class (such as
 * {@code singleSound()}, {@code variedSound()} and
 * {@code customPaths()}) to keep sound registration and datagen
 * synchronized while avoiding duplicated information.
 *
 * <p>Example:
 * <pre>{@code
 * public static final RegistryObject<SoundEvent> VINE_BOOM =
 *     singleSound("vine_boom", "sfx/misc/vine_boom");
 *
 * public static final RegistryObject<SoundEvent> FIRE_CRACKLING =
 *     variedSound("fire_crackling", "sfx/fire_cracklings/fire_crackling", 5);
 * }</pre>
 *
 * <p>
 * These helpers both register the corresponding {@link SoundEvent} and
 * add the matching {@link SoundEntry} to {@link #SOUND_ENTRIES}.
 * The list is later consumed by {@link AutoSoundProvider} to generate
 * {@code sounds.json}, ensuring that both the registry and datagen
 * remain synchronized without duplicating information.
 *
 * <hr>
 *
 * This class was not registered in the ModEventBus and therefore will not appear in-game.
 * <p>
 * It serves only as an example.
 */
@ApiStatus.Internal
public class ModSounds {
    public static final List<SoundEntry> SOUND_ENTRIES = new ArrayList<>();

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RaccoonCoreMod.MODID);

    public static final RegistryObject<SoundEvent> VINE_BOOM =
            singleSound("vine_boom", "sfx/misc/vine_boom.ogg");

    // No use here
    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }

    @SuppressWarnings("SameParameterValue")
    private static RegistryObject<SoundEvent> singleSound(String soundName, String pathAndFileName){
        SOUND_ENTRIES.add(SoundEntry.single(soundName, pathAndFileName));
        return registerSoundEvent(soundName);
    }

    private static RegistryObject<SoundEvent> variedSound(String soundName, String pathAndFileName, int count){
        SOUND_ENTRIES.add(SoundEntry.varied(soundName, pathAndFileName, count));
        return registerSoundEvent(soundName);
    }

    private static RegistryObject<SoundEvent> customPaths(String soundName, String... pathsAndFileNames){
        SOUND_ENTRIES.add(SoundEntry.customPaths(soundName, pathsAndFileNames));
        return registerSoundEvent(soundName);
    }

    private static RegistryObject<SoundEvent> registerSoundEvent(String soundName) {
        return SOUND_EVENTS.register(soundName,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RaccoonCoreMod.MODID, soundName))
        );
    }
}
